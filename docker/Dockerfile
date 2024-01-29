FROM openjdk:17

RUN mkdir /app
EXPOSE 8080

COPY ./ktor-application.jar /app/ktor-application.jar
WORKDIR /app

CMD ["java", "-jar", "ktor-application.jar"]
