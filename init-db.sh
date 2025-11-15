#!/bin/bash

# Script para crear las bases de datos

POSTGRES_USER="postgres"
POSTGRES_PASSWORD="postgres"
POSTGRES_HOST="localhost"
POSTGRES_PORT="5432"

echo "🗄️  Creando bases de datos..."

# Crear base de datos auth_db
psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -tc "SELECT 1 FROM pg_database WHERE datname = 'auth_db'" | grep -q 1 || psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -c "CREATE DATABASE auth_db"

# Crear base de datos user_db
psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -tc "SELECT 1 FROM pg_database WHERE datname = 'user_db'" | grep -q 1 || psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -c "CREATE DATABASE user_db"

echo "✅ Bases de datos creadas"
