FROM gradle:jdk17 AS build

# Copy entire parent project
COPY --chown=gradle:gradle . /home/gradle/project/
WORKDIR /home/gradle/project/

# Build the connector subproject using parent's gradle wrapper
RUN ./gradlew :connector:build --no-daemon

FROM openjdk:17

WORKDIR /app
# Copy the jar from the connector subproject build output
COPY --from=build /home/gradle/project/connector/build/libs/connector.jar /app

ENV EDC_FS_CONFIG="/config/configuration.properties"

EXPOSE 9191
EXPOSE 9192
EXPOSE 9193
EXPOSE 9194
EXPOSE 9291

HEALTHCHECK --interval=5s --timeout=2s CMD curl -f http://localhost:9191/api/check/health || exit 1

ENTRYPOINT ["java", "-jar", "connector.jar"]