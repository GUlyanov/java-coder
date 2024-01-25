CREATE SCHEMA if not exists prodsch;

drop table if exists account_pool;
drop table if exists tpp_product_register;
drop table if exists agreement;
drop table if exists tpp_product;
drop table if exists tpp_ref_product_register_type;
DROP table if exists tpp_ref_product_class;
DROP table if exists tpp_ref_account_type;


CREATE TABLE tpp_ref_account_type(
	internal_id	 SERIAL PRIMARY KEY,
	value		 VARCHAR(50) UNIQUE
);

CREATE TABLE tpp_ref_product_class(
	internal_id	 SERIAL PRIMARY KEY,
	value		 VARCHAR(30) UNIQUE,
	gbl_code	 VARCHAR(2),
	gbl_name	 VARCHAR(50),
	product_row_code VARCHAR(3),
	product_row_name VARCHAR(50),
	subclass_code	 VARCHAR(3),
	subclass_name	 VARCHAR(50)
);

CREATE TABLE tpp_ref_product_register_type(
  internal_id        SERIAL PRIMARY KEY,
  value              VARCHAR(30) UNIQUE,
  register_type_name VARCHAR(254),
  product_class_code VARCHAR(30) REFERENCES tpp_ref_product_class(value),
  account_type       VARCHAR(30) REFERENCES tpp_ref_account_type(value)
);

CREATE TABLE tpp_product(
  id                 SERIAL PRIMARY KEY, 
  product_code_id    INTEGER REFERENCES tpp_ref_product_class(internal_id),
  client_id          VARCHAR(10),
  type               VARCHAR(30),
  number             VARCHAR(30) UNIQUE,	
  priority           VARCHAR(2),
  date_of_conclusion DATE,
  start_date_time    TIMESTAMP,
  end_date_time      TIMESTAMP,
  days               INTEGER,
  penalty_rate       NUMERIC,
  nso                NUMERIC,
  threshold_amount   NUMERIC,
  requisite_type     VARCHAR(30),
  interest_rate_type VARCHAR(30),
  tax_rate           NUMERIC,
  reason_close       VARCHAR(254),
  state              VARCHAR(20)	
);

CREATE TABLE agreement(
  id		     SERIAL PRIMARY KEY,
  product_id	     INTEGER REFERENCES tpp_product(id),
  type 		     VARCHAR(10),
  number	     VARCHAR(30),
  start_date_time    TIMESTAMP,							
  end_date_time	     TIMESTAMP,							
  days               INTEGER,
  reason_close       VARCHAR(254),  
  state              VARCHAR(30),
  UNIQUE(product_id, number)	
);

CREATE TABLE tpp_product_register(
  id                 SERIAL PRIMARY KEY,
  product_id         INTEGER REFERENCES tpp_product(id),
  type               VARCHAR(30) REFERENCES tpp_ref_product_register_type(value),
  account_id         INTEGER,
  current_code       VARCHAR(3),
  state              VARCHAR(30),
  account_number     VARCHAR(30)
);

CREATE TABLE account_pool(
  id 		     SERIAL PRIMARY KEY,
  branch_code	     VARCHAR(30),
  currency_code      VARCHAR(3),
  mdm_code	     VARCHAR(30),
  priority           VARCHAR(2),
  registry_type_code VARCHAR(30),
  accounts	     VARCHAR(254)
);

