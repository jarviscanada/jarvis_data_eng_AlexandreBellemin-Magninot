#! /bin/bash

# Assign CLI arguments to variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check args
if [[ $# -ne 5 ]]; then
	echo 'Illegal number of parameters'
	echo 'Please refer your hostname, port, DB name, psql user and password'
	exit 1
fi

# Parse host hardware specifications
hostname=$(hostname -f)
lscpu_out=$(lscpu)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model name:" | awk '{print substr($0, 13)}' | xargs)
cpu_mhz=$(echo "$cpu_model" | grep -oE '[0-9.]+GHz' | sed 's/GHz//' | awk '{print $1 * 1000}') # in MHz
# We want to check if the value is in KiB or MiB, and changes the value in function. We do it in the awk function.
l2_cache=$(echo "$lscpu_out"  | egrep "^L2 cache:" | awk '{val=$3; unit=$4; if(unit~/MiB/) val*=1024;print val}' | xargs)
total_mem=$(grep "MemTotal" /proc/meminfo | awk '{print $2}' | xargs)
timestamp=$(date "+%Y-%m-%d %T")

# INSERT statement
insert_stmt="INSERT INTO host_info (
    hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, \"timestamp\", total_mem
) VALUES (
    '$hostname',
    $cpu_number,
    '$cpu_architecture',
    '$cpu_model',
    $cpu_mhz,
    $l2_cache,
    '$timestamp',
    $total_mem
);"

# Execute the INSERT statement through the psql CLI tool
PGPASSWORD="$psql_password" psql -h "$psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"
exit $?
