#! /bin/bash

# CLI arguments check
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check # of args
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# Retrieve hardware specification variables
memory_free=$(echo "$vmstat_mb" | tail -1 | awk -v col="4" '{print $col}')
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk '{print $15}')
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk '{print $14}')
disk_io=$(vmstat --unit M -d | tail -1 | awk -v col="10" '{print $col}')
disk_available=$(df -BM | tail -3 | head -1 | awk '{gsub(/[A-Za-z]/,"",$4); print $4}')

# Current time in `YYYY-MM-DD hh:mm:ss` UTC format
timestamp=$(vmstat -t | tail -1 | awk '{print $18, $19}')

# Subquery to find matching id in host_info table
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

# PSQL command: Inserts server usage data into host_usage table
insert_stmt="INSERT INTO host_usage (
    \"timestamp\", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available
) VALUES (
    '$timestamp',
    $host_id,
    $memory_free,
    $cpu_idle,
    $cpu_kernel,
    $disk_io,
    $disk_available
);"

# Set up env var for pql cmd
export PGPASSWORD=$psql_password 
# Insert data into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
