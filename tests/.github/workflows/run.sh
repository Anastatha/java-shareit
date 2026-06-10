#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")/../../.."

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker is not available in this environment" >&2
  exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
  echo "Docker Compose is not available in this environment" >&2
  exit 1
fi

printf 'Starting Docker Compose environment...\n'
if ! docker compose up --build -d --wait --wait-timeout 180; then
  printf 'Compose --wait failed, falling back to manual wait...\n'
  docker compose up --build -d

  for _ in {1..60}; do
    if docker compose ps --format json 2>/dev/null | grep -q '"State":"running"'; then
      break
    fi
    sleep 1
  done
fi

printf 'Waiting for server on port 9090...\n'
curl -fsS --retry 120 --retry-connrefused --retry-delay 1 http://localhost:9090/actuator/health >/dev/null || {
  echo 'Server did not become ready in time' >&2
  docker compose ps
  exit 1
}

printf 'Waiting for gateway on port 8080...\n'
curl -fsS --retry 120 --retry-connrefused --retry-delay 1 http://localhost:8080/users >/dev/null || {
  echo 'Gateway did not become ready in time' >&2
  docker compose ps
  exit 1
}

printf 'Running project tests...\n'
mvn test -DskipTests=false
