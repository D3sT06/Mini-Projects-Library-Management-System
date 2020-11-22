FROM openjdk:8-jdk-alpine
EXPOSE 7001
ARG JAR_FILE=target/*.jar
ENV PROFILE=dev
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-jar", "/app.jar"]