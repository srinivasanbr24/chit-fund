#! /bin/bash

set -e
# The script will exit immediately if any command exits with a non-zero status.

CONTAINER_NAME="akb-chit-fund-app"
DB_CONTAINER_NAME="mysql-akb-app"
APP_PORT="8082"
PROFILE="local"

echo "Stopping and removing any existing container named $CONTAINER_NAME... and $DB_CONTAINER_NAME..."

# 1. Gracefully stop and remove existing containers, networks, and anonymous volumes managed by compose
docker-compose down || true

# 2. Double-check cleanup for standalone instances if necessary
docker stop "$CONTAINER_NAME" "$DB_CONTAINER_NAME" 2>/dev/null || true
docker rm "$CONTAINER_NAME" "$DB_CONTAINER_NAME" 2>/dev/null || true

echo "Starting Docker build and re-triggering the new instances..."

# 3. Force rebuild of the application service and bring everything up in detached mode
docker-compose up --build -d

# shellcheck disable=SC2181
if [ $? -eq 0 ]; then
    echo "✅ SUCCESS: Application is running on http://localhost:$APP_PORT with the '$PROFILE' profile."
    echo "Tail the logs with: docker compose -f app"
else
    echo "❌ ERROR: Failed to start the containers."
    exit 1
fi