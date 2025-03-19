FROM openjdk:17-jdk-alpine
COPY target/*.jar convocatoria-app.jar
EXPOSE 8085
ENTRYPOINT [ "java", "-jar", "convocatoria-app.jar" ]
