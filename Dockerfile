# syntax=docker/dockerfile:1

# Comments are provided throughout this file to help you get started.
# If you need more help, visit the Dockerfile reference guide at
# https://docs.docker.com/go/dockerfile-reference/

# Want to help us make this template better? Share your feedback here: https://forms.gle/ybq9Krt8jtBL3iCk7

################################################################################

# Stage 1: Build Dependencies
# This stage resolves and downloads all project dependencies.
# We're updating the base image tag from '17.0.14-jdk-jammy' to '17-jdk-jammy'.
# This ensures Docker pulls the latest patch version of Java 17 JDK
# based on Ubuntu 22.04 (Jammy Jellyfish), which is more reliable
# as specific patch versions like 17.0.14 might not always be available
# or might be superseded.
FROM eclipse-temurin:17-jdk-jammy as deps

WORKDIR /build

# Copy the Maven wrapper with executable permissions.
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

# Download dependencies as a separate step to take advantage of Docker's caching.
# Leverage a cache mount to /root/.m2 so that subsequent builds don't have to
# re-download packages, speeding up builds if only source code changes.
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

################################################################################

# Stage 2: Build the Application Package
# This stage builds the application JAR, relying on the dependencies downloaded
# in the 'deps' stage.
FROM deps as package

WORKDIR /build

# Copy the source code into the build environment.
COPY ./src src/
COPY pom.xml .

# Package the application into an executable (uber) JAR.
# We use 'mv' to rename the generated JAR to 'app.jar' for a simpler ENTRYPOINT.
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################

# Stage 3: Extract Application Layers
# This stage uses Spring Boot's layer tools to extract the application
# into separate layers (dependencies, Spring Boot loader, snapshot dependencies, application code).
# This is a Docker best practice for Spring Boot apps as it optimizes image caching
# (layers that change less frequently are cached more effectively).
FROM package as extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################

# Stage 4: Final Runtime Image
# This is the final image for running the application. It contains only the
# minimal runtime dependencies needed for the application, resulting in a small
# and secure image.
# We're updating the base image tag from '17.0.14-jre-jammy' to '17-jre-jammy'.
# This ensures Docker pulls the latest patch version of Java 17 JRE
# based on Ubuntu 22.04 (Jammy Jellyfish).
FROM eclipse-temurin:17-jre-jammy AS final

# Create a non-privileged user that the app will run under.
# This enhances security by not running the application as root inside the container.
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

# Copy the extracted layers from the 'extract' stage.
# These are copied in the order recommended by Spring Boot for optimal caching.
COPY --from=extract build/target/extracted/dependencies/ ./
COPY --from=extract build/target/extracted/spring-boot-loader/ ./
COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract build/target/extracted/application/ ./

# Expose the port your Spring Boot application listens on.
EXPOSE 8080

# Define the command to run the application when the container starts.
# This uses Spring Boot's JarLauncher to correctly load the layered JAR.
ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]
