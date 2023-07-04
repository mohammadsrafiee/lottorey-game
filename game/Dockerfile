FROM maven:amazoncorretto AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package


FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080:8080
COPY --from=build /home/app/target/*.jar /usr/local/lib/game.jar
ENTRYPOINT 	["java","-jar","/usr/local/lib/game.jar"]