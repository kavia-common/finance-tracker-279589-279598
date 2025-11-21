#!/bin/bash
cd /home/kavia/workspace/code-generation/finance-tracker-279589-279598/android_kotlin_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

