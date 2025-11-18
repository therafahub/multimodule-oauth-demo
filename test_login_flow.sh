#!/bin/bash

echo "=========================================="
echo "🧪 Test de Integración: Flujo de Login"
echo "=========================================="

# Variables
WEB_UI="http://localhost:8083"
AUTH_SERVICE="http://localhost:8081"
USERNAME="admin"
PASSWORD="admin123"

echo ""
echo "1️⃣ Verificando que web-ui está accesible..."
curl -s -o /dev/null -w "Status: %{http_code}\n" "$WEB_UI/auth/login"

echo ""
echo "2️⃣ Obteniendo la página de login (con CSRF token)..."
LOGIN_PAGE=$(curl -s -c /tmp/cookies.txt "$WEB_UI/auth/login")
CSRF_TOKEN=$(echo "$LOGIN_PAGE" | grep -oP 'value="\K[^"]*' | head -1)
CSRF_PARAM=$(echo "$LOGIN_PAGE" | grep -oP 'name="\K[^"]*(?=.*_csrf)' | head -1)

if [ -z "$CSRF_TOKEN" ]; then
    echo "❌ No se pudo obtener CSRF token"
    exit 1
fi

echo "✅ CSRF Token obtenido: ${CSRF_TOKEN:0:20}..."
echo ""

echo "3️⃣ Probando auth-service /validate endpoint..."
curl -s -X POST "$AUTH_SERVICE/auth/api/v1/auth/validate" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}" | jq .

echo ""
echo "4️⃣ Intentando login vía form..."
LOGIN_RESPONSE=$(curl -s -b /tmp/cookies.txt -c /tmp/cookies.txt \
  -X POST "$WEB_UI/login" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=$USERNAME&password=$PASSWORD&${CSRF_PARAM}=$CSRF_TOKEN" \
  -w "\n%{http_code}")

HTTP_CODE=$(echo "$LOGIN_RESPONSE" | tail -1)
RESPONSE_BODY=$(echo "$LOGIN_RESPONSE" | head -n -1)

echo "HTTP Status: $HTTP_CODE"

if [ "$HTTP_CODE" == "302" ]; then
    echo "✅ Login devolvió 302 (redirect esperado)"
    
    echo ""
    echo "5️⃣ Obteniendo página del dashboard..."
    DASHBOARD=$(curl -s -b /tmp/cookies.txt "$WEB_UI/dashboard")
    
    if echo "$DASHBOARD" | grep -q "dashboard"; then
        echo "✅ Dashboard accesible y autenticado"
    else
        echo "⚠️ Dashboard retornado, verificando contenido..."
        echo "$DASHBOARD" | head -20
    fi
elif [ "$HTTP_CODE" == "200" ]; then
    echo "❌ Login devolvió 200 (esperaba 302 redirect)"
    echo "$RESPONSE_BODY" | head -5
else
    echo "❌ Código HTTP inesperado: $HTTP_CODE"
fi

echo ""
echo "=========================================="
echo "✅ Test completado"
echo "=========================================="
