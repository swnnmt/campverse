# ========== STAGE 1: Build ==========
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# copy pom.xml trước để cache dependency
COPY pom.xml .

# tải dependency về trước (cache tốt hơn)
RUN mvn dependency:go-offline -B

# copy src sau
COPY src ./src

# build app
RUN mvn clean package -DskipTests

# ========== STAGE 2: Run ==========
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
