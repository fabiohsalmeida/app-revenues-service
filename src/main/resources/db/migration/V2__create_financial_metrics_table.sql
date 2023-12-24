CREATE TABLE financial_metrics(
  id serial PRIMARY KEY,
  date varchar(10) NOT NULL,
  app_name varchar(50) NOT NULL,
  company_id serial NOT NULL,
  total_revenue float8 NOT NULL,
  market_spending float8,
  revenue_at_time_of_marketing_spending float8,
  payback_period int,
  ltvCacRatio int,
  riskScore int,
  riskRating varchar(15),
  isEvaluationFinished bool,
  CONSTRAINT fk_company
    FOREIGN KEY(company_id)
      REFERENCES app_companies(id)
)