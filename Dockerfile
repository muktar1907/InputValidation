FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install

FROM openjdk:11-jre-slim
#install curl to do health check
RUN apt-get update && apt-get install -y curl netcat && rm -rf /var/lib/apt/lists/*



#setting environment variables
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

COPY --from=build /home/app/target/assignment-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
#copy waiting script
COPY wait_for_mysql.sh /usr/local/bin/wait_for_mysql.sh
#make it executable
RUN chmod +x /usr/local/bin/wait_for_mysql.sh
EXPOSE 8080
ENTRYPOINT ["/usr/local/bin/wait_for_mysql.sh"]