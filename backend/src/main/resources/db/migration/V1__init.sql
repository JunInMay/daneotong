-- 사용자 테이블
CREATE TABLE users (
    id               uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email            varchar(255) NOT NULL UNIQUE,
    password_hash    varchar(255) NOT NULL,
    native_language  varchar(10)  NOT NULL,
    dict_preference  varchar(10)  NOT NULL,
    created_at       timestamp    DEFAULT now(),
    updated_at       timestamp    DEFAULT now()
);

-- 덱 테이블
CREATE TABLE decks (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     uuid         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name        varchar(255) NOT NULL,
    description text,
    created_at  timestamp    DEFAULT now(),
    updated_at  timestamp    DEFAULT now()
);

-- 카드 테이블
CREATE TABLE cards (
    id                uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id           uuid         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    word              varchar(255) NOT NULL,
    part_of_speech    varchar(50),
    phonetic          varchar(100),
    definition_en     text,
    definition_native text,
    created_at        timestamp    DEFAULT now(),
    updated_at        timestamp    DEFAULT now()
);

-- 카드 예문 테이블
CREATE TABLE card_examples (
    id               uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    card_id          uuid NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    example_sentence text NOT NULL,
    created_at       timestamp DEFAULT now()
);

-- 덱-카드 연결 테이블
CREATE TABLE deck_cards (
    id       uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    deck_id  uuid      NOT NULL REFERENCES decks(id) ON DELETE CASCADE,
    card_id  uuid      NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    added_at timestamp DEFAULT now(),
    UNIQUE (deck_id, card_id)
);

-- 복습 로그 테이블
CREATE TABLE review_logs (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    card_id     uuid        NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    user_id     uuid        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    result      varchar(10) NOT NULL,
    reviewed_at timestamp   DEFAULT now()
);
