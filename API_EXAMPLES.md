# EJEMPLOS DE USO DE LA API

## 🔐 Auth Service API

### Base URL: `http://localhost:8081/auth/api/v1/auth`

---

### 1. REGISTRAR NUEVO USUARIO

```bash
curl -X POST http://localhost:8081/auth/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan.perez",
    "email": "juan.perez@example.com",
    "password": "SecurePass@123",
    "firstName": "Juan",
    "lastName": "Pérez García"
  }'
```

**Respuesta (201 Created):**
```json
{
  "code": "CREATED",
  "message": "Resource created successfully",
  "data": {
    "id": 1,
    "username": "juan.perez",
    "email": "juan.perez@example.com",
    "first_name": "Juan",
    "last_name": "Pérez García",
    "enabled": true,
    "roles": ["USER"]
  },
  "timestamp": "2024-11-14T10:30:45.123456",
  "status": 201
}
```

---

### 2. OBTENER USUARIO POR USERNAME

```bash
curl http://localhost:8081/auth/api/v1/auth/users/juan.perez
```

**Respuesta (200 OK):**
```json
{
  "code": "SUCCESS",
  "message": "User found",
  "data": {
    "id": 1,
    "username": "juan.perez",
    "email": "juan.perez@example.com",
    "first_name": "Juan",
    "last_name": "Pérez García",
    "enabled": true,
    "roles": ["USER"]
  },
  "timestamp": "2024-11-14T10:31:15.654321",
  "status": 200
}
```

---

### 3. OBTENER TODOS LOS USUARIOS (Admin)

```bash
curl http://localhost:8081/auth/api/v1/auth/users
```

**Respuesta (200 OK):**
```json
{
  "code": "SUCCESS",
  "message": "Users retrieved",
  "data": [
    {
      "id": 1,
      "username": "juan.perez",
      "email": "juan.perez@example.com",
      "first_name": "Juan",
      "last_name": "Pérez García",
      "enabled": true,
      "roles": ["USER"]
    },
    {
      "id": 2,
      "username": "admin",
      "email": "admin@example.com",
      "first_name": "Admin",
      "last_name": "User",
      "enabled": true,
      "roles": ["ADMIN"]
    }
  ],
  "timestamp": "2024-11-14T10:32:00.111111",
  "status": 200
}
```

---

### 4. ASIGNAR ROL A USUARIO

```bash
curl -X POST http://localhost:8081/auth/api/v1/auth/users/1/roles/ADMIN
```

**Respuesta (200 OK):**
```json
{
  "code": "SUCCESS",
  "message": "Role assigned",
  "data": {
    "id": 1,
    "username": "juan.perez",
    "email": "juan.perez@example.com",
    "first_name": "Juan",
    "last_name": "Pérez García",
    "enabled": true,
    "roles": ["USER", "ADMIN"]
  },
  "timestamp": "2024-11-14T10:33:00.222222",
  "status": 200
}
```

---

## 👤 User Service API

### Base URL: `http://localhost:8082/users/api/v1/profiles`

**Nota:** Estos endpoints requieren autenticación (Bearer token en header)

---

### 1. CREAR PERFIL DE USUARIO

```bash
curl -X POST http://localhost:8082/users/api/v1/profiles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "userId": 1,
    "bio": "Desarrollador apasionado por las mejores prácticas",
    "avatarUrl": "https://example.com/avatars/juan.jpg"
  }'
```

**Respuesta (201 Created):**
```json
{
  "code": "CREATED",
  "message": "Resource created successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "bio": "Desarrollador apasionado por las mejores prácticas",
    "avatarUrl": "https://example.com/avatars/juan.jpg",
    "phoneNumber": null,
    "country": null,
    "city": null
  },
  "timestamp": "2024-11-14T10:35:00.333333",
  "status": 201
}
```

---

### 2. OBTENER PERFIL DE USUARIO

```bash
curl http://localhost:8082/users/api/v1/profiles/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Respuesta (200 OK):**
```json
{
  "code": "SUCCESS",
  "message": "Profile found",
  "data": {
    "id": 1,
    "userId": 1,
    "bio": "Desarrollador apasionado por las mejores prácticas",
    "avatarUrl": "https://example.com/avatars/juan.jpg",
    "phoneNumber": null,
    "country": null,
    "city": null
  },
  "timestamp": "2024-11-14T10:35:30.444444",
  "status": 200
}
```

---

### 3. ACTUALIZAR PERFIL DE USUARIO

```bash
curl -X PUT http://localhost:8082/users/api/v1/profiles/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "userId": 1,
    "bio": "Desarrollador senior con 10 años de experiencia",
    "avatarUrl": "https://example.com/avatars/juan-updated.jpg",
    "phoneNumber": "+34 666 777 888",
    "country": "España",
    "city": "Madrid"
  }'
```

**Respuesta (200 OK):**
```json
{
  "code": "SUCCESS",
  "message": "Profile updated",
  "data": {
    "id": 1,
    "userId": 1,
    "bio": "Desarrollador senior con 10 años de experiencia",
    "avatarUrl": "https://example.com/avatars/juan-updated.jpg",
    "phoneNumber": "+34 666 777 888",
    "country": "España",
    "city": "Madrid"
  },
  "timestamp": "2024-11-14T10:36:00.555555",
  "status": 200
}
```

---

### 4. OBTENER TODOS LOS PERFILES (Admin)

```bash
curl http://localhost:8082/users/api/v1/profiles \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Respuesta (200 OK):**
```json
{
  "code": "SUCCESS",
  "message": "Profiles retrieved",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "bio": "Desarrollador senior",
      "avatarUrl": "https://example.com/avatars/juan.jpg",
      "phoneNumber": "+34 666 777 888",
      "country": "España",
      "city": "Madrid"
    },
    {
      "id": 2,
      "userId": 2,
      "bio": "QA Engineer",
      "avatarUrl": "https://example.com/avatars/maria.jpg",
      "phoneNumber": "+34 666 999 111",
      "country": "España",
      "city": "Barcelona"
    }
  ],
  "timestamp": "2024-11-14T10:37:00.666666",
  "status": 200
}
```

---

## 🌐 Gateway Routes

El API Gateway rutea automáticamente las solicitudes:

```
POST   /auth/**    → Auth Service (8081)
GET    /auth/**    → Auth Service (8081)

POST   /users/**   → User Service (8082)
GET    /users/**   → User Service (8082)
PUT    /users/**   → User Service (8082)

/ui/**             → Web UI (8083)
/                  → Web UI (8083)
```

---

## 🧪 Testing con curl en Bash Script

```bash
#!/bin/bash

# Variables
AUTH_URL="http://localhost:8081/auth/api/v1/auth"
USER_URL="http://localhost:8082/users/api/v1/profiles"
CONTENT_TYPE="Content-Type: application/json"

# 1. Registrar usuario
echo "=== Registrando usuario ==="
RESPONSE=$(curl -s -X POST $AUTH_URL/register \
  -H "$CONTENT_TYPE" \
  -d '{
    "username": "test.user",
    "email": "test@example.com",
    "password": "Test@123",
    "firstName": "Test",
    "lastName": "User"
  }')
echo $RESPONSE | jq '.'

# Extraer ID del usuario (simplificado)
USER_ID=1

# 2. Obtener usuario
echo -e "\n=== Obteniendo usuario ==="
curl -s $AUTH_URL/users/test.user | jq '.'

# 3. Asignar rol ADMIN
echo -e "\n=== Asignando rol ADMIN ==="
curl -s -X POST $AUTH_URL/users/$USER_ID/roles/ADMIN | jq '.'

# 4. Crear perfil
echo -e "\n=== Creando perfil ==="
curl -s -X POST $USER_URL \
  -H "$CONTENT_TYPE" \
  -d "{
    \"userId\": $USER_ID,
    \"bio\": \"Test user profile\",
    \"avatarUrl\": \"https://example.com/avatar.jpg\"
  }" | jq '.'

# 5. Obtener perfil
echo -e "\n=== Obteniendo perfil ==="
curl -s $USER_URL/$USER_ID | jq '.'

# 6. Actualizar perfil
echo -e "\n=== Actualizando perfil ==="
curl -s -X PUT $USER_URL/$USER_ID \
  -H "$CONTENT_TYPE" \
  -d "{
    \"userId\": $USER_ID,
    \"bio\": \"Updated bio\",
    \"avatarUrl\": \"https://example.com/avatar-updated.jpg\",
    \"phoneNumber\": \"+34666777888\",
    \"country\": \"España\",
    \"city\": \"Madrid\"
  }" | jq '.'

echo -e "\n=== Testing completado ==="
```

Guardar como `test-api.sh` y ejecutar:
```bash
chmod +x test-api.sh
./test-api.sh
```

---

## 📊 Códigos de Error

### 200 OK
Solicitud exitosa

### 201 Created
Recurso creado exitosamente

### 400 Bad Request
```json
{
  "code": "ERROR",
  "message": "Username already exists",
  "status": 400
}
```

### 404 Not Found
```json
{
  "code": "ERROR",
  "message": "User not found",
  "status": 404
}
```

### 500 Internal Server Error
```json
{
  "code": "ERROR",
  "message": "Error retrieving users",
  "status": 500
}
```

---

## 🔗 URLs Útiles

- **Gateway**: http://localhost:8080
- **Auth Service**: http://localhost:8081/auth
- **User Service**: http://localhost:8082/users
- **Web UI**: http://localhost:8083/ui

### Documentation

- **Swagger Auth**: http://localhost:8081/auth/swagger-ui.html
- **Swagger User**: http://localhost:8082/users/swagger-ui.html
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000

---

## 📝 Notas

- Los tokens JWT deben incluirse en el header `Authorization: Bearer <token>`
- Las contraseñas deben tener al menos 6 caracteres (mejor con mayúsculas, números, caracteres especiales)
- Los IDs son autonuméricos de BD
- El caché de Redis almacena usuarios por 30 minutos
- Los roles disponibles: USER, ADMIN (añadir más según necesidad)

