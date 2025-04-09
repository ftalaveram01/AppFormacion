FROM openjdk:17-jdk-alpine
RUN apk update && apk add --no-cache fontconfig freetype ttf-dejavu cabextract wget
RUN wget https://downloads.sourceforge.net/project/corefonts/the%20fonts/final/arial32.exe -O /tmp/arial32.exe && \
    cabextract /tmp/arial32.exe -d /usr/share/fonts/ttf && \
    fc-cache -f -v
COPY target/*.jar convocatoria-app.jar
EXPOSE 8105
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "convocatoria-app.jar"]
