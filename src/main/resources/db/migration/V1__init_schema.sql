-- =========
-- USERS
-- =========
CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(320) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    role          VARCHAR(32) NOT NULL,
    enabled       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- =========
-- PROPERTIES (minimal)
-- =========
CREATE TABLE IF NOT EXISTS properties (
    id          BIGSERIAL PRIMARY KEY,
    owner_id    BIGINT NOT NULL,
    title       VARCHAR(255) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_properties_owner
        FOREIGN KEY (owner_id) REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_properties_owner_id ON properties(owner_id);

-- =========
-- PROPERTY AVAILABILITY
-- =========
CREATE TABLE IF NOT EXISTS property_availability (
    id           BIGSERIAL PRIMARY KEY,
    property_id  BIGINT NOT NULL,
    start_date   DATE NOT NULL,
    end_date     DATE NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_availability_property
        FOREIGN KEY (property_id) REFERENCES properties(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_availability_property_id ON property_availability(property_id);

-- =========
-- LANGUAGES
-- =========
CREATE TABLE IF NOT EXISTS languages (
    id     BIGSERIAL PRIMARY KEY,
    code   VARCHAR(16) NOT NULL UNIQUE,
    label  VARCHAR(120) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_languages_code ON languages(code);

-- =========
-- PROPERTY LANGUAGES (join)
-- =========
CREATE TABLE IF NOT EXISTS property_languages (
    property_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    kind        VARCHAR(32) NOT NULL,

    PRIMARY KEY (property_id, language_id),

    CONSTRAINT fk_property_languages_property
        FOREIGN KEY (property_id) REFERENCES properties(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_property_languages_language
        FOREIGN KEY (language_id) REFERENCES languages(id)
        ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_property_languages_property ON property_languages(property_id);
CREATE INDEX IF NOT EXISTS idx_property_languages_language ON property_languages(language_id);
