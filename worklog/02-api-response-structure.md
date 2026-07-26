# 2. 백엔드 API 응답 구조 짜기

> 원본: Obsidian `작업/3. 백엔드 API 응답 구조 짜기.md`

- Lombok 도입 (`@Getter`, `@AllArgsConstructor` 등).
- `ApiResponse<T>` 공통 응답 클래스 작성: `code`, `message`, `data` 필드.
  - `ResponseCode`를 통째로 필드에 넣지 않고 `code`/`message`를 풀어서 담는 이유: `ResponseCode`가 객체라 그대로 넣으면 응답 JSON이 중첩되어버림.
- `ResponseCode` enum으로 응답 코드/메시지를 상수 관리 (`SUCCESS` 등).
- 정적 팩토리 메서드 패턴 적용: `ApiResponse.of(ResponseCode, data)`, `ApiResponse.of(ResponseCode, message, data)`.

**상태**: 완료
