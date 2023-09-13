FROM openjdk:17-alpine
EXPOSE 8080
ADD target/java-laza-backend.jar java-laza-backend.jar
ENTRYPOINT ["java", "-jar", "java-laza-backend.jar"]