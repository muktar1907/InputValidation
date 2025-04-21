FROM postman/newman:alpine

# Copy the Postman collection and environment
COPY PhoneBook_API_Test.postman_collection.json /etc/newman/
COPY Testing_Environment.postman_environment.json /etc/newman/


RUN ls -l /etc/newman/
