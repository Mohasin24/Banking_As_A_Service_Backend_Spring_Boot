FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY ./target/account_service-*.jar account_service.jar
ENTRYPOINT ["java", "-jar", "account-service"]