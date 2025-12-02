#!/bin/bash

set -e

echo "=== Starting Docker Containers ==="
docker-compose up -d

echo "Waiting for MySQL and Redis to be ready..."
sleep 10

echo ""
echo "=== Starting Microservices ==="

declare -A SERVICE_PIDS

start_service() {
    local service_name=$1
    local port=$2
    echo "Starting $service_name on port $port..."
    cd $service_name
    mvn clean spring-boot:run -Dspring-boot.run.arguments="--server.port=$port" &
    SERVICE_PIDS[$service_name]=$!
    cd ..
    sleep 3
}

start_service "catalog-service" 8001
start_service "cart-service" 8002
start_service "order-service" 8003
start_service "checkout-service" 8004
start_service "payment-service" 8005
start_service "subscription-service" 8006

echo ""
echo "=== All Services Started ==="
echo "Catalog Service:      http://localhost:8001"
echo "Cart Service:         http://localhost:8002"
echo "Order Service:        http://localhost:8003"
echo "Checkout Service:     http://localhost:8004"
echo "Payment Service:      http://localhost:8005"
echo "Subscription Service: http://localhost:8006"
echo ""
echo "Press Ctrl+C to stop all services"

for service in "${!SERVICE_PIDS[@]}"; do
    wait ${SERVICE_PIDS[$service]} || true
done
