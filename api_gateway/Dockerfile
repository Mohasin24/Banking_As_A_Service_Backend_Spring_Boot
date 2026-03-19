FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY ./target/api_gateway-*.jar api_gateway.jar
ENTRYPOINT ["java", "-jar", "api_gateway"]