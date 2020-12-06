#!/bin/sh

# --batch to prevent interactive command
# --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="$GPG_FIREBASE_PRIVATE_KEY" \
    --output keys/firebase-private-key.json keys/firebase-private-key.json.gpg