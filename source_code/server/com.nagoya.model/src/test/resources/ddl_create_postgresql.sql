-- ----------------------------------------------
-- hibernate sequences / tables
-- ----------------------------------------------
DROP SEQUENCE IF EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence START 1;

DROP TABLE IF EXISTS revinfo;
DROP SEQUENCE IF EXISTS revinfo_seq;

CREATE SEQUENCE revinfo_seq;
CREATE TABLE revinfo (
  rev 			INT NOT NULL DEFAULT nextval ('revinfo_seq'),
  revtstmp 		BIGINT DEFAULT NULL,
  PRIMARY KEY (rev)
);

-- ----------------------------------------------
-- address
-- ----------------------------------------------
DROP TABLE IF EXISTS taddress;
DROP SEQUENCE IF EXISTS taddress_seq;

CREATE SEQUENCE taddress_seq;
CREATE TABLE taddress (
  id 					BIGINT NOT NULL DEFAULT nextval ('taddress_seq'),
  street 				TEXT NOT NULL,
  number 				TEXT NOT NULL,
  zip 					TEXT NOT NULL,
  city 					TEXT NOT NULL,
  region 				TEXT DEFAULT NULL,
  country 				TEXT NOT NULL,   
  creation_date 		TIMESTAMP(0) DEFAULT NULL,
  creation_user 		TEXT DEFAULT NULL,
  modification_date 	TIMESTAMP(0) DEFAULT NULL,
  modification_user 	TEXT DEFAULT NULL,
  rev 					BIGINT DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS taddress_aud;
DROP SEQUENCE IF EXISTS taddress_aud_seq;

CREATE SEQUENCE taddress_aud_seq;
CREATE TABLE taddress_aud (
  id 					BIGINT NOT NULL DEFAULT nextval ('taddress_aud_seq'),
  street 				TEXT DEFAULT NULL,
  number 				TEXT DEFAULT NULL,
  zip 					TEXT DEFAULT NULL,
  city 					TEXT DEFAULT NULL,
  region 				TEXT DEFAULT NULL,
  country 				TEXT DEFAULT NULL,
  creation_date 		TIMESTAMP(0) DEFAULT NULL,
  creation_user 		TEXT DEFAULT NULL,
  modification_date 	TIMESTAMP(0) DEFAULT NULL,
  modification_user 	TEXT DEFAULT NULL,
  rev 					BIGINT DEFAULT NULL,
  revtype 				SMALLINT DEFAULT NULL,
  PRIMARY KEY (id, rev)
);

-- ----------------------------------------------
-- person
-- ----------------------------------------------
DROP TABLE IF EXISTS tperson;
DROP SEQUENCE IF EXISTS tperson_seq;

CREATE SEQUENCE tperson_seq;
CREATE TABLE tperson (
  id 					BIGINT NOT NULL DEFAULT nextval ('tperson_seq'),
  email 				TEXT NOT NULL,
  email_confirmed		BOOLEAN NOT NULL DEFAULT FALSE,
  password 				TEXT NOT NULL, 
  person_type 			TEXT NOT NULL,
  address_id 			BIGINT DEFAULT NULL,
  creation_date 		TIMESTAMP(0) DEFAULT NULL,
  creation_user		 	TEXT DEFAULT NULL,
  modification_date 	TIMESTAMP(0) DEFAULT NULL,
  modification_user 	TEXT DEFAULT NULL,
  rev 					BIGINT DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS tperson_aud;
DROP SEQUENCE IF EXISTS tperson_aud_seq;

CREATE SEQUENCE tperson_aud_seq;
CREATE TABLE tperson_aud (
  id 					BIGINT NOT NULL DEFAULT nextval ('tperson_aud_seq'),
  email 				TEXT DEFAULT NULL,
  email_confirmed		BOOLEAN DEFAULT NULL,
  password 				TEXT DEFAULT NULL, 
  person_type 			TEXT DEFAULT NULL,
  address_id 			BIGINT DEFAULT NULL,
  creation_date 		TIMESTAMP(0) DEFAULT NULL,
  creation_user 		TEXT DEFAULT NULL,
  modification_date 	TIMESTAMP(0) DEFAULT NULL,
  modification_user 	TEXT DEFAULT NULL,
  rev 					BIGINT DEFAULT NULL,
  revtype 				SMALLINT DEFAULT NULL,
  PRIMARY KEY (id, rev)
);

-- ----------------------------------------------
-- person_natural
-- ----------------------------------------------
DROP TABLE IF EXISTS tperson_natural;
DROP SEQUENCE IF EXISTS tperson_natural_seq;

CREATE SEQUENCE tperson_natural_seq;
CREATE TABLE tperson_natural (
  id 			BIGINT NOT NULL DEFAULT nextval ('tperson_natural_seq'),
  person_id 	BIGINT NOT NULL,
  first_name 	TEXT NOT NULL,
  last_name 	TEXT NOT NULL,
  birthdate 	TIMESTAMP(0) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS tperson_natural_aud;
DROP SEQUENCE IF EXISTS tperson_natural_aud_seq;

CREATE SEQUENCE tperson_natural_aud_seq;
CREATE TABLE tperson_natural_aud (
  id 			BIGINT NOT NULL DEFAULT nextval ('tperson_natural_aud_seq'),
  person_id 	BIGINT DEFAULT NULL,
  first_name 	TEXT DEFAULT NULL,
  last_name 	TEXT DEFAULT NULL,
  birthdate 	TIMESTAMP(0) DEFAULT NULL,
  rev 			BIGINT DEFAULT NULL,
  revtype 		SMALLINT DEFAULT NULL,
  PRIMARY KEY (id, rev)
);

-- ----------------------------------------------
-- person_legal
-- ----------------------------------------------
DROP TABLE IF EXISTS tperson_legal;
DROP SEQUENCE IF EXISTS tperson_legal_seq;

CREATE SEQUENCE tperson_legal_seq;
CREATE TABLE tperson_legal (
  id 							BIGINT NOT NULL DEFAULT nextval ('tperson_legal_seq'),
  person_id 					BIGINT NOT NULL,
  "name" 						TEXT NOT NULL,
  commercial_register_number 	TEXT DEFAULT NULL,
  tax_number 					TEXT DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS tperson_legal_aud;
DROP SEQUENCE IF EXISTS tperson_legal_aud_seq;

CREATE SEQUENCE tperson_legal_aud_seq;
CREATE TABLE tperson_legal_aud (
  id 							BIGINT NOT NULL DEFAULT nextval ('tperson_legal_aud_seq'),
  person_id 					BIGINT DEFAULT NULL,
  "name" 						TEXT DEFAULT NULL,
  commercial_register_number 	TEXT DEFAULT NULL,
  tax_number 					TEXT DEFAULT NULL,
  rev 							BIGINT DEFAULT NULL,
  revtype 						SMALLINT DEFAULT NULL,
  PRIMARY KEY (id, rev)
);

-- ----------------------------------------------
-- online_user
-- ----------------------------------------------
DROP TABLE IF EXISTS tonline_user;
DROP SEQUENCE IF EXISTS tonline_user_seq;

CREATE SEQUENCE tonline_user_seq;
CREATE TABLE tonline_user (
  id 					BIGINT NOT NULL DEFAULT nextval ('tonline_user_seq'),
  person_id				BIGINT NOT NULL,
  session_token			TEXT NOT NULL,
  private_key 			TEXT NOT NULL,
  expiration_date		TIMESTAMP(0) NOT NULL,
  creation_date 		TIMESTAMP(0) DEFAULT NULL,
  creation_user 		TEXT DEFAULT NULL,
  modification_date 	TIMESTAMP(0) DEFAULT NULL,
  modification_user 	TEXT DEFAULT NULL,
  rev 					BIGINT DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------------------------
-- user_request
-- ----------------------------------------------
DROP TABLE IF EXISTS tuser_request;
DROP SEQUENCE IF EXISTS tuser_request_seq;

CREATE SEQUENCE tuser_request_seq;
CREATE TABLE tuser_request (
  id 					BIGINT NOT NULL DEFAULT nextval ('tuser_request_seq'),
  person_id				BIGINT NOT NULL,
  request_type 			TEXT NOT NULL,
  token					TEXT NOT NULL,
  expiration_date		TIMESTAMP(0) NOT NULL,
  creation_date 		TIMESTAMP(0) DEFAULT NULL,
  creation_user 		TEXT DEFAULT NULL,
  modification_date 	TIMESTAMP(0) DEFAULT NULL,
  modification_user 	TEXT DEFAULT NULL,
  rev 					BIGINT DEFAULT NULL,
  PRIMARY KEY (id)
);