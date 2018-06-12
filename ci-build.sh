#!/usr/bin/env bash

sh ci/ci-properties.sh $1 $2
./gradlew clean assembleRelease
