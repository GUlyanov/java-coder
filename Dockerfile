FROM maven as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

#FROM eclipse-temurin:17-jre-alpine
FROM openjdk:21-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
EXPOSE 8189
ENTRYPOINT ["java", "-jar", "/app/*.jar"]