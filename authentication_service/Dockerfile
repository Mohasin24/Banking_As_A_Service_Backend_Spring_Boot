FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY ./target/authentication_service-*.jar authentication_service.jar
ENTRYPOINT ["java", "-jar", "authentication_service"]