INSERT INTO amenities (code, label, group_name)
VALUES
  -- General
  ('AIR_CONDITIONING', 'Air conditioning', 'GENERAL'),
  ('HEATING', 'Heating', 'GENERAL'),
  ('FREE_WIFI', 'Free WiFi', 'GENERAL'),
  ('EV_CHARGING', 'Electric vehicle charging station', 'GENERAL'),

  -- Cooking and cleaning
  ('KITCHEN', 'Kitchen', 'COOKING_AND_CLEANING'),
  ('KITCHENETTE', 'Kitchenette', 'COOKING_AND_CLEANING'),
  ('WASHING_MACHINE', 'Washing machine', 'COOKING_AND_CLEANING'),

  -- Entertainment
  ('FLAT_SCREEN_TV', 'Flat-screen TV', 'ENTERTAINMENT'),
  ('SWIMMING_POOL', 'Swimming pool', 'ENTERTAINMENT'),
  ('HOT_TUB', 'Hot tub', 'ENTERTAINMENT'),
  ('MINIBAR', 'Minibar', 'ENTERTAINMENT'),
  ('SAUNA', 'Sauna', 'ENTERTAINMENT'),

  -- Outside and view
  ('BALCONY', 'Balcony', 'OUTSIDE_AND_VIEW'),
  ('GARDEN_VIEW', 'Garden view', 'OUTSIDE_AND_VIEW'),
  ('TERRACE', 'Terrace', 'OUTSIDE_AND_VIEW'),
  ('VIEW', 'View', 'OUTSIDE_AND_VIEW')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    group_name = EXCLUDED.group_name;
