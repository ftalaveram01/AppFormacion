FROM openjdk:17-jdk-alpine
COPY target/*.jar eureka-server.jar
EXPOSE 8761
ENTRYPOINT [ "java", "-jar", "eureka-server.jar", "com.viewnext.eureka"]
