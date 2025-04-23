#!/bin/bash
#the above is the shebang that specifies which interpreter to use
#executable file that runs docker containers and orchestrates flow of application

set -e #exit on errors
set -o pipefail #return failure if any part of pipeline fails, helps catch errors

echo "Building the Phonebook and postman-tests containers"
docker-compose build

echo "Starting the application"
docker-compose up -d #start the container detached so that other commands can be ran
#wait until healthy
until [ "$(docker inspect --format '{{.State.Health.Status}}' $(docker-compose ps -q phonebook))" == "healthy" ]; do
    echo "phonebook api is not healthy yet"
    sleep 5
done

docker-compose up --abort-on-container-exit postman-tests || true #prevent premature script exit
#get exit code for tests
Postman_Exit_Code=$(docker inspect --format '{{.State.ExitCode}}' $(docker-compose ps -q -a postman-tests)) #ps -q -a gets container id of container with name "postman-tests", the -a forces it to include stopped containers in search
echo "Postman tests code: $Postman_Exit_Code"
#if exit code is failure then stop the application
if [ "$Postman_Exit_Code" -ne 0 ]; then
    echo "Tests failed, check logs for more detail"
    docker-compose down
    docker-compose stop phonebook
else
    echo "All tests passed successfully"
fi
