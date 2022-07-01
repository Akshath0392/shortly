#!/bin/sh

JARNAME="shortly-0.0.1.jar"
NEWJARNAME="shortly.jar"

if ! [ -x "$(command -v mvn)" ]; then
  echo 'Error: maven cli is not installed.' >&2
  exit 1
fi

echo '[Info] Build creation ... STARTED' >&2
mvn clean install -DskipTests
echo '[Info] Build creation ... COMPLETED' >&2

cp application.properties deployment/application.properties
cp target/$JARNAME deployment/$NEWJARNAME

echo '[Info] Build path - deployment/shortly.jar' >&2