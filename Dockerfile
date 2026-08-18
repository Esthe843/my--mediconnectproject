FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy Maven wrapper and pom.xml first for caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the JAR
RUN ./mvnw clean package -DskipTests -B

# Run the app
EXPOSE 8080
CMD ["java", "-jar", "target/MediConnect-0.0.1-SNAPSHOT.jar"]
