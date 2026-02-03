-- ===========================================
-- V8: Booking checkout details (1:1 with bookings)
-- PK = FK to bookings.id via @MapsId pattern
-- ===========================================

CREATE TABLE IF NOT EXISTS booking_checkout_details (
    booking_id          BIGINT PRIMARY KEY,

    traveling_for_work  BOOLEAN NOT NULL DEFAULT FALSE,

    title               VARCHAR(20),

    first_name          VARCHAR(120) NOT NULL,
    last_name           VARCHAR(120) NOT NULL,

    contact_email       VARCHAR(320) NOT NULL,

    phone_country_code  VARCHAR(10),
    phone_number        VARCHAR(40),

    special_request     VARCHAR(1000),

    CONSTRAINT fk_booking_checkout_details_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(id)
        ON DELETE CASCADE
);

-- Useful index if you ever query by contact_email (optional but harmless)
CREATE INDEX IF NOT EXISTS idx_booking_checkout_details_email
    ON booking_checkout_details(contact_email);
