# 4. 기초적인 엔티티 및 API 흐름 만들기: 카드 생성 API

> 원본: Obsidian 일지(2026-07-27 ~ 2026-07-28) 및 커밋 `# 5. 기초적인 엔티티 및 API 흐름 만들기`

첫 도메인 기능. "카드 하나 저장" 흐름을 Entity → Repository → Service → Controller → DTO까지 전 계층에 걸쳐 구현하고, Bruno로 실제 테스트까지 완료.

## 패키지 구조 결정
`common/`(공통 인프라)과 별도로 도메인별 패키지(`card/`)를 두는 **package-by-feature** 방식 채택. 도메인이 늘어날수록(인증, 플래시카드 등) 관련 파일을 한 폴더에서 관리하기 위함.

## DB 스키마와의 정합성 이슈 (중요한 방향 전환)
`V1__init.sql`에는 이미 기획 단계에서 짜둔 `cards` 테이블(uuid id, `word`, `part_of_speech`, `phonetic`, `definition_en`, `definition_native`, `user_id` FK 등)이 있었는데, 처음 만든 `Card` 엔티티(`Long id`, `expression`, `meaning`)는 이거랑 전혀 다른 형태였음. Hibernate `ddl-auto: validate` 때문에 테이블/컬럼이 안 맞으면 앱이 아예 안 켜짐.

**결정**: 엔티티가 기준이 된다 — DB가 엔티티를 따라가도록 마이그레이션으로 맞춤 (기획 당시 짜둔 스키마를 그대로 따르지 않기로 함). `V2__align_cards_with_entity.sql` 추가:
- `user_id` FK 제약 + `NOT NULL` 제거 (아직 인증/User 엔티티가 없어서 임시 조치, TODO로 남김)
- `word` → `expression`으로 컬럼명 변경
- `meaning` 컬럼 추가
- 아직 안 쓰는 `part_of_speech`, `phonetic`, `definition_en`, `definition_native` 컬럼 제거 (필요해지면 나중에 다시 추가)

## Cards — Entity
클래스명을 `Card`에서 `Cards`로 리네이밍(테이블명 `cards`와 맞춤), `id` 타입도 `Long` → `UUID`로 변경.

```java
@Entity
@Getter
@Setter
@NoArgsConstructor   // JPA 기본 생성자
@AllArgsConstructor  // @Builder가 사용
@Builder
public class Cards {
    @Id
    @GeneratedValue
    private UUID id;

    private String expression;
    private String meaning;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```
- 이름을 `Word`가 아닌 `Card`(→`Cards`)로 정함 — 단어뿐 아니라 구동사/문장까지 다 `String` 필드 하나로 커버 가능하고, 나중에 플래시카드 기능과도 자연스럽게 이어짐.
- `id`를 처음엔 `Long`으로 시작했다가(과설계 방지 목적), 기존 DB 설계(`cards.id`가 `uuid`)를 따르기로 하면서 다시 `UUID`로 전환.
- `@Builder` 사용 시 `@NoArgsConstructor`(JPA용)와 `@AllArgsConstructor`(빌더용) 세트가 필요하다는 점 학습 — `@NoArgsConstructor`가 있으면 `@Builder`가 all-args 생성자를 자동 생성해주지 않기 때문.

## CardRepository
```java
public interface CardRepository extends JpaRepository<Cards, UUID> {
}
```
`JpaRepository<T, ID>`의 `ID`는 엔티티의 `@Id` 타입과 일치해야 함 — `Long`으로 남겨뒀다가 `Cards.id`를 `UUID`로 바꾼 뒤 안 맞춰서 런타임 에러 날 뻔한 걸 미리 잡음.

## DTO — CreateCardRequest / CreateCardResponse
네이밍은 `동작(Create) + 도메인(Card) + Request/Response`로 통일 (메서드 이름 `createCard`와 대응).

- `CreateCardRequest`(record): `expression`, `meaning`만 받음. `toEntity()` 메서드로 자기 자신을 `Cards`로 변환.
- `CreateCardResponse`(record): `id`(UUID), `expression`, `meaning`, `createdAt` 포함. 정적 팩토리 `from(Cards cards)`로 Entity → DTO 변환.
- Entity를 API 요청/응답에 직접 노출하지 않기로 결정 (클라이언트가 `id`/`createdAt` 같은 서버 관리 필드를 조작할 수 있는 문제, 스키마 변경이 API를 깨는 문제 방지).

## CardService / CardController
```java
@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public CreateCardResponse createCard(Cards card) {
        Cards saved = cardRepository.save(card);
        return CreateCardResponse.from(saved);
    }
}
```

```java
@RestController
@RequestMapping("/api/v1/card")   // 단수 — 컨트롤러 경로는 아직 단수로 남아있음
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping()
    public ApiResponse<CreateCardResponse> createCard(@RequestBody CreateCardRequest request) {
        Cards card = request.toEntity();
        return ApiResponse.of(ResponseCode.SUCCESS, cardService.createCard(card));
    }
}
```

## 진행 중 발견/수정한 실수들 (학습 포인트)
- `@RequiredArgsConstructor`는 `final` 필드만 생성자에 넣어줌 — Service 필드에 `final`을 빠뜨려서 의존성 주입이 안 되던 걸 발견하고 수정.
- `@RestController("card")`처럼 어노테이션에 문자열을 넣으면 **URL 경로가 아니라 스프링 빈 이름**이 된다는 점 확인 — `@RequestMapping("/api/v1/card")`으로 별도 지정하는 게 맞음.
- `@RequestBody`(파라미터, JSON 바디를 객체로 읽어옴)와 `@ResponseBody`(메서드/클래스, 반환값을 JSON으로 씀)를 헷갈려서 처음엔 `@ResponseBody`를 파라미터 자리에 필요한 걸로 착각 — `@RestController`가 이미 `@ResponseBody`를 포함하고 있다는 것도 함께 확인.
- API 테스트 도구로 Bruno 채택 — Postman은 무겁다고 판단, Insomnia는 회사 계정과 섞일까 봐 우려해서 로컬/오프라인 기반의 Bruno로 결정.

## 테스트
Bruno로 `POST http://localhost:8080/api/v1/card` 요청 (`{"expression": "apple", "meaning": "사과"}`) → `SUCCESS` 응답과 함께 저장된 카드(`id`, `expression`, `meaning`) 정상 반환 확인.

## 알려진 이슈 (다음 작업 후보)
- **`createdAt`/`updatedAt`이 항상 `null`로 저장됨** — 엔티티에 값을 채워주는 로직이 없어서, Hibernate가 명시적으로 `NULL`을 insert함 (DB 컬럼의 `DEFAULT now()`는 컬럼이 아예 안 들어갈 때만 적용되므로 무시됨). `@PrePersist`/`@PreUpdate` 콜백이나 Hibernate의 `@CreationTimestamp`/`@UpdateTimestamp` 어노테이션으로 해결 예정.
- `user_id`는 FK/NOT NULL 없이 임시로 열어둔 상태 — User 엔티티/인증 기능이 생기면 재검토 필요.
- 컨트롤러 경로(`/api/v1/card`, 단수)와 테이블명(`cards`, 복수)이 서로 다름 — 나중에 통일할지 결정 필요.

## 상태
코드 작성, 컴파일, DB 스키마 정합성 확보, Bruno를 통한 실제 API 테스트까지 완료.
