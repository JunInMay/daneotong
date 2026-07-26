# 3. 예외 처리 구조 짜기

> 원본: Obsidian `작업/4. 예외 처리 구조 짜기.md`

## 목표 흐름
`throw DaneotongException` → `GlobalExceptionHandler`가 캐치 → `ApiResponse`로 변환해 반환

## DaneotongException — 완료
`RuntimeException`을 상속. `ResponseCode`를 필드로 들고 있고, 필요하면 커스텀 메시지도 받을 수 있는 생성자 2개.

```java
public class DaneotongException extends RuntimeException {
    private final ResponseCode responseCode;

    public DaneotongException(ResponseCode responseCode) { ... }
    public DaneotongException(ResponseCode responseCode, String message) { ... }
}
```

## ResponseCode에 HttpStatus 추가 — 완료
각 응답 코드가 어떤 HTTP 상태코드로 나갈지 `HttpStatusCode` 필드로 들고 있도록 확장.
(자바 enum은 생성자 기본값을 지원하지 않으므로, 앞으로 추가하는 모든 코드마다 상태코드를 명시적으로 지정해야 함.)

## GlobalExceptionHandler — 완료
`DaneotongException` 전용 핸들러와, 그 외 예상 못한 예외를 잡는 catch-all 핸들러 둘 다 완성. 둘 다 `ResponseEntity<ApiResponse<Object>>`를 반환해서 `ResponseCode`의 `HttpStatusCode`가 실제 HTTP 상태 줄에 반영됨.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ApiResponse<Object>> error(DaneotongException e) {
        return ResponseEntity.status(e.getHttpStatusCode())
                .body(ApiResponse.of(e));
    }

    @ExceptionHandler
    ResponseEntity<ApiResponse<Object>> unexpectedError(Exception e) {
        ResponseCode code = ResponseCode.UNKNOWN_ERROR;
        return ResponseEntity.status(code.getHttpStatusCode())
                .body(ApiResponse.of(code));
    }
}
```

스프링은 예외 타입이 여러 핸들러와 매칭될 때 가장 구체적인 타입을 우선 선택하므로, `DaneotongException`은 항상 전용 핸들러가 먼저 잡고 그 외는 catch-all로 흘러감.

`ResponseCode`에 공용 서버 에러 코드 `UNKNOWN_ERROR`(`HttpStatus.INTERNAL_SERVER_ERROR`) 추가함. 도메인별 에러 코드(회원가입 검증 등)는 아직 없음 — 해당 API 만들 때 그때그때 추가하기로 결정.

**남은 작업 (의도적 보류)**
- 예외 로깅: 지금은 예외를 잡기만 하고 서버 로그에 남기지 않음. 코드에 `// TODO : logging 작업 필요` 표시해둠. 나중에 로깅 전략(어떤 레벨로, 어디에) 정할 때 처리.

**상태**: 완료
