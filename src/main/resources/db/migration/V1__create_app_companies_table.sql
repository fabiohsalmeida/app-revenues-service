CREATE TABLE app_companies (
   id serial PRIMARY KEY,
   name varchar (50) UNIQUE NOT NULL,
   country_code varchar (2) NOT NULL
)