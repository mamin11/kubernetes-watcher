FROM openjdk:17-alpine
VOLUME /tmp
COPY target/watcher-0.0.1-SNAPSHOT.jar watcher-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/watcher-0.0.1-SNAPSHOT.jar"]