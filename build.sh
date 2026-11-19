#!/bin/bash

set -e

echo "===================================="
echo " Building Citizen Registry"
echo "===================================="

if [ ! -f Dockerfile ]; then
    echo "ERROR: Dockerfile not found!"
    exit 1
fi

if [ ! -f setup.sql ]; then
    echo "ERROR: setup.sql not found!"
    exit 1
fi

if [ ! -f .env ]; then
    echo "ERROR: .env file not found!"
    exit 1
fi

set -a
source ./.env
set +a

echo "[1/2] Building REST API image..."

docker build --no-cache -t "$API_IMAGE" .

echo "[2/2] Creating MySQL volume..."

docker volume create "$DB_VOLUME" > /dev/null

echo "===================================="
echo " Build completed successfully!"
echo " Image: $API_IMAGE"
echo " Volume: $DB_VOLUME"
echo "===================================="
