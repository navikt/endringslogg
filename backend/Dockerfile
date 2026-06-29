FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21-dev@sha256:d5e854b669ca3557b35431e93009bdf779f32b8db479c2a77acd8a5623007e65
COPY ./build/libs/*all.jar "app.jar"
ENV TZ="Europe/Oslo"
ENV JDK_JAVA_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT [ "java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "app.jar" ]
