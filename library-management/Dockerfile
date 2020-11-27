FROM openjdk:8-jdk-alpine
EXPOSE 7001
ARG JAR_FILE=target/*.jar
ENV PROFILE=dev
ENV MONGO_HOST=localhost
ENV RABBIT_HOST=localhost
ENV REDIS_HOST=localhost
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dspring.data.mongodb.host=${MONGO_HOST}", "-Dspring.rabbitmq.host=${RABBIT_HOST}", "-Dspring.redis.host=${REDIS_HOST}", "-jar", "/app.jar"]