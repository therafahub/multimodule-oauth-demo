#!/bin/bash

# Script para inicializar las bases de datos en Docker

echo "⏳ Esperando a que PostgreSQL esté listo..."
sleep 5

echo "🗄️  Creando bases de datos..."

# Conectar a PostgreSQL y crear bases de datos
docker exec postgres-db psql -U postgres -c "CREATE DATABASE auth_db;" 2>/dev/null || echo "auth_db ya existe"
docker exec postgres-db psql -U postgres -c "CREATE DATABASE user_db;" 2>/dev/null || echo "user_db ya existe"

echo "✅ Bases de datos inicializadas"
