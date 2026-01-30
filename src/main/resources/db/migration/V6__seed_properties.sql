-- ===========================================
-- V6: Seed 15 properties for testing
-- Uses a DO block so Flyway runs it as one statement.
-- Inserts:
--  - 1 partner user (owner)
--  - 15 properties
--  - address for each
--  - 1 photo per property + main_photo_id/main_photo_url
--  - amenities links (FREE_WIFI for all, KITCHEN for evens, BALCONY for multiples of 3)
-- ===========================================

DO $$
DECLARE
  pid BIGINT;
  photo_id BIGINT;
  photo_url TEXT;
  i INT;

  cc TEXT;
  city TEXT;
  status_val property_status_enum;
  parking_val parking_policy_enum;
  pets_val pets_policy_enum;

  amenity_free_wifi_id BIGINT;
  amenity_kitchen_id BIGINT;
  amenity_balcony_id BIGINT;
BEGIN



  -- 2) Cache amenity IDs (assumes amenities seeded already)
  SELECT id INTO amenity_free_wifi_id FROM amenities WHERE code = 'FREE_WIFI';
  SELECT id INTO amenity_kitchen_id   FROM amenities WHERE code = 'KITCHEN';
  SELECT id INTO amenity_balcony_id   FROM amenities WHERE code = 'BALCONY';

  IF amenity_free_wifi_id IS NULL THEN
    RAISE EXCEPTION 'Amenity FREE_WIFI not found. Seed amenities first.';
  END IF;

  -- 3) Insert 15 properties
  FOR i IN 1..15 LOOP

    -- Cities & countries
    IF (i % 3) = 0 THEN
      cc := 'GR'; city := 'Athens';
    ELSIF (i % 3) = 1 THEN
      cc := 'DE'; city := 'Berlin';
    ELSE
      cc := 'FR'; city := 'Paris';
    END IF;

    -- Status mix
    IF (i % 5) = 0 THEN
      status_val := 'PAUSED'::property_status_enum;
    ELSIF (i % 3) = 0 THEN
      status_val := 'DRAFT'::property_status_enum;
    ELSE
      status_val := 'PUBLISHED'::property_status_enum;
    END IF;

    -- Parking mix
    IF (i % 3) = 0 THEN
      parking_val := 'PAID'::parking_policy_enum;
    ELSIF (i % 2) = 0 THEN
      parking_val := 'FREE'::parking_policy_enum;
    ELSE
      parking_val := 'NONE'::parking_policy_enum;
    END IF;

    -- Pets mix
    IF (i % 7) = 0 THEN
      pets_val := 'UPON_REQUEST'::pets_policy_enum;
    ELSE
      pets_val := 'NO'::pets_policy_enum;
    END IF;

    -- Insert property
    INSERT INTO properties (
      owner_id,
      type,
      status,
      name,
      price_per_night,
      currency,
      max_guests,
      bathrooms,
      size_sqm,
      children_allowed,
      cots_offered,
      breakfast_served,
      parking_policy,
      smoking_allowed,
      parties_allowed,
      pets_policy,
      check_in_from,
      check_in_until,
      check_out_from,
      check_out_until,
      living_room_count,
      bedroom_count,
      bed_count,
      bed_summary,
      sleeping_areas_json,
      main_photo_url
    )
    VALUES (
      1,
      'APARTMENT'::property_type_enum,
      status_val,
      'Seed Apartment #' || i,
      (40 + (i * 7))::numeric(10,2),
      'EUR'::currency_code_enum,
      2 + (i % 4),
      1 + (i % 2),
      (35 + (i * 3))::numeric(10,2),
      TRUE,
      (i % 4 = 0),
      (i % 6 = 0),
      parking_val,
      FALSE,
      FALSE,
      pets_val,
      '13:00'::time,
      '22:00'::time,
      '07:00'::time,
      '11:00'::time,
      1,
      1 + (i % 3),
      2 + (i % 4),
      (2 + (i % 4))::text || ' beds (1 DOUBLE, 1 SINGLE_SOFA)',
      '{"bedrooms":[{"beds":{"single":1,"double":1,"king_size":0}}],"livingRoom":{"beds":{"single_sofa":1,"double_sofa":0}}}'::jsonb,
      'https://picsum.photos/seed/' || i || '/600/400'
    )
    RETURNING id INTO pid;

    -- Insert address (required)
    INSERT INTO property_addresses (
      property_id, country_code, city, postcode, street, street_number, lat, lng
    )
    VALUES (
      pid,
      cc,
      city,
      '10' || (pid % 90)::text,
      'Seed Street ' || pid,
      ((pid % 50) + 1)::text,
      NULL,
      NULL
    );

    -- Insert one photo
    photo_url := 'https://picsum.photos/seed/main-' || pid || '/800/600';

    INSERT INTO property_photos (
      property_id, url, public_id, sort_order
    )
    VALUES (
      pid,
      photo_url,
      'seed/properties/' || pid || '/main',
      0
    )
    RETURNING id INTO photo_id;

    -- Set main photo reference
    UPDATE properties
      SET main_photo_id = photo_id,
          main_photo_url = photo_url
    WHERE id = pid;

    -- Amenity links
    INSERT INTO property_amenities(property_id, amenity_id)
    VALUES (pid, amenity_free_wifi_id)
    ON CONFLICT DO NOTHING;

    IF amenity_kitchen_id IS NOT NULL AND (pid % 2 = 0) THEN
      INSERT INTO property_amenities(property_id, amenity_id)
      VALUES (pid, amenity_kitchen_id)
      ON CONFLICT DO NOTHING;
    END IF;

    IF amenity_balcony_id IS NOT NULL AND (pid % 3 = 0) THEN
      INSERT INTO property_amenities(property_id, amenity_id)
      VALUES (pid, amenity_balcony_id)
      ON CONFLICT DO NOTHING;
    END IF;

  END LOOP;
END $$;
