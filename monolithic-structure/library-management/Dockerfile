FROM openjdk:8-jdk-alpine
EXPOSE 7001
ARG JAR_FILE=target/*.jar
ENV PROFILE=dev
ENV RABBIT_HOST=localhost
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dspring.rabbitmq.host=${RABBIT_HOST}", "-jar", "/app.jar"]