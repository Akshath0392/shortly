#!/bin/sh

JARNAME="shortly.jar"
JARCONFIG="application.properties"
date="$(date +'%Y%m%d')"

nohup java -jar ./$JARNAME --spring.config.location=$JARCONFIG > logs/bgserver-${date}.out 2>&1 &