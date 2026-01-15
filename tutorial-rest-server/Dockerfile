FROM eclipse-temurin:8-jre-ubi10-minimal


ARG IMAGE_TITLE="OpenDMA Tutorial xmlrepo Server"
ARG IMAGE_DESCRIPTION="Sample REST server providing a sample XML-based repository for the OpenDMA tutorial."
ARG IMAGE_SOURCE="https://github.com/OpenDMA/opendma-java-tutorial/tutorial-rest-server"
ARG IMAGE_URL="https://github.com/OpenDMA/opendma-java-tutorial/blob/main/tutorial-rest-server/README.md"
ARG IMAGE_DOCUMENTATION="https://github.com/OpenDMA/opendma-java-tutorial/blob/main/tutorial-rest-server/README.md"
ARG IMAGE_LICENSES="AGPL-3.0-only"
ARG IMAGE_AUTHORS="Stefan Kopf, xaldon Technologies GmbH"
ARG IMAGE_VENDOR="xaldon Technologies GmbH, https://xaldon.com"

ARG IMAGE_VERSION
ARG VCS_REF
ARG BUILD_DATE

RUN test -n "$IMAGE_VERSION" || (echo "IMAGE_VERSION is required" >&2 && exit 1) \
 && test -n "$VCS_REF" || (echo "VCS_REF is required" >&2 && exit 1) \
 && test -n "$BUILD_DATE" || (echo "BUILD_DATE is required" >&2 && exit 1)

LABEL org.opencontainers.image.title=$IMAGE_TITLE \
      org.opencontainers.image.description=$IMAGE_DESCRIPTION \
      org.opencontainers.image.source=$IMAGE_SOURCE \
      org.opencontainers.image.url=$IMAGE_URL \
      org.opencontainers.image.documentation=$IMAGE_DOCUMENTATION \
      org.opencontainers.image.licenses=$IMAGE_LICENSES \
      org.opencontainers.image.authors=$IMAGE_AUTHORS \
      org.opencontainers.image.vendor=$IMAGE_VENDOR \
      org.opencontainers.image.version=$IMAGE_VERSION \
      org.opencontainers.image.revision=$VCS_REF \
      org.opencontainers.image.created=$BUILD_DATE


WORKDIR /app

COPY target/opendma-tutorial-rest-server-*.jar /app/app.jar
COPY target/docker-extra/sample-content.bin /app/sample-content.bin

ENTRYPOINT ["java", "-jar", "/app/app.jar"]