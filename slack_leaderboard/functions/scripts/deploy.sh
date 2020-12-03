#!/bin/sh

if [[ "$CI" == "true" ]]; then
    firebase deploy --only functions --token "$FIREBASE_TOKEN"
else
    firebase deploy --only functions
fi