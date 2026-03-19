FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY ./target/user_service-*.jar user_service.jar
ENTRYPOINT ["java", "-jar", "user_service"]