#!/usr/bin/env bash

echo "Prepare Properties for CI start."

# 发布模式
PUBLISH_MODEL=$1

# 打包机器 输入参数转成小写
MACHINE=$(echo $2 | tr '[:upper:]' '[:lower:]')

if [ ! -n "$1" ];then
  PUBLISH_MODEL=2
fi

if [ ! -n "$2" ];then
  MACHINE="mac"
fi

function prepare_properties_linux() {
  # gradle
  sed -i 's/.*org.gradle.daemon=.*/org.gradle.daemon=false/g' gradle.properties
  sed -i 's/.*org.gradle.parallel=.*/org.gradle.parallel=false/g' gradle.properties

  # params
  sed -i 's/.*PUBLISH_MODEL=.*/PUBLISH_MODEL='${PUBLISH_MODEL}'/g' gradle.properties

  # custom
  sed -i 's/.*FAST_BUILD=.*/FAST_BUILD=false/g' gradle.properties
}

function prepare_properties_mac() {
  # gradle
  sed -i "" 's/.*org.gradle.daemon=.*/org.gradle.daemon=false/g' gradle.properties
  sed -i "" 's/.*org.gradle.parallel=.*/org.gradle.parallel=false/g' gradle.properties

  # params
  sed -i "" 's/.*PUBLISH_MODEL=.*/PUBLISH_MODEL='${PUBLISH_MODEL}'/g' gradle.properties

  # custom
  sed -i "" 's/.*FAST_BUILD=.*/FAST_BUILD=false/g' gradle.properties

}

if [[ ${MACHINE} == "linux" ]]; then
  prepare_properties_linux
elif [[ ${MACHINE} == "mac" ]]; then
  prepare_properties_mac
else
  echo "Machine must be linux or mac!"
  echo 'Prepare Properties for CI failed!'
  exit 1
fi

echo 'Prepare Properties for CI success!'
exit 0
