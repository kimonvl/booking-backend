-- Replace property_languages link-entity table with a pure ManyToMany join table.
-- Keeps existing active links and drops soft-delete/audit columns model.

CREATE TABLE IF NOT EXISTS properties_languages (
    property_id BIGINT NOT NULL,
    language_id BIGINT NOT NULL,
    PRIMARY KEY (property_id, language_id),
    CONSTRAINT fk_properties_languages_property
        FOREIGN KEY (property_id) REFERENCES properties(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_properties_languages_language
        FOREIGN KEY (language_id) REFERENCES languages(id)
            ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_properties_languages_property
    ON properties_languages(property_id);

CREATE INDEX IF NOT EXISTS idx_properties_languages_language
    ON properties_languages(language_id);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public'
          AND table_name = 'property_languages'
    ) THEN
        INSERT INTO properties_languages (property_id, language_id)
        SELECT pl.property_id, pl.language_id
        FROM property_languages pl
        WHERE COALESCE(pl.deleted, FALSE) = FALSE
        ON CONFLICT (property_id, language_id) DO NOTHING;

        DROP TABLE property_languages;
    END IF;
END $$;
