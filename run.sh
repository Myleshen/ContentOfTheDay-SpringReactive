#!/usr/bin/bash

set -a;
. ./.env;
set +a;
./mvnw clean install;
docker build cotd:0.0.1 .;
docker compose up -d;