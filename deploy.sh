#!/bin/bash

# Script para construir y desplegar todos los servicios

set -e

echo "🏗️  Construyendo proyecto Maven..."
mvn clean package -DskipTests=true

echo "🐳 Construyendo imágenes Docker..."
docker-compose build

echo "🚀 Iniciando servicios con Docker Compose..."
docker-compose up -d

echo "⏳ Esperando a que los servicios estén listos..."
sleep 10

echo "✅ Servicios iniciados!"
echo ""
echo "📍 URLs disponibles:"
echo "   - Gateway:     http://localhost:8080"
echo "   - Auth Service: http://localhost:8081/auth"
echo "   - User Service: http://localhost:8082/users"
echo "   - Web UI:      http://localhost:8083/ui"
echo "   - Prometheus:  http://localhost:9090"
echo "   - Grafana:     http://localhost:3000 (admin/admin)"
echo ""
echo "📋 Logs:"
docker-compose logs -f
