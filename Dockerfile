#
# inspired by https://hub.docker.com/_/java/
# Alpine Linux
# JRE 8
#
#
# BUILD
#   docker build . -t mfs-broker
#
# RUN
#   docker run -it -p 8715:8715 -e PROFILE=ece.test mfs-broker
#

FROM alpine:3.8

ENV LANG C.UTF-8

RUN { \
		echo '#!/bin/sh'; \
		echo 'set -e'; \
		echo; \
		echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; \
	} > /usr/local/bin/docker-java-home \
	&& chmod +x /usr/local/bin/docker-java-home
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin

ENV JAVA_VERSION 8u171
ENV JAVA_ALPINE_VERSION 8.171.11-r0

RUN set -x \
	&& apk add --no-cache \
		openjdk8="$JAVA_ALPINE_VERSION" \
	&& [ "$JAVA_HOME" = "$(docker-java-home)" ]


#
# APP
#
RUN mkdir /app
COPY build/libs/*.jar /app/app.jar

WORKDIR /app
EXPOSE 8080
CMD java -jar app.jar