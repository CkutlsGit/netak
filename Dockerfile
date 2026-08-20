FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S netak && adduser -S netak -G netak

COPY target/netak-0.0.1-SNAPSHOT.jar /app/netak.jar

RUN chown -R netak:netak /app

USER netak

ENTRYPOINT ["java", "-jar", "netak.jar", "--spring.profiles.active=dockerdev"]