FROM openjdk:17-jdk-alpine
COPY target/*.jar course-app.jar
EXPOSE 8082
ENTRYPOINT [ "java", "-jar", "course-app.jar" ]