FROM node:24-bookworm-slim AS frontend
WORKDIR /app
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
ENV VITE_PUBLIC_DEMO=true
RUN npm run build

FROM maven:3.9-eclipse-temurin-21 AS backend
WORKDIR /app
COPY backend/pom.xml ./
COPY backend/src/main ./src/main
COPY --from=frontend /app/dist/ ./src/main/resources/static/
RUN mvn -B -Dmaven.test.skip=true package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend /app/target/adotamatch-backend-*.jar ./adotamatch.jar
ENV SPRING_PROFILES_ACTIVE=demo
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=45 -XX:+UseSerialGC"
EXPOSE 10000
USER 10001
ENTRYPOINT ["java", "-jar", "/app/adotamatch.jar"]
