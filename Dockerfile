FROM gradle:8.12.1-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon --stacktrace --info

FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar /app/study-blog.jar 
CMD ["java", "-jar", "/app/study-blog.jar"]
