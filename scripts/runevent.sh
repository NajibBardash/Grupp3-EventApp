#!/bin/bash
set -e

echo "Building event-service (no clean/test)"

cd ../backend/event-service
mvn clean install -DskipTests

echo "Starting event-service..."

mvn spring-boot:run