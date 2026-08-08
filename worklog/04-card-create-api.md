# 4. 기초적인 엔티티 및 API 흐름 만들기: 카드 생성 API

> 원본: Obsidian 일지(2026-07-27 ~ 2026-07-28) 및 커밋 `# 5. 기초적인 엔티티 및 API 흐름 만들기`

첫 도메인 기능. "카드 하나 저장" 흐름을 Entity → Repository → Service → Controller → DTO까지 전 계층에 걸쳐 최소 구현.

## 패키지 구조 결정
`common/`(공통 인프라)과 별도로 도메인별 패키지(`card/`)를 두는 **package-by-feature** 방식 채택. 도메인이 늘어날수록(인증, 플래시카드 등) 관련 파일을 한 폴더에서 관리하기 위함.

## Card — Entity
```java
@Entity
@Getter
@Setter
@NoArgsConstructor   // JPA 기본 생성자
@AllArgsConstructor  // @Builder가 사용
@Builder
public class Card {
    @Id
    @GeneratedValue
    private Long id;

    private String expression;
    private String meaning;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```
- 이름을 `Word`가 아닌 `Card`로 정함 — 단어뿐 아니라 구동사/문장까지 다 `String` 필드 하나로 커버 가능하고, 나중에 플래시카드 기능과도 자연스럽게 이어짐.
- `id`를 `String`(UUID/짧은 랜덤 문자열)으로 바꿔볼지 고민했으나, 지금 규모에서는 과설계로 판단하고 `Long` 자동증가로 원복. 인덱스 성능이나 ID 추측 방지가 실제로 필요해지면 그때 재검토하기로 함.
- `@Builder` 사용 시 `@NoArgsConstructor`(JPA용)와 `@AllArgsConstructor`(빌더용) 세트가 필요하다는 점 학습 — `@NoArgsConstructor`가 있으면 `@Builder`가 all-args 생성자를 자동 생성해주지 않기 때문.

## CardRepository
```java
public interface CardRepository extends JpaRepository<Card, Long> {
}
```
Spring Data JPA가 `save`, `findById` 등 기본 구현을 런타임에 자동 생성.

## DTO — CreateCardRequest / CreateCardResponse
네이밍은 `동작(Create) + 도메인(Card) + Request/Response`로 통일 (메서드 이름 `createCard`와 대응).

- `CreateCardRequest`(record): `expression`, `meaning`만 받음. `toEntity()` 메서드로 자기 자신을 `Card`로 변환.
- `CreateCardResponse`(record): `id`, `expression`, `meaning`, `createdAt` 포함. 정적 팩토리 `from(Card card)`로 Entity → DTO 변환.
- Entity를 API 요청/응답에 직접 노출하지 않기로 결정 (클라이언트가 `id`/`createdAt` 같은 서버 관리 필드를 조작할 수 있는 문제, 스키마 변경이 API를 깨는 문제 방지).

## CardService
```java
@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public CreateCardResponse createCard(Card card) {
        Card saved = cardRepository.save(card);
        return CreateCardResponse.from(saved);
    }
}
```

## CardController
```java
@RestController
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping()
    public ApiResponse<CreateCardResponse> createCard(@RequestBody CreateCardRequest request) {
        Card card = request.toEntity();
        return ApiResponse.of(ResponseCode.SUCCESS, cardService.createCard(card));
    }
}
```

## 진행 중 발견/수정한 실수들 (학습 포인트)
- `@RequiredArgsConstructor`는 `final` 필드만 생성자에 넣어줌 — Service 필드에 `final`을 빠뜨려서 의존성 주입이 안 되던 걸 발견하고 수정.
- `@RestController("card")`처럼 어노테이션에 문자열을 넣으면 **URL 경로가 아니라 스프링 빈 이름**이 된다는 점 확인 — `@RequestMapping("/api/v1/card")`으로 별도 지정하는 게 맞음.
- `@RequestBody`(파라미터, JSON 바디를 객체로 읽어옴)와 `@ResponseBody`(메서드/클래스, 반환값을 JSON으로 씀)를 헷갈려서 처음엔 `@ResponseBody`를 파라미터 자리에 필요한 걸로 착각 — `@RestController`가 이미 `@ResponseBody`를 포함하고 있다는 것도 함께 확인.

## 상태
코드 작성 및 컴파일 확인 완료. **Postman을 통한 실제 API 테스트는 아직 안 함** — 다음에 이어서 진행.
