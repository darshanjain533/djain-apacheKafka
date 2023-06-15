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
WORKDIR /kafka/producer
MAINTAINER djain
EXPOSE 9000
COPY --from=build ./target/producer.jar /usr/local/lib/producer.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/producer.jar"]