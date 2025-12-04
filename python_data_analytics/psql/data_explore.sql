-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT
  *
FROM
  retail
limit
  10;

-- Check # of records
SELECT
  COUNT(*) AS total_records
FROM
  retail;

-- Number of clients (e.g. unique client ID)
SELECT
  COUNT(DISTINCT customer_id) AS number_customers
FROM
  retail;

-- Invoice Date Range (e.g. max/min dates)
SELECT
  MAX(invoice_date),
  MIN(invoice_date)
FROM
  retail;

-- Number of SKU/merchants (e.g. unique stock code)
SELECT
  COUNT(DISTINCT stock_code)
FROM
  retail;

-- Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
SELECT
  AVG(amount)
FROM
  (
    SELECT
      invoice_no,
      SUM(unit_price * quantity) AS amount
    FROM
      retail
    GROUP BY
      invoice_no
  ) AS r
WHERE
  amount > 0;

-- Calculate total revenue (e.g. sum of unit_price * quantity)
-- Results is different than expected because here we delete values with negative unit_price
SELECT
  SUM(unit_price * quantity)
FROM
  retail
WHERE
  unit_price > 0;

-- Calculate total revenue by YYYYMM
SELECT
  (
    CAST(
      EXTRACT(
        YEAR
        FROM
          invoice_date
      ) AS INTEGER
    ) * 100 + CAST(
      EXTRACT(
        MONTH
        FROM
          invoice_date
      ) AS INTEGER
    )
  ) AS YYYYMM,
  SUM(unit_price * quantity)
FROM
  retail
GROUP BY
  YYYYMM
ORDER BY
  YYYYMM;