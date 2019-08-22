#!/bin/bash
PROPETY_PATH=./assemble/src/main/resources/$3

awk -v pat="^$1=" -v value="$1=$2" '{ if ($0 ~ pat) print value; else print $0; }' $PROPETY_PATH > $PROPETY_PATH.tmp
mv $PROPETY_PATH.tmp $PROPETY_PATH
