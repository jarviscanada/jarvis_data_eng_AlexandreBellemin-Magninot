# Introduction

# SQL Queries

###### Table Setup (DDL)
```
-- Create tables members, facilites and bookings in schema cd

CREATE SCHEMA IF NOT EXISTS cd;

CREATE TABLE IF NOT EXISTS cd.members (
  memid INTEGER NOT NULL,
  surname VARCHAR(200) NOT NULL,
  firstname VARCHAR(200) NOT NULL,
  address VARCHAR(300) NOT NULL,
  zipcode INTEGER NOT NULL,
  telephone VARCHAR(20) NOT NULL,
  recommendedby INTEGER,
  joindate TIMESTAMP NOT NULL,
  CONSTRAINT members_pk PRIMARY KEY (memid), 
  CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members(memid) ON DELETE 
  SET 
    NULL
);

CREATE TABLE IF NOT EXISTS cd.facilities(
  facid INTEGER NOT NULL, 
  name VARCHAR(200) NOT NULL, 
  membercost NUMERIC NOT NULL, 
  guestcost NUMERIC NOT NULL, 
  initialoutlay NUMERIC NOT NULL, 
  monthlymaintenance NUMERIC NOT NULL, 
  CONSTRAINT facilities_pk PRIMARY KEY (facid)
);

CREATE TABLE IF NOT EXISTS cd.bookings(
  bookid INTEGER NOT NULL,
  facid INTEGER NOT NULL,
  memid INTEGER NOT NULL,
  starttime TIMESTAMP NOT NULL,
  slots INTEGER NOT NULL,
  CONSTRAINT bookings_pk PRIMARY KEY (bookid), 
  CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid), 
  CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
);
```

##### Modifying Data
###### Question 1: Insert some data into a table
```
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
VALUES 
  (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 2: Insert calculated data into a table
```
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
VALUES 
  (
    (
      SELECT 
        MAX(facid) 
      FROM 
        cd.facilities
    )+ 1, 
    'Spa', 
    20, 
    30, 
    100000, 
    800
  );
```

###### Question 3: Update some existing data
```
UPDATE 
  cd.facilities 
SET 
  initialoutlay = 10000 
WHERE 
  name LIKE 'Tennis Court 2';
```

###### Question 4: Update a row based on the contents of another row
```
UPDATE 
  cd.facilities 
SET 
  (membercost, guestcost) = (
    SELECT 
      membercost * 1.1, 
      guestcost * 1.1 
    FROM 
      cd.facilities 
    WHERE 
      name LIKE 'Tennis Court 1'
  ) 
WHERE 
  name LIKE 'Tennis Court 2';
```

###### Question 5. Delete all bookings
```
DELETE FROM 
  cd.bookings;
```

###### Question 6. Delete a member from the cd.members table
```
DELETE FROM 
  cd.members 
WHERE 
  memid = 37;
```

##### Basics
###### Question 7. Control which rows are retrieved - part 2
```
SELECT 
  facid, 
  name, 
  membercost, 
  monthlymaintenance 
FROM 
  cd.facilities 
WHERE 
  membercost > 0 
  AND membercost < monthlymaintenance / 50;
```

###### Question 8. Basic string searches
```
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  name LIKE '%Tennis%';
```

###### Question 9. Matching against multiple possible values
```
SELECT
    *
FROM
    cd.facilities
WHERE
    facid IN (1, 5);
```

###### Question 10. Working with dates
```
SELECT 
  memid, 
  surname, 
  firstname, 
  joindate 
FROM 
  cd.members 
WHERE 
  joindate >= '2012-09-01';
```

###### Question 11. Combining results from multiple queries
```
SELECT 
  surname 
FROM 
  cd.members 
UNION 
SELECT 
  name 
FROM 
  cd.facilities;
```

##### Join
###### Question 12. Retrieve the start times of members' bookings
```
SELECT 
  starttime 
FROM 
  cd.bookings b 
  JOIN cd.members m ON m.memid = b.memid 
WHERE 
  m.surname LIKE 'Farrell' 
  AND m.firstname LIKE 'David';
```

###### Question 13. Work out the start times of bookings for tennis courts
```
SELECT
  b.starttime AS start,
  f.name
FROM
  cd.bookings b
  JOIN cd.facilities f ON f.facid = b.facid
WHERE
  DATE(b.starttime) = '2012-09-21'
  AND f.name LIKE '%Tennis Court%'
ORDER BY
  b.starttime;
```

###### Question 14. Produce a list of all members, along with their recommender
```
SELECT 
  m.firstname AS memfname, 
  m.surname AS memsname, 
  r.firstname AS recfname, 
  r.surname AS recsname 
FROM 
  cd.members m 
  LEFT JOIN cd.members r ON m.recommendedby = r.memid 
ORDER BY 
  m.surname, 
  m.firstname;
```

###### Question 15. Produce a list of all members who have recommended another member
```
SELECT 
  DISTINCT r.firstname AS firstname, 
  r.surname AS surname 
FROM 
  cd.members m 
  JOIN cd.members r ON m.recommendedby = r.memid 
ORDER BY 
  r.surname, 
  r.firstname;
```

###### Question 16. Produce a list of all members, along with their recommender, using no joins.
```
SELECT 
  DISTINCT CONCAT(m.firstname, ' ', m.surname) AS member, 
  (
    SELECT 
      CONCAT(r.firstname, ' ', r.surname) AS recommender 
    FROM 
      cd.members r 
    WHERE 
      r.memid = m.recommendedby
  ) 
FROM 
  cd.members m 
ORDER BY 
  member, 
  recommender;
```

##### Aggregation
###### Question 17. Count the number of recommendations each member makes.
```
SELECT 
  recommendedby, 
  COUNT(*) AS count 
FROM 
  cd.members 
WHERE 
  recommendedby IS NOT NULL 
GROUP BY 
  recommendedby 
ORDER BY 
  recommendedby;
```

###### Question 18. List the total slots booked per facility
```
SELECT 
  facid, 
  SUM(slots) AS "Total Slots" 
FROM 
  cd.bookings 
GROUP BY 
  facid 
ORDER BY 
  facid;
```

###### Question 19. List the total slots booked per facility in a given month
```
SELECT 
  facid, 
  SUM(slots) AS "Total Slots" 
FROM 
  cd.bookings 
WHERE 
  starttime >= '2012-09-01' 
  AND starttime < '2012-10-01' 
GROUP BY 
  facid 
ORDER BY 
  "Total Slots";
```

###### Question 20. List the total slots booked per facility per month
```
SELECT 
  facid, 
  EXTRACT(
    MONTH 
    FROM 
      starttime
  ) AS month, 
  SUM(slots) AS "Total Slots" 
FROM 
  cd.bookings 
WHERE 
  EXTRACT(
    YEAR 
    FROM 
      starttime
  )= 2012 
GROUP BY 
  facid, 
  month 
ORDER BY 
  facid, 
  month;
```

###### Question 21. Find the count of members who have made at least one booking
```
SELECT 
  COUNT(DISTINCT memid) AS count 
FROM 
  cd.bookings;
```

###### Question 22. List each member's first booking after September 1st 2012
```
SELECT 
  m.surname, 
  m.firstname, 
  b.memid, 
  MIN(b.starttime) 
FROM 
  cd.bookings b 
  JOIN cd.members m ON m.memid = b.memid 
WHERE 
  starttime >= '2012-09-01' 
GROUP BY 
  surname, 
  firstname, 
  b.memid 
ORDER BY 
  b.memid;
```

###### Question 23. Produce a list of member names, with each row containing the total member count
```
SELECT 
  COUNT (*) OVER () as COUNT, 
  firstname, 
  surname 
FROM 
  cd.members 
ORDER BY 
  joindate;
```

###### Question 24. Produce a numbered list of members
```
SELECT 
  ROW_NUMBER() OVER(
    ORDER BY 
      joindate
  ) AS row_number, 
  firstname, 
  surname 
FROM 
  cd.members 
ORDER BY 
  row_number;
```

###### Question 25. Output the facility id that has the highest number of slots booked, again
```
SELECT 
  facid, 
  total 
FROM 
  (
    SELECT 
      facid, 
      SUM(slots) AS total, 
      RANK() OVER(
        ORDER BY 
          SUM(slots) DESC
      ) as rank 
    FROM 
      cd.bookings 
    GROUP BY 
      facid
  ) 
WHERE 
  rank = 1;
```

##### String
###### Question 26. Format the names of members
```
SELECT 
  CONCAT(surname, ', ', firstname) AS name 
FROM 
  cd.members;
```

###### Question 27. Find telephone numbers with parentheses
```
SELECT 
  memid, 
  telephone 
FROM 
  cd.members 
WHERE 
  telephone ~ '[()]';
```

###### Question 28. Count the number of members whose surname starts with each letter of the alphabet
```
SELECT 
  SUBSTR(surname, 1, 1) AS letter, 
  COUNT(*) AS count 
FROM 
  cd.members 
GROUP BY 
  letter 
ORDER BY 
  letter;
```



