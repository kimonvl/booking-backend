DO $$ BEGIN
  CREATE TYPE payment_status_enum AS ENUM ('REQUIRES_PAYMENT', 'PROCESSING', 'SUCCEEDED', 'FAILED');
EXCEPTION WHEN duplicate_object THEN NULL; END $$;

ALTER TABLE bookings
  ADD COLUMN IF NOT EXISTS payment_intent_id VARCHAR(120),
  ADD COLUMN IF NOT EXISTS payment_status payment_status_enum NOT NULL DEFAULT 'REQUIRES_PAYMENT',
  ADD COLUMN IF NOT EXISTS amount_total NUMERIC(10,2),
  ADD COLUMN IF NOT EXISTS paid_at TIMESTAMPTZ;

CREATE UNIQUE INDEX IF NOT EXISTS uq_bookings_payment_intent
  ON bookings(payment_intent_id)
  WHERE payment_intent_id IS NOT NULL;