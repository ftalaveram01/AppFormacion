FROM openjdk:17-jdk-alpine
COPY target/*.jar login-app.jar
EXPOSE 8100
ENTRYPOINT [ "java", "-jar", "login-app.jar" ]