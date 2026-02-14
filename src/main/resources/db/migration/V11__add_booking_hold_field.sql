ALTER TABLE bookings
    ADD COLUMN IF NOT EXISTS hold_expires_at TIMESTAMPTZ;

-- helpful index for cleanup job
CREATE INDEX IF NOT EXISTS idx_bookings_status_hold_expires
    ON bookings(status, hold_expires_at);

-- availability index (if not already)
CREATE INDEX IF NOT EXISTS idx_availability_booking
    ON property_availability(booking_id);