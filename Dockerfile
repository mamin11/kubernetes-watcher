FROM openjdk:17-alpine
VOLUME /tmp
COPY target/watcher-v1.0.0.jar watcher-v1.0.0.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/watcher-v1.0.0.jar"]