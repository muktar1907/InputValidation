#!/bin/bash
#define host and ports
host=mysql
port=3306
timeout=120
echo "waiting for mysql to be ready"
exit_code=1
for i in $(seq 1 $timeout); do
    #check if mysql is ready to accept connections
    nc -z $host $port #check if connection can be established
    if [ $? -eq 0 ];then #if the previous command returns exit code 0
        echo "MySQL is ready to connect"
        exit_code=0
        break # break out of the loop
    fi #end loop
    echo "MySQL wasn't ready going to sleep for 3 seconds"
    sleep 3
done

#check value of exit_code to see why
if [ $exit_code -eq 1 ]; then
    echo "MySQL is not ready. Timed Out"
    exit 1
fi

#if code makes it here then mysql didn't timeout
echo "MySQL is ready to take connections"
echo "Starting phonebook application now..."
#replaced entrypoint command in dockerfile with exec command here
exec java -jar /usr/local/lib/app.jar