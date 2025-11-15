# INSTRUCCIONES DE INSTALACIÓN Y EJECUCIÓN

## 📋 Requisitos Previos

### Necesarios
- **Java 21** (OpenJDK 21 o superior)
- **Maven 3.9+**
- **Docker Desktop** (incluye Docker y Docker Compose)
- **Git** (opcional, para control de versiones)

### Verificar Instalación
```bash
java -version          # Debe mostrar 21
mvn -version           # Debe mostrar 3.9+
docker --version       # Debe estar instalado
docker-compose --version # Debe estar instalado
```

## 🚀 OPCIÓN 1: Despliegue con Docker Compose (Recomendado)

### Paso 1: Clonar o acceder al proyecto
```bash
cd /Users/rafael/Projects/demo/Development/demo
```

### Paso 2: Compilar el proyecto Maven
```bash
mvn clean package -DskipTests=true
```

Esto va a:
- Descargar todas las dependencias
- Compilar todo el código
- Empaquetar los servicios en JARs
- Durará ~3-5 minutos la primera vez

### Paso 3: Iniciar los servicios
```bash
# Hacer el script ejecutable (una sola vez)
chmod +x deploy.sh stop.sh init-docker-db.sh

# Ejecutar despliegue
./deploy.sh
```

O manualmente:
```bash
docker-compose up -d
```

### Paso 4: Esperar a que todo esté listo
```bash
# Ver logs
docker-compose logs -f

# Cuando veas esto, todo está listo:
# auth-service  | Started AuthServiceApplication
# user-service  | Started UserServiceApplication
# gateway-service | Started GatewayServiceApplication
# web-ui | Started WebUiApplication
```

### Paso 5: Verificar servicios
```bash
# Health check
curl http://localhost:8081/auth/actuator/health
curl http://localhost:8082/users/actuator/health
curl http://localhost:8080/actuator/health
curl http://localhost:8083/ui/actuator/health
```

## 🌐 Acceder a las Aplicaciones

Una vez que todo está corriendo:

### Web UI (Interfaz Principal)
- **URL**: http://localhost:8083/ui
- **Puerto**: 8083
- Interfaz Thymeleaf con:
  - Página de inicio
  - Formulario de registro
  - Formulario de login
  - Dashboard personal

### Auth Service (Servicio de Autenticación)
- **API Base**: http://localhost:8081/auth/api/v1/auth
- **Puerto**: 8081
- **Swagger**: http://localhost:8081/auth/swagger-ui.html
- **Health**: http://localhost:8081/auth/actuator/health

Endpoints principales:
```bash
# Registrar usuario
POST http://localhost:8081/auth/api/v1/auth/register
Content-Type: application/json

{
  "username": "juan",
  "email": "juan@example.com",
  "password": "Seguro123!",
  "firstName": "Juan",
  "lastName": "Pérez"
}

# Obtener usuario
GET http://localhost:8081/auth/api/v1/auth/users/juan

# Asignar rol
POST http://localhost:8081/auth/api/v1/auth/users/1/roles/ADMIN
```

### User Service (Gestión de Perfiles)
- **API Base**: http://localhost:8082/users/api/v1/profiles
- **Puerto**: 8082
- **Swagger**: http://localhost:8082/users/swagger-ui.html
- **Health**: http://localhost:8082/users/actuator/health

### API Gateway
- **URL**: http://localhost:8080
- **Puerto**: 8080
- **Health**: http://localhost:8080/actuator/health

Rutas:
```
/auth/**    → Auth Service (puerto 8081)
/users/**   → User Service (puerto 8082)
/ui/**      → Web UI (puerto 8083)
/           → Web UI
```

### Monitoreo

**Prometheus** (Recopilación de métricas)
- **URL**: http://localhost:9090
- **Métricas disponibles**:
  - Latencia de endpoints
  - Conexiones de BD
  - Uso de memoria
  - Errores HTTP

**Grafana** (Visualización)
- **URL**: http://localhost:3000
- **Credenciales**: admin / admin
- Dashboards pre-configurados para los servicios

## 🔌 Pruebas Manuales

### 1. Registro de usuario
```bash
curl -X POST http://localhost:8081/auth/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test@123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### 2. Obtener usuario registrado
```bash
curl http://localhost:8081/auth/api/v1/auth/users/testuser
```

### 3. Crear perfil de usuario
```bash
curl -X POST http://localhost:8082/users/api/v1/profiles \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "bio": "Soy un usuario de prueba",
    "avatarUrl": "https://example.com/avatar.jpg"
  }'
```

### 4. Obtener perfil
```bash
curl http://localhost:8082/users/api/v1/profiles/1
```

## 🐳 Gestión de Docker

### Ver estado de servicios
```bash
docker-compose ps
```

### Ver logs en tiempo real
```bash
docker-compose logs -f                    # Todos los servicios
docker-compose logs -f auth-service       # Solo auth-service
docker-compose logs -f user-service       # Solo user-service
```

### Detener servicios (sin eliminar datos)
```bash
docker-compose stop
```

### Reiniciar servicios
```bash
docker-compose start
```

### Limpiar todo (eliminar contenedores y volúmenes)
```bash
docker-compose down -v

# O usar el script
./stop.sh
```

### Ver volúmenes y datos
```bash
docker volume ls
docker volume inspect demo_postgres_data    # Ver datos de BD
docker volume inspect demo_redis_data       # Ver datos de Redis
```

## 🛠️ OPCIÓN 2: Desarrollo Local (Sin Docker)

Si prefieres ejecutar servicios localmente:

### Paso 1: Instalar PostgreSQL y Redis localmente
```bash
# macOS con Homebrew
brew install postgresql redis

# Iniciar servicios
brew services start postgresql
brew services start redis
```

### Paso 2: Crear bases de datos
```bash
createdb auth_db
createdb user_db

# O usar el script
./init-db.sh
```

### Paso 3: Compilar
```bash
mvn clean package -DskipTests=true
```

### Paso 4: Ejecutar cada servicio en terminal diferente

**Terminal 1 - Auth Service:**
```bash
cd auth-service
mvn spring-boot:run
```

**Terminal 2 - User Service:**
```bash
cd user-service
mvn spring-boot:run
```

**Terminal 3 - Gateway Service:**
```bash
cd gateway-service
mvn spring-boot:run
```

**Terminal 4 - Web UI:**
```bash
cd web-ui
mvn spring-boot:run
```

## 📊 Monitoreo

### Métricas en Prometheus
```bash
# Acceder a Prometheus
http://localhost:9090

# Queries útiles
- http_requests_total
- http_request_duration_seconds_bucket
- jvm_memory_used_bytes
- db_connection_pool_size
```

### Dashboards Grafana
```bash
http://localhost:3000
Usuario: admin
Contraseña: admin
```

## 🧪 Ejecutar Tests

```bash
# Tests unitarios
mvn test

# Tests de un módulo específico
mvn test -pl auth-service

# Skip tests (durante compilación)
mvn clean package -DskipTests=true

# Coverage
mvn clean test jacoco:report
```

## 🔍 Troubleshooting

### Error: Puerto ya en uso
```bash
# Encontrar qué proceso usa el puerto
lsof -i :8081  # Para auth-service
lsof -i :8082  # Para user-service
lsof -i :8080  # Para gateway

# Matar el proceso
kill -9 <PID>
```

### Error: Cannot connect to PostgreSQL
```bash
# Verificar que PostgreSQL está corriendo
docker ps | grep postgres

# Verificar conectividad
docker-compose exec postgres pg_isready

# Reiniciar
docker-compose restart postgres
```

### Error: Cannot connect to Redis
```bash
# Verificar que Redis está corriendo
docker ps | grep redis

# Verificar conectividad
docker-compose exec redis redis-cli ping
```

### Error: Out of Memory
```bash
# Aumentar memoria en docker-compose.yml
# En la sección de servicios, añadir:
# memory: 1g
```

### Logs mostrando errores de compilación
```bash
# Limpiar y reconstruir
mvn clean install -DskipTests=true

# Verificar dependencias
mvn dependency:tree
```

## 📚 Documentación Adicional

- **README.md**: Descripción general del proyecto
- **Architecture**: Documentación de arquitectura hexagonal
- **API Docs**: OpenAPI/Swagger disponible en cada servicio
- **Performance**: Tips de optimización en configuración

## ✅ Verificación Final

Para verificar que todo funciona correctamente:

```bash
# 1. Verificar todos los servicios en docker
docker-compose ps
# Deberías ver 6 servicios activos (auth, user, gateway, web-ui, postgres, redis)

# 2. Verificar health checks
curl http://localhost:8081/auth/actuator/health
curl http://localhost:8082/users/actuator/health
curl http://localhost:8080/actuator/health
curl http://localhost:8083/ui/actuator/health

# 3. Acceder a Web UI
# Abre http://localhost:8083/ui en tu navegador
# Deberías ver la página de inicio

# 4. Registrar un usuario
# Haz clic en "Registrarse"
# Completa el formulario
# Deberías ser redirigido a login

# 5. Ver métricas
# Abre http://localhost:9090
# Deberías ver datos de Prometheus
```

## 🎉 ¡Listo!

Si ves todos los servicios corriendo y puedes acceder a http://localhost:8083/ui, ¡el proyecto está completamente funcional!

---

## 📞 Soporte

Si tienes problemas:
1. Revisa los logs: `docker-compose logs`
2. Verifica requisitos: Java 21, Maven 3.9+, Docker
3. Limpia y reconstruye: `mvn clean package && docker-compose down -v && docker-compose up -d`
