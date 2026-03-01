-- V2 kept for exclusion constraint only.
-- Core table/column creation now happens in V1.

CREATE EXTENSION IF NOT EXISTS btree_gist;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'ex_availability_no_overlap'
    ) THEN
        ALTER TABLE property_availability
            ADD CONSTRAINT ex_availability_no_overlap
            EXCLUDE USING gist (
                property_id WITH =,
                daterange(start_date, end_date, '[)') WITH &&
            );
    END IF;
END $$;
