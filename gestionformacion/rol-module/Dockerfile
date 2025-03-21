FROM openjdk:17-jdk-alpine
COPY target/*.jar rol-app.jar
EXPOSE 8104
ENTRYPOINT [ "java", "-jar", "rol-app.jar" ]