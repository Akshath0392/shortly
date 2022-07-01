#!/bin/sh

JAVAJARPATH=./shortly.jar

# Kill old process
JAVAPID=$(ps ax | grep $JAVAJARPATH | grep -v grep | head -1 | awk '{print $1}')
if [ "$JAVAPID" != "" ]
then
    kill -15 $JAVAPID
fi