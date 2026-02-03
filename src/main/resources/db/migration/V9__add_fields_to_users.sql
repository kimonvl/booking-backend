-- ===========================================
-- V9: Add basic profile fields to users
-- ===========================================

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS first_name VARCHAR(120),
    ADD COLUMN IF NOT EXISTS last_name  VARCHAR(120),
    ADD COLUMN IF NOT EXISTS country_code VARCHAR(2);

-- FK to countries (optional but recommended)
DO $$ BEGIN
    ALTER TABLE users
        ADD CONSTRAINT fk_users_country
        FOREIGN KEY (country_code)
        REFERENCES countries(code)
        ON DELETE SET NULL;
EXCEPTION WHEN duplicate_object THEN NULL; END $$;

CREATE INDEX IF NOT EXISTS idx_users_country_code ON users(country_code);
CREATE INDEX IF NOT EXISTS idx_users_last_name ON users(last_name);
