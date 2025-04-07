FROM openjdk:17-jdk-alpine
COPY target/*.jar gateway-app.jar
EXPOSE 8106
ENTRYPOINT [ "java", "-jar", "gateway-app.jar" ]
