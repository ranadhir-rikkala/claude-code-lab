#!/usr/bin/env bash
grep -v '^#' "$1" | grep -v '^$' | awk -F'|' '{ print $1 "|" $2 }' | sort | uniq -c | sort -rn
