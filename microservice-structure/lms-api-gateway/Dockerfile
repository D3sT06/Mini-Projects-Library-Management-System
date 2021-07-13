FROM openjdk:8-jdk-alpine
EXPOSE 7002
ARG JAR_FILE=target/*.jar
ENV PROFILE=dev
ENV REDIS_HOST=localhost
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dspring.redis.host=${REDIS_HOST}", "-jar", "/app.jar"]