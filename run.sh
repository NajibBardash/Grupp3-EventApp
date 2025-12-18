#!/bin/bash
set -e

docker compose down
docker compose build
docker compose up -d

echo "Building services (no clean/test)"

cd ./backend/user-service
mvn install -DskipTests
cd ./backend/event-service
mvn install -DskipTests
cd ./backend/booking-service
mvn install -DskipTests

echo "Starting services..."

cd backend/user-service
mvn spring-boot:run &
cd ../event-service
mvn spring-boot:run &
cd ../booking-service
mvn spring-boot:run &

cd ../../frontend
npm install
npm run dev
