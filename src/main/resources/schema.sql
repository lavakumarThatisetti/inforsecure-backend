CREATE TABLE IF NOT EXISTS users
(
  id uuid PRIMARY KEY,
  created_at timestamp NOT NULL,
  updated_at timestamp NOT NULL,
  version INTEGER NOT NULL,
  first_name VARCHAR(40) NULL,
  last_name VARCHAR(40)  NULL ,
  user_name VARCHAR(40) NOT NULL UNIQUE,
  email VARCHAR(60) NOT NULL UNIQUE,
  phone_no VARCHAR(12) UNIQUE,
  wealth_score INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS consent_requests
(
  id uuid PRIMARY KEY,
  created_at timestamp NOT NULL,
  updated_at timestamp NOT NULL,
  version INTEGER NOT NULL,
  user_id  VARCHAR(50) NOT NULL,
  data TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS consents_response
(
  id uuid PRIMARY KEY,
  created_at timestamp NOT NULL,
  updated_at timestamp NOT NULL,
  version INTEGER NOT NULL,
  user_id  VARCHAR(50) NOT NULL,
  consent_id VARCHAR(100) NULL ,
  consent_hash TEXT NOT NULL UNIQUE,
  expiry_date timestamp NOT NULL,
  is_expired boolean DEFAULT false
);

CREATE TABLE IF NOT EXISTS fi_data_responses
(
  id uuid PRIMARY KEY,
  created_at timestamp NOT NULL,
  updated_at timestamp NOT NULL,
  version INTEGER NOT NULL,
  user_id  VARCHAR(50) NOT NULL,
  consent_id VARCHAR(100) NULL ,
  from_date timestamp NOT NULL,
  to_date timestamp NOT NULL,
  fi_hash TEXT NOT NULL UNIQUE,
  data TEXT NOT NULL,
  wealth_score INTEGER DEFAULT 0
);
