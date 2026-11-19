#!/bin/bash

set -e

echo "===================================="
echo " Starting Citizen Registry"
echo "===================================="

if [ ! -f .env ]; then
    echo "ERROR: .env file not found!"
    exit 1
fi

set -a
source ./.env
set +a

if ! docker image inspect "$API_IMAGE" > /dev/null 2>&1; then
    echo "ERROR: API image not found. Run ./build.sh first."
    exit 1
fi

echo "[1/4] Creating Docker network..."

docker network inspect "$DOCKER_NETWORK" > /dev/null 2>&1 || \
docker network create "$DOCKER_NETWORK" > /dev/null

docker rm -f "$API_CONTAINER" "$DB_CONTAINER" > /dev/null 2>&1 || true

echo "[2/4] Starting MySQL container..."

docker run -d \
    --name "$DB_CONTAINER" \
    --network "$DOCKER_NETWORK" \
    --restart unless-stopped \
    -e MYSQL_ROOT_PASSWORD="$MYSQL_ROOT_PASSWORD" \
    -e MYSQL_DATABASE="$MYSQL_DATABASE" \
    -e MYSQL_USER="$MYSQL_USER" \
    -e MYSQL_PASSWORD="$MYSQL_PASSWORD" \
    -v "$DB_VOLUME:/var/lib/mysql" \
    -v "$PWD/setup.sql:/docker-entrypoint-initdb.d/01-setup.sql:ro" \
    --health-cmd='mysqladmin ping -h 127.0.0.1 -u root -p"$MYSQL_ROOT_PASSWORD" --silent' \
    --health-interval=5s \
    --health-timeout=3s \
    --health-retries=20 \
    mysql:8.4

echo "[3/4] Waiting for MySQL..."

for i in $(seq 1 60); do

    STATUS=$(docker inspect \
        --format='{{.State.Health.Status}}' \
        "$DB_CONTAINER" 2>/dev/null || echo "starting")

    if [ "$STATUS" = "healthy" ]; then
        echo "MySQL is ready!"
        break
    fi

    if [ "$STATUS" = "unhealthy" ]; then
        echo "ERROR: MySQL container is unhealthy!"
        docker logs "$DB_CONTAINER"
        exit 1
    fi

    if [ "$i" -eq 60 ]; then
        echo "ERROR: MySQL startup timeout!"
        docker logs "$DB_CONTAINER"
        exit 1
    fi

    sleep 2
done

echo "[4/4] Starting REST API..."

docker run -d \
    --name "$API_CONTAINER" \
    --network "$DOCKER_NETWORK" \
    --restart unless-stopped \
    -p 8080:8080 \
    -e DB_URL="jdbc:mysql://$DB_CONTAINER:3306/$MYSQL_DATABASE?sslMode=DISABLED&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
    -e DB_USER="$MYSQL_USER" \
    -e DB_PASSWORD="$MYSQL_PASSWORD" \
    "$API_IMAGE"

echo "===================================="
echo " Citizen Registry started!"
echo " REST API: http://localhost:8080/api/citizens"
echo "===================================="
