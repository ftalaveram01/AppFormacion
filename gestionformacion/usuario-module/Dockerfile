FROM openjdk:17-jdk-alpine
COPY target/*.jar usuario-app.jar
EXPOSE 8083
ENTRYPOINT [ "java", "-jar", "usuario-app.jar" ]