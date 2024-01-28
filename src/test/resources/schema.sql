CREATE SCHEMA if not exists prodsch;

drop table if exists account_pool;
drop table if exists tpp_product_register;
drop table if exists agreement;
drop table if exists tpp_product;
drop table if exists tpp_ref_product_register_type;
DROP table if exists tpp_ref_product_class;
DROP table if exists tpp_ref_account_type;


CREATE TABLE tpp_ref_account_type(
	internal_id	     SERIAL PRIMARY KEY,
	xvalue		     VARCHAR(50) UNIQUE
);

CREATE TABLE tpp_ref_product_class(
	internal_id	     SERIAL PRIMARY KEY,
	xvalue		     VARCHAR(30) UNIQUE,
	gbl_code	     VARCHAR(2),
	gbl_name	     VARCHAR(50),
	product_row_code VARCHAR(3),
	product_row_name VARCHAR(50),
	subclass_code	 VARCHAR(3),
	subclass_name	 VARCHAR(50)
);

CREATE TABLE tpp_ref_product_register_type(
  internal_id        SERIAL PRIMARY KEY,
  xvalue             VARCHAR(30) UNIQUE,
  register_type_name VARCHAR(254),
  product_class_code VARCHAR(30) REFERENCES tpp_ref_product_class(xvalue),
  account_type       VARCHAR(30) REFERENCES tpp_ref_account_type(xvalue)
);

CREATE TABLE tpp_product(
  id                 SERIAL PRIMARY KEY, 
  product_code_id    INTEGER REFERENCES tpp_ref_product_class(internal_id),
  client_id          VARCHAR(10),
  xtype              VARCHAR(30),
  xnumber            VARCHAR(30) UNIQUE,
  xpriority          VARCHAR(2),
  date_of_conclusion DATE,
  start_date_time    TIMESTAMP,
  end_date_time      TIMESTAMP,
  xdays               INTEGER,
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
  id		         SERIAL PRIMARY KEY,
  product_id	     INTEGER REFERENCES tpp_product(id),
  xtype 		     VARCHAR(10),
  xnumber	         VARCHAR(30),
  start_date_time    TIMESTAMP,							
  end_date_time	     TIMESTAMP,							
  xdays               INTEGER,
  reason_close       VARCHAR(254),  
  state              VARCHAR(30),
  UNIQUE(product_id, xnumber)
);

CREATE TABLE tpp_product_register(
  id                 SERIAL PRIMARY KEY,
  product_id         INTEGER REFERENCES tpp_product(id),
  xtype              VARCHAR(30) REFERENCES tpp_ref_product_register_type(xvalue),
  account_id         INTEGER,
  current_code       VARCHAR(3),
  state              VARCHAR(30),
  account_number     VARCHAR(30)
);

CREATE TABLE account_pool(
  id 		         SERIAL PRIMARY KEY,
  branch_code	     VARCHAR(30),
  currency_code      VARCHAR(3),
  mdm_code	         VARCHAR(30),
  xpriority          VARCHAR(2),
  registry_type_code VARCHAR(30),
  accounts	         VARCHAR(254)
);

