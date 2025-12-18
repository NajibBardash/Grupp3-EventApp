#!/bin/bash
set -e

echo "Building User-Service..."

cd backend/user-service
mvn clean install

echo "Starting User-Service..."

mvn spring-boot:run 
