FROM gradle:jdk17 AS build

# Copy entire parent project
COPY --chown=gradle:gradle . /home/gradle/project/
WORKDIR /home/gradle/project/

# Build the connector subproject using parent's gradle wrapper
RUN ./gradlew :connector:build --no-daemon

FROM openjdk:17-slim

WORKDIR /app
# Copy the jar from the connector subproject build output
COPY --from=build /home/gradle/project/connector/build/libs/connector.jar /app

ENV EDC_FS_CONFIG="/config/configuration.properties"

EXPOSE 9191
EXPOSE 9192
EXPOSE 9193
EXPOSE 9194
EXPOSE 9291

ENTRYPOINT ["java", "-jar", "connector.jar"]