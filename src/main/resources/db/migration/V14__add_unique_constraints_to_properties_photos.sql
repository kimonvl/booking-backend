-- Add explicit unique constraints for properties_photos to match JPA mapping.

DO $$ BEGIN
    ALTER TABLE properties_photos
        ADD CONSTRAINT uq_properties_photos_pair UNIQUE (property_id, photo_id);
EXCEPTION WHEN duplicate_object THEN NULL; END $$;

DO $$ BEGIN
    ALTER TABLE properties_photos
        ADD CONSTRAINT uq_properties_photos_photo UNIQUE (photo_id);
EXCEPTION WHEN duplicate_object THEN NULL; END $$;
