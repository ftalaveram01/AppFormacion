FROM openjdk:17-jdk-alpine
COPY target/*.jar rol-app.jar
EXPOSE 8084
ENTRYPOINT [ "java", "-jar", "rol-app.jar" ]