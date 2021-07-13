FROM openjdk:8-jdk-alpine
EXPOSE 7007
ARG JAR_FILE=target/*.jar
ENV PROFILE=dev
ENV REDIS_HOST=localhost
ENV ACTIVE_BROKER_URL='tcp://localhost:61616'
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dspring.redis.host=${REDIS_HOST}",  "-Dspring.activemq.broker-url=${ACTIVE_BROKER_URL}", "-jar", "/app.jar"]