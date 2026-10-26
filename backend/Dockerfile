FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21-dev@sha256:a68bc234132169bdbe4d895ebffc962bb0aab68a0ffba6bb725bb6dbf6d1acd4
COPY ./build/libs/*all.jar "app.jar"
ENV TZ="Europe/Oslo"
ENV JDK_JAVA_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT [ "java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "app.jar" ]
