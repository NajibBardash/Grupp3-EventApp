#!/bin/bash
set -e

echo "Building Booking-Service..."

cd ../backend/booking-service
mvn clean install

echo "Starting Booking-Service..."

mvn spring-boot:run 
