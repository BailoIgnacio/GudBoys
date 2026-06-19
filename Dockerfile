# ===== Etapa 1: build =====
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Primero solo el pom para cachear las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ahora el codigo y empaquetamos (saltamos los tests para el build de la imagen)
COPY src ./src
RUN mvn clean package -DskipTests

# ===== Etapa 2: runtime =====
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
