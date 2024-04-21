#!/usr/bin/bash

set -a
. ./.env
set +a
./mvnw clean install
