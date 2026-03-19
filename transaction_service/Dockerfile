FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY ./target/transaction_service-*.jar transaction_service.jar
ENTRYPOINT ["java", "-jar", "transaction_service"]