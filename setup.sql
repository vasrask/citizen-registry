CREATE TABLE IF NOT EXISTS citizens (
    registry_number VARCHAR(9) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    date_of_birth VARCHAR(32),
    place_of_birth VARCHAR(255)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;
