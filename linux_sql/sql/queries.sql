-- Those queries are some exercices for SQL. It's not part of the project itself

-- 1. Group hosts by hardware info
SELECT 
  cpu_number, 
  id AS host_id, 
  total_mem
FROM host_info
ORDER BY 
  cpu_number, 
  total_mem DESC;

-- 2. Average memory usage
-- The function is in the Notion
CREATE FUNCTION round5min(ts timestamp) RETURNS timestamp AS $$ BEGIN RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$ LANGUAGE PLPGSQL;
SELECT 
  hu.host_id, 
  hi.hostname, 
  round5min(hu.timestamp) AS timestamp, 
  AVG(
    (
      (hi.total_mem / 1024.0) - hu.memory_free
    ) * 100.0 / (hi.total_mem / 1024.0)
  ) AS avg_used_mem_percentage 
FROM 
  host_usage hu 
  JOIN host_info hi ON hi.id = hu.host_id 
GROUP BY 
  hu.host_id, 
  hi.hostname, 
  round5min(timestamp) 
ORDER BY 
  hu.host_id, 
  round5min(timestamp);

-- 3. Detect host failure
-- The function is in the Notion
CREATE FUNCTION round5min(ts timestamp) RETURNS timestamp AS $$ BEGIN RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$ LANGUAGE PLPGSQL;
SELECT
  host_id,
  round5min(timestamp) AS timestamp,
  COUNT(*) AS num_data_points
FROM 
  host_usage
GROUP BY
  host_id,
  round5min(timestamp)
HAVING 
  COUNT(*) < 3;
  
