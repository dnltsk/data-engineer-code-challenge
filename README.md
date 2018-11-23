[![Build Status](https://travis-ci.org/dnltsk/data-engineer-code-challenge.svg?branch=master)](https://travis-ci.org/dnltsk/data-engineer-code-challenge)
[![codebeat badge](https://codebeat.co/badges/e8acd680-a0ea-4aa9-9f51-61ed1b3d0fbc)](https://codebeat.co/projects/github-com-dnltsk-data-engineer-code-challenge-master)
[![codecov](https://codecov.io/gh/dnltsk/data-engineer-code-challenge/branch/master/graph/badge.svg)](https://codecov.io/gh/dnltsk/data-engineer-code-challenge)

# data-engineer-code-challenge

## Requirements

to build & run locally:

* JDK & JRE >= 1.8
* Postgres 9 + PostGIS service

to build & run containerized environment:

* docker
* docker-compose

for IDE:

* Lombok plugin

## test, build, start

test: `./gradlew clean test`

integration test: `./gradlew clean integrationTest` :warning: depends on a running Postgres instance, see `applications.yml`

build: `./gradlew clean build`

start: `./gradlew clean bootRun` :warning: depends on a running Postgres instance, see `applications.yml`


## start containerized environment

`docker-compose up` :warning: depends on build (see above), no Postgres required.

## accessing

http://localhost:8080