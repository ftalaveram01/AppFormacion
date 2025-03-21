FROM openjdk:17-jdk-alpine
COPY target/*.jar usuario-app.jar
EXPOSE 8103
ENTRYPOINT [ "java", "-jar", "usuario-app.jar" ]