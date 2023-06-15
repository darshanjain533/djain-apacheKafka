#
# Build stage
#
FROM maven:3.8.3-openjdk-17  AS build
COPY src ./src
COPY pom.xml .
RUN mvn -f ./pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:17
WORKDIR /kafka/consumer
MAINTAINER djain
EXPOSE 9001
COPY --from=build ./target/consumer.jar /usr/local/lib/consumer.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/consumer.jar"]