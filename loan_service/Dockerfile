FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY ./target/loan_service-*.jar loan_service.jar
ENTRYPOINT ["java", "-jar", "loan_service"]