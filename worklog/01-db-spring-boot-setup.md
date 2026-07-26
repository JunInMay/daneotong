# 1. DB 최초 설정 및 Spring Boot 세팅

> 원본: Obsidian `작업/2. DB 최초 설정 및 Spring boot 세팅.md`

- PostgreSQL 로컬 설치(`brew install postgresql@16`) 및 `daneotong` 데이터베이스 생성.
- Spring Boot 프로젝트 세팅, `application.yaml`에 DB 연결 설정.
- Flyway로 스키마 버전 관리 시작 (`V1__init.sql` — 최초 스키마, 초안 단계).

**상태**: 완료 (로컬 개발용 최소 세팅. DB는 추후 클라우드로 이전 예정, 스키마도 계속 바뀔 수 있음)
