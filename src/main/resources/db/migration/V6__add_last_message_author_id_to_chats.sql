ALTER TABLE chats
    ADD COLUMN IF NOT EXISTS last_message_author_id BIGINT;

ALTER TABLE chats
    ADD CONSTRAINT fk_chats_last_message_author
        FOREIGN KEY (last_message_author_id) REFERENCES users(id)
            ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_chats_last_message_author_id
    ON chats(last_message_author_id);
