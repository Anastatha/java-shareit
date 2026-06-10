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
docker compose up --build -d

for _ in {1..60}; do
  if docker compose ps --format json 2>/dev/null | grep -q '"State":"running"'; then
    break
  fi
  sleep 1
 done

printf 'Waiting for server on port 9090...\n'
for _ in {1..60}; do
  if curl -fsS http://localhost:9090/actuator/health >/dev/null 2>&1; then
    break
  fi
  sleep 1
 done

printf 'Waiting for gateway on port 8080...\n'
for _ in {1..60}; do
  if curl -fsS http://localhost:8080/ >/dev/null 2>&1; then
    break
  fi
  sleep 1
 done

printf 'Running project tests...\n'
mvn test -DskipTests=false
