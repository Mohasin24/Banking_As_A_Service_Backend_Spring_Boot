FROM gcr.io/distroless/java17
LABEL authors="Mohasin_Patel"
WORKDIR /app
COPY target/config_server-*.jar config_server.jar
ENTRYPOINT ["java", "-jar", "config_server"]