-- Add UUID business identifiers for selected entities.
-- Backfills existing rows and enforces NOT NULL + uniqueness.
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- users
ALTER TABLE users ADD COLUMN IF NOT EXISTS uuid UUID;
UPDATE users SET uuid = gen_random_uuid() WHERE uuid IS NULL;
ALTER TABLE users ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_uuid ON users(uuid);

-- refresh_tokens
ALTER TABLE refresh_tokens ADD COLUMN IF NOT EXISTS uuid UUID;
UPDATE refresh_tokens SET uuid = gen_random_uuid() WHERE uuid IS NULL;
ALTER TABLE refresh_tokens ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_refresh_tokens_uuid ON refresh_tokens(uuid);

-- properties
ALTER TABLE properties ADD COLUMN IF NOT EXISTS uuid UUID;
UPDATE properties SET uuid = gen_random_uuid() WHERE uuid IS NULL;
ALTER TABLE properties ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_properties_uuid ON properties(uuid);

-- property_addresses
ALTER TABLE property_addresses ADD COLUMN IF NOT EXISTS uuid UUID;
UPDATE property_addresses SET uuid = gen_random_uuid() WHERE uuid IS NULL;
ALTER TABLE property_addresses ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_property_addresses_uuid ON property_addresses(uuid);

-- property_photos
ALTER TABLE property_photos ADD COLUMN IF NOT EXISTS uuid UUID;
UPDATE property_photos SET uuid = gen_random_uuid() WHERE uuid IS NULL;
ALTER TABLE property_photos ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_property_photos_uuid ON property_photos(uuid);

-- bookings
ALTER TABLE bookings ADD COLUMN IF NOT EXISTS uuid UUID;
UPDATE bookings SET uuid = gen_random_uuid() WHERE uuid IS NULL;
ALTER TABLE bookings ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_bookings_uuid ON bookings(uuid);

-- booking_checkout_details
ALTER TABLE booking_checkout_details ADD COLUMN IF NOT EXISTS uuid UUID;
UPDATE booking_checkout_details SET uuid = gen_random_uuid() WHERE uuid IS NULL;
ALTER TABLE booking_checkout_details ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_booking_checkout_details_uuid ON booking_checkout_details(uuid);

-- property_availability
ALTER TABLE property_availability ADD COLUMN IF NOT EXISTS uuid UUID;
UPDATE property_availability SET uuid = gen_random_uuid() WHERE uuid IS NULL;
ALTER TABLE property_availability ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_property_availability_uuid ON property_availability(uuid);

-- reviews
ALTER TABLE reviews ADD COLUMN IF NOT EXISTS uuid UUID;
UPDATE reviews SET uuid = gen_random_uuid() WHERE uuid IS NULL;
ALTER TABLE reviews ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_reviews_uuid ON reviews(uuid);
