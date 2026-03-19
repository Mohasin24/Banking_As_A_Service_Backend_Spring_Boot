FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY ./target/discovery_server-*.jar discovery_server.jar
ENTRYPOINT ["java", "-jar", "discovery_server"]