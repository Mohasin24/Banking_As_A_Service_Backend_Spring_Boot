FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY ./target/notification_service-*.jar notification_service.jar
ENTRYPOINT ["java", "-jar", "notification_service"]