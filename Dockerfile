FROM maven:3.5.2-jdk-8-alpine AS builder
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -DskipTests

FROM openjdk:8-jre-alpine
COPY --from=builder /tmp/target/ldh.war /ldh.war
WORKDIR /
EXPOSE 14004

CMD ["java", "-jar", "/ldh.war"]
