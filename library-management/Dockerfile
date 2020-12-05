FROM openjdk:8-jdk-alpine
EXPOSE 7001
ARG JAR_FILE=target/*.jar
ENV PROFILE=dev
ENV MONGO_HOST=localhost
ENV RABBIT_HOST=localhost
ENV REDIS_HOST=localhost
ENV ACTIVE_BROKER_URL='tcp://localhost:61616'
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dspring.data.mongodb.host=${MONGO_HOST}", "-Dspring.rabbitmq.host=${RABBIT_HOST}", "-Dspring.redis.host=${REDIS_HOST}",  "-Dspring.activemq.broker-url=${ACTIVE_BROKER_URL}", "-jar", "/app.jar"]