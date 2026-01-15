-- Needed so we can use "=" on bigint in a GiST exclusion constraint
CREATE EXTENSION IF NOT EXISTS btree_gist;

-- Basic validity
ALTER TABLE property_availability
    ADD CONSTRAINT chk_availability_date_order
    CHECK (start_date < end_date);

-- No overlap per property:
-- daterange(start_date, end_date, '[)') => end date is exclusive
ALTER TABLE property_availability
    ADD CONSTRAINT ex_availability_no_overlap
    EXCLUDE USING gist (
        property_id WITH =,
        daterange(start_date, end_date, '[)') WITH &&
    );
