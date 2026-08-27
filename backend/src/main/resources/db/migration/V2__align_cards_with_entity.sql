-- 인증(User) 기능이 아직 없어 user_id 외래키 제약과 NOT NULL을 임시로 제거
-- TODO: User 엔티티/인증 기능 생기면 FK, NOT NULL 다시 추가 검토
ALTER TABLE cards DROP CONSTRAINT cards_user_id_fkey;
ALTER TABLE cards ALTER COLUMN user_id DROP NOT NULL;

-- Cards 엔티티(expression, meaning)에 맞춰 컬럼 정리
-- word -> expression 이름 변경, meaning 컬럼 추가
-- 아직 안 쓰는 필드(part_of_speech, phonetic, definition_en, definition_native)는 제거 (필요해지면 나중에 다시 추가)
ALTER TABLE cards RENAME COLUMN word TO expression;
ALTER TABLE cards ADD COLUMN meaning text;
ALTER TABLE cards DROP COLUMN part_of_speech;
ALTER TABLE cards DROP COLUMN phonetic;
ALTER TABLE cards DROP COLUMN definition_en;
ALTER TABLE cards DROP COLUMN definition_native;
