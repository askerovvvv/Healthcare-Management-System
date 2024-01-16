FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/healthwave-0.0.1-SNAPSHOT.jar /app/healthwave.jar

EXPOSE 8080

CMD ["java", "-jar", "healthwave.jar"]
