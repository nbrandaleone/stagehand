FROM clojure:temurin-20-tools-deps-1.11.1.1323-alpine
# Base image that includes the Clojure CLI tools
# Linux AMD64 - 230 MB compressed

WORKDIR /app

CMD ["clojure", "-M", "-m", "stagehand.app"]
