#!/bin/bash

# Script para detener y limpiar los servicios

set -e

echo "🛑 Deteniendo servicios Docker..."
docker-compose down

echo "🧹 Limpiando volúmenes..."
docker-compose down -v

echo "✅ Servicios detenidos"
