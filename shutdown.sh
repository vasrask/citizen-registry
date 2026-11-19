#!/bin/bash

set -e

echo "===================================="
echo " Shutting down Citizen Registry"
echo "===================================="

if [ ! -f .env ]; then
    echo "ERROR: .env file not found!"
    exit 1
fi

set -a
source ./.env
set +a

echo "[1/3] Removing REST API container..."

docker rm -f "$API_CONTAINER" 2>/dev/null || true

echo "[2/3] Removing MySQL container..."

docker rm -f "$DB_CONTAINER" 2>/dev/null || true

echo "[3/3] Removing Docker network..."

docker network rm "$DOCKER_NETWORK" 2>/dev/null || true

echo "===================================="
echo " Shutdown completed!"
echo "===================================="

echo "Persistent volume '$DB_VOLUME' was preserved."
echo "Database data has NOT been deleted."
