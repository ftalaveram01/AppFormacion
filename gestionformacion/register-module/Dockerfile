FROM openjdk:17-jdk-alpine
COPY target/*.jar register-app.jar
EXPOSE 8101
ENTRYPOINT [ "java", "-jar", "register-app.jar" ]