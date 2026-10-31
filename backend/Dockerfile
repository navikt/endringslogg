FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21-dev@sha256:1b79186e5d37e3ae826c8cb566118f4c8af81a30e370a6ce346bf4a67c7ce2c3
COPY ./build/libs/*all.jar "app.jar"
ENV TZ="Europe/Oslo"
ENV JDK_JAVA_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT [ "java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "app.jar" ]
