# OAuth 2.0 Microservicios - Proyecto Enterprise

## 📋 Descripción

Plataforma completa de microservicios con OAuth 2.0 implementando mejores prácticas de desarrollo enterprise:

### 🏗️ Arquitectura Hexagonal
- Separación clara entre capas (Domain, Application, Infrastructure)
- Puertos y Adaptadores (Hexagonal Ports & Adapters)
- Domain-Driven Design (DDD)
- Inversión de dependencias

### 🔐 OAuth 2.0
- Authorization Server completo
- Resource Server para proteger APIs
- JWT con RSA
- Refresh Tokens
- Manejo de roles y permisos

### 📊 Componentes
- **auth-service**: Servicio de autenticación y autorización (Puerto 8081)
- **user-service**: Gestión de perfiles de usuarios (Puerto 8082)
- **gateway-service**: API Gateway con Spring Cloud Gateway (Puerto 8080)
- **web-ui**: Interfaz gráfica con Spring Thymeleaf (Puerto 8083)
- **common-lib**: Librería compartida

### 🚀 Tecnologías
- Java 21 con Spring Boot 3.3
- Spring Cloud Gateway
- Spring Security 6
- Spring Data JPA
- PostgreSQL 16
- Redis 7
- Docker & Docker Compose
- Maven 3.9
- Prometheus & Grafana

### ⚡ Características
- Caching distribuido con Redis
- Conexión pooling con HikariCP (20 conexiones máx)
- Manejo de errores funcional con Vavr
- Mapeo de entidades con MapStruct
- Monitoreo con Micrometer y Prometheus
- Logs estructurados
- Health checks
- API Documentation OpenAPI 3.0
- Soporte para +1000 usuarios concurrentes

## 🚀 Inicio Rápido

### Prerequisitos
- Java 21 instalado
- Maven 3.9+
- Docker y Docker Compose
- PostgreSQL 16 (o usar Docker)
- Redis 7 (o usar Docker)

### Desarrollo Local

```bash
# 1. Compilar el proyecto
mvn clean package -DskipTests=true

# 2. Iniciar bases de datos (si no usa Docker)
./init-db.sh

# 3. Iniciar servicios con Docker Compose
docker-compose up -d

# 4. Acceder a las aplicaciones
- Web UI: http://localhost:8083/ui
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)
```

### Despliegue Automatizado

```bash
# Hacer script ejecutable
chmod +x deploy.sh stop.sh init-db.sh

# Desplegar
./deploy.sh

# Detener
./stop.sh
```

## 📚 Estructura del Proyecto

```
├── auth-service/                 # Servicio de autenticación
│   ├── src/main/java/com/microservices/auth
│   │   ├── domain/               # Entidades y puertos
│   │   ├── application/          # Casos de uso y DTOs
│   │   └── infrastructure/       # Adaptadores y configuración
│   └── pom.xml
├── user-service/                 # Servicio de usuarios
│   └── (estructura similar)
├── gateway-service/              # API Gateway
├── web-ui/                        # Interfaz web
├── common-lib/                    # Librería compartida
├── docker-compose.yml            # Orquestación de contenedores
├── .docker/
│   ├── Dockerfile.auth           # Imagen auth-service
│   ├── Dockerfile.user           # Imagen user-service
│   ├── Dockerfile.gateway        # Imagen gateway
│   ├── Dockerfile.webui          # Imagen web-ui
│   └── prometheus.yml            # Configuración de métricas
└── pom.xml                        # POM padre
```

## 🔌 Arquitectura Hexagonal

### Puertos (Interfaces)
```
Domain Layer
├── UserRepositoryPort (Puerto de persistencia)
└── AuthenticationUseCase (Puerto de casos de uso)

Infrastructure Layer
├── UserRepositoryAdapter (Adaptador JPA)
└── AuthenticationController (Adaptador REST)
```

### Flujo de una Solicitud
```
HTTP Request
    ↓
AuthenticationController (Adaptador Web)
    ↓
AuthenticationService (Caso de Uso)
    ↓
UserEntity (Modelo de Dominio)
    ↓
UserRepositoryPort (Puerto)
    ↓
UserRepositoryAdapter (Adaptador JPA)
    ↓
Base de Datos
```

## 🔒 Seguridad OAuth 2.0

### Flujo de Autenticación
1. Usuario se registra en `/auth/api/v1/auth/register`
2. Credenciales se validan y guardan en PostgreSQL
3. JWT se genera con roles incluidos
4. Cliente accede a recursos protegidos con JWT
5. Resource Server valida JWT

### Configuración de Seguridad
```properties
# Auth Service - Authorization Server
spring.security.user.name=admin
spring.security.user.password=admin123

# User Service - Resource Server
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081/auth
```

## 📊 Monitoreo

### Métricas Disponibles
- Latencia de endpoints
- Número de conexiones activas
- Uso de memoria y CPU
- Errores HTTP
- Tiempo de respuesta de BD

### URLs de Monitoreo
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000
- Health Auth: http://localhost:8081/auth/actuator/health
- Métricas Auth: http://localhost:8081/auth/actuator/metrics

## 🎯 Mejores Prácticas Implementadas

### 1. Arquitectura Limpia
- Separación de responsabilidades
- Inversión de dependencias
- Inyección de dependencias
- Casos de uso independientes

### 2. Patrones de Diseño
- Hexagonal Architecture
- Repository Pattern
- Adapter Pattern
- Dependency Injection

### 3. Functional Programming
- Uso de Vavr (Option, Either)
- Manejo funcional de errores
- Composición de funciones

### 4. Performance
- Connection pooling (HikariCP)
- Caching con Redis
- Índices en BD
- Lazy loading

### 5. Observabilidad
- Logging estructurado
- Health checks
- Métricas Prometheus
- Trazas distribuidas

## 🧪 Testing

```bash
# Tests unitarios
mvn test

# Tests de integración con TestContainers
mvn test -Dgroups=integration

# Coverage
mvn jacoco:report
```

## 📖 API Documentation

### OpenAPI/Swagger
- Auth Service: http://localhost:8081/auth/swagger-ui.html
- User Service: http://localhost:8082/users/swagger-ui.html

## 🔧 Configuración

### Variables de Entorno
```bash
# PostgreSQL
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/auth_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Redis
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379

# OAuth2
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://auth-service:8081/auth
```

### Tuning de Performance
```properties
# Pool de conexiones
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# Batch processing
spring.jpa.properties.hibernate.jdbc.batch_size=20

# Cache
spring.cache.redis.time-to-live=1800000

# JVM
-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

## 🚢 Despliegue en Producción

### Requisitos
- Kubernetes o Docker Swarm
- Load Balancer
- PostgreSQL Managed (RDS/Azure Database)
- Redis Managed (ElastiCache/Azure Cache)
- Container Registry

### Pasos
1. Construir imágenes
2. Push a registry
3. Configurar manifiestos K8s
4. Desplegar servicios
5. Configurar ingress/load balancer
6. Monitoreo con Prometheus/Grafana

## 📝 Ejemplo de Uso

### 1. Registrar usuario
```bash
curl -X POST http://localhost:8081/auth/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "email": "juan@example.com",
    "password": "secure123",
    "firstName": "Juan",
    "lastName": "Pérez"
  }'
```

### 2. Crear perfil
```bash
curl -X POST http://localhost:8082/users/api/v1/profiles \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "bio": "Developer passionate about clean code",
    "avatarUrl": "https://example.com/avatar.jpg"
  }'
```

### 3. Obtener perfil
```bash
curl http://localhost:8082/users/api/v1/profiles/1 \
  -H "Authorization: Bearer <token>"
```

## 🤝 Contribuciones

Para contribuir:
1. Fork el proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📄 Licencia

Este proyecto está bajo licencia MIT.

## 📞 Soporte

Para preguntas o problemas, crear un issue en el repositorio.

---

**Desarrollado con ❤️ siguiendo mejores prácticas de software enterprise**
