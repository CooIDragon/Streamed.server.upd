#!/bin/bash

KEY=$1

# Clean up the key to be filename-safe
FILENAME=$(echo "$KEY" | sed 's/[^a-zA-Z0-9_-]/_/g')

# Move the recorded file to its final location
mv /media/_tmp.flv /media/"$FILENAME.mp4"
