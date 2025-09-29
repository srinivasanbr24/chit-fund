#! /bin/bash

set -e
# The script will exit immediately if any command exits with a non-zero status.

CONTAINER_NAME="akb-chit-fund-app"
IMAGE_TAG="akb-chit-fund-app:latest"
APP_PORT="8082"
PROFILE="local"

echo "Stopping and removing any existing container named /$CONTAINER_NAME..."

# 1. Stop the existing container if it's running.
docker stop "$CONTAINER_NAME" || true

# 2. Remove the existing container.
docker rm "$CONTAINER_NAME" || true

echo "Starting Docker build..."

docker build -t "$IMAGE_TAG" .

echo "Starting Docker container..."

docker run \
    -d \
    -e SPRING_PROFILES_ACTIVE="$PROFILE" \
    -p "$APP_PORT":"$APP_PORT" \
    --name "$CONTAINER_NAME" \
    "$IMAGE_TAG"

# shellcheck disable=SC2181
if [ $? -eq 0 ]; then
    echo "✅ SUCCESS: Application is running on http://localhost:$APP_PORT with the '$PROFILE' profile."
    echo "Tail the logs with: docker logs -f $CONTAINER_NAME"
else
    echo "❌ ERROR: Failed to start the container."
    exit 1
fi