FROM openjdk:17-jdk-alpine
COPY target/*.jar convocatoria-app.jar
EXPOSE 8105
ENTRYPOINT [ "java", "-jar", "convocatoria-app.jar" ]
