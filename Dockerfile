FROM eclipse-temurin:20-jre-alpine
# Linux AMD64 - 58 MB compressed

WORKDIR /app

COPY ./target/stagehand-1.2.0-standalone.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "stagehand-1.2.0-standalone.jar"]
