CREATE TABLE financial_months(
    id serial PRIMARY KEY,
    year_month varchar(7) NOT NULL,
    is_processed bool DEFAULT false
)