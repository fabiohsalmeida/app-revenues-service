CREATE TABLE financial_metrics(
  id serial PRIMARY KEY,
  year_month varchar(7) NOT NULL,
  app_name varchar(50) NOT NULL,
  company_id serial NOT NULL,
  total_revenue float8 NOT NULL,
  marketing_spend float8,
  revenue_at_time_of_marketing_spend float8,
  marketing_spend_day int,
  payback_period int,
  ltv_cac_ratio float8,
  risk_score float8,
  risk_rating varchar(15),
  is_evaluation_finished bool,
  is_already_exported bool,
  CONSTRAINT fk_company_financial_metrics
    FOREIGN KEY(company_id)
      REFERENCES app_companies(id)
);

CREATE TABLE financial_metrics_history(
  id serial PRIMARY KEY,
  date varchar(10) NOT NULL,
  year_month varchar(7) NOT NULL,
  app_name varchar(50) NOT NULL,
  company_id serial NOT NULL,
  revenue float8 NOT NULL,
  marketing_spend float8 NOT NULL,
  CONSTRAINT fk_company_metrics_history
    FOREIGN KEY(company_id)
      REFERENCES app_companies(id)
);