#!/bin/bash

echo "=========================================="
echo "🧪 Test de Integración: Flujo de Login"
echo "=========================================="

WEB_UI="http://localhost:8083"
AUTH_SERVICE="http://localhost:8081"

echo ""
echo "1️⃣ Verificando web-ui accesible..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$WEB_UI/auth/login")
if [ "$RESPONSE" == "200" ]; then
    echo "✅ Web-UI respondiendo en puerto 8083"
else
    echo "❌ Web-UI no accesible (code: $RESPONSE)"
    exit 1
fi

echo ""
echo "2️⃣ Probando auth-service /validate endpoint..."
AUTH_RESPONSE=$(curl -s -X POST "$AUTH_SERVICE/auth/api/v1/auth/validate" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

if echo "$AUTH_RESPONSE" | grep -q "valid"; then
    echo "✅ Auth-service /validate respondiendo"
    echo "$AUTH_RESPONSE"
else
    echo "❌ Auth-service no respondiendo correctamente"
    echo "$AUTH_RESPONSE"
    exit 1
fi

echo ""
echo "3️⃣ Obteniendo página de login..."
LOGIN_PAGE=$(curl -s "$WEB_UI/auth/login")
if echo "$LOGIN_PAGE" | grep -q "Iniciar Sesión\|Entrar\|form"; then
    echo "✅ Página de login accesible"
else
    echo "❌ Página de login no cargó correctamente"
    exit 1
fi

echo ""
echo "4️⃣ Test de autenticación vía formulario..."
COOKIES="/tmp/cookies_test.txt"
# Obtener CSRF token
CSRF=$(curl -s -c "$COOKIES" "$WEB_UI/auth/login" | grep -o 'name="_csrf" value="[^"]*"' | grep -o 'value="[^"]*"' | cut -d'"' -f2)

if [ -n "$CSRF" ]; then
    echo "✅ CSRF token obtenido: ${CSRF:0:10}..."
    
    # Intentar login
    LOGIN_RESULT=$(curl -s -b "$COOKIES" -c "$COOKIES" \
      -X POST "$WEB_UI/login" \
      -d "username=admin&password=admin123&_csrf=$CSRF" \
      -w "\n%{http_code}")
    
    HTTP_CODE=$(echo "$LOGIN_RESULT" | tail -1)
    echo "   POST /login HTTP Response: $HTTP_CODE"
    
    if [ "$HTTP_CODE" == "302" ]; then
        echo "✅ Login retorna 302 redirect"
    else
        echo "⚠️ Login retorna $HTTP_CODE"
    fi
else
    echo "⚠️ CSRF token no encontrado (puede no estar en form)"
fi

echo ""
echo "=========================================="
echo "✅ Test completado"
echo "=========================================="
