FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY ./target/payment_service-*.jar payment_service.jar
ENTRYPOINT ["java", "-jar", "payment_service"]