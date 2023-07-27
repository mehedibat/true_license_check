FROM openjdk:21-slim-bullseye
WORKDIR /app
ADD target/MicroService1-0.0.1-SNAPSHOT.jar MicroService1-0.0.1-SNAPSHOT.jar
EXPOSE 8891

ENTRYPOINT ["java","--enable-preview","-Xmx300m","-jar","MicroService1-0.0.1-SNAPSHOT.jar"]
