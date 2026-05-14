FROM gradle:9-jdk21 AS build

# Copy entire parent project
COPY --chown=gradle:gradle . /home/gradle/project/
WORKDIR /home/gradle/project/

RUN --mount=type=cache,target=/home/gradle/.gradle/caches \
    gradle :connector:build --no-daemon

FROM eclipse-temurin:21

WORKDIR /app
COPY --from=build /home/gradle/project/connector/build/libs/connector.jar /app

ENV EDC_FS_CONFIG="/config/configuration.properties"

HEALTHCHECK --interval=5s --timeout=2s CMD curl -f http://localhost:9191/api/check/health || exit 1

EXPOSE 9191
EXPOSE 9192
EXPOSE 9193
EXPOSE 9194
EXPOSE 9291

ENTRYPOINT ["java", "-jar", "connector.jar"]