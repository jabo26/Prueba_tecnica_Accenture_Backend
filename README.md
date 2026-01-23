# Prueba_tecnica_Accenture_Backend

# API REST de Gestión de Franquicias

API REST desarrollada con Spring Boot WebFlux para la gestión de franquicias, sucursales y productos con arquitectura reactiva.

## Descripción

Sistema backend que permite administrar franquicias y sus sucursales, así como el inventario de productos en cada ubicación. La aplicación está construida siguiendo los principios de Clean Architecture y programación reactiva.

## Características Principales

- API REST completamente reactiva utilizando Spring WebFlux
- Gestión de franquicias con nombres únicos a nivel global
- Gestión de sucursales con nombres únicos por franquicia
- Gestión de productos con control de stock
- Consulta del producto con mayor stock por sucursal en una franquicia
- Actualización de nombres de entidades
- Validaciones de unicidad y restricciones de negocio
- Manejo centralizado de excepciones
- Persistencia reactiva con R2DBC
- Migraciones de base de datos con Flyway
- Despliegue en nube con base de datos MySQL gestionada

## Tecnologías Utilizadas

### Backend
- Java 17
- Spring Boot 3.2.1
- Spring WebFlux (Programación Reactiva)
- Spring Data R2DBC
- MySQL 8.0
- Flyway para migraciones
- Lombok
- Maven

### Base de Datos
- MySQL 8.0 en Railway (producción)
- R2DBC MySQL Driver para acceso reactivo
- JDBC MySQL Driver para Flyway

### Infraestructura
- Railway.app para hosting
- Docker para contenerización
- Docker Compose para desarrollo local

## Arquitectura

El proyecto implementa Clean Architecture dividida en tres capas principales:

### Capa de Dominio
Contiene la lógica de negocio independiente del framework.
- **Modelos**: Franquicia, Sucursal, Producto
- **Interfaces de Repositorio**: Contratos de persistencia
- **Excepciones de Dominio**: DominioException, EntidadNoEncontradaException, NombreDuplicadoException

### Capa de Aplicación
Contiene los casos de uso y lógica de aplicación:
- **DTOs**: Request y Response para cada entidad
- **Servicios**: FranquiciaService, SucursalService, ProductoService
- Validaciones de negocio y orquestación de operaciones

### Capa de Infraestructura
Implementaciones concretas de frameworks y herramientas:
- **Repositorios R2DBC**: Implementación reactiva de persistencia
- **Controllers REST**: Exposición de endpoints HTTP
- **Manejo de Errores**: GlobalExceptionHandler para respuestas estandarizadas

## Modelo de Datos

### Franquicia
- id (Long, generado automáticamente)
- nombre (String, único a nivel global)
- fechaCreacion (LocalDateTime)
- fechaActualizacion (LocalDateTime)

### Sucursal
- id (Long, generado automáticamente)
- nombre (String, único por franquicia)
- franquiciaId (Long, FK a Franquicia)
- fechaCreacion (LocalDateTime)
- fechaActualizacion (LocalDateTime)

### Producto
- id (Long, generado automáticamente)
- nombre (String, único por sucursal)
- stock (Integer)
- sucursalId (Long, FK a Sucursal)
- fechaCreacion (LocalDateTime)
- fechaActualizacion (LocalDateTime)

## API Endpoints

### URL Base
- **Producción**: `https://pruebatecnicaaccenturebackend-production.up.railway.app`
- **Local**: `http://localhost:8080`

### Franquicias

#### Crear Franquicia
```
POST /api/franquicias
Content-Type: application/json

{
  "nombre": "McDonald's"
}
```

#### Listar Todas las Franquicias
```
GET /api/franquicias
```

#### Buscar Franquicia por ID
```
GET /api/franquicias/{id}
```

#### Actualizar Nombre de Franquicia
```
PUT /api/franquicias/{id}/nombre
Content-Type: application/json

{
  "nombre": "Ecomoda"
}
```

### Sucursales

#### Crear Sucursal
```
POST /api/sucursales
Content-Type: application/json

{
  "nombre": "Sucursal Centro",
  "franquiciaId": 1
}
```

#### Listar Sucursales de una Franquicia
```
GET /api/sucursales/franquicia/{franquiciaId}
```

#### Listar Todas las Sucursales
```
GET /api/sucursales
```

#### Buscar Sucursal por ID
```
GET /api/sucursales/{id}
```

#### Actualizar Nombre de Sucursal
```
PUT /api/sucursales/{id}/nombre
Content-Type: application/json

{
  "nombre": "Sucursal Centro Histórico"
}
```

### Productos

#### Crear Producto
```
POST /api/productos
Content-Type: application/json

{
  "nombre": "Big Mac",
  "stock": 50,
  "sucursalId": 1
}
```

#### Listar Productos de una Sucursal
```
GET /api/productos/sucursal/{sucursalId}
```

#### Actualizar Nombre de Producto
```
PUT /api/productos/{id}/nombre
Content-Type: application/json

{
  "nombre": "Big Mac Deluxe"
}
```

#### Actualizar Stock de Producto
```
PUT /api/productos/{id}/stock
Content-Type: application/json

{
  "stock": 75
}
```

#### Eliminar Producto
```
DELETE /api/productos/{id}
```

#### Obtener Producto con Mayor Stock por Sucursal
```
GET /api/productos/mas-stock/franquicia/{franquiciaId}
```

Retorna una lista con el producto de mayor stock de cada sucursal dentro de la franquicia especificada.

## Respuestas de Error

La API utiliza códigos de estado HTTP estándar y retorna errores en formato JSON:

```json
{
  "timestamp": "2026-01-23T10:30:00.123456789",
  "status": 404,
  "error": "Entidad no encontrada",
  "message": "Franquicia con id 999 no encontrada"
}
```

### Códigos de Estado
- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `204 No Content`: Operación exitosa sin contenido de respuesta
- `400 Bad Request`: Error de validación en los datos enviados
- `404 Not Found`: Recurso no encontrado
- `409 Conflict`: Conflicto de unicidad (nombre duplicado)
- `500 Internal Server Error`: Error interno del servidor

## Instalación y Configuración

### Requisitos Previos
- Java 17 o superior
- Maven 3.9 o superior
- Docker y Docker Compose (para desarrollo local)

### Configuración Local

1. Clonar el repositorio:
```bash
git clone https://github.com/jabo26/Prueba_tecnica_Accenture_Backend.git
cd PruebaTecnicaAccenture
```

2. Iniciar la base de datos MySQL con Docker Compose:
```bash
docker-compose up -d
```

3. Compilar el proyecto:
```bash
mvn clean install
```

4. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

### Variables de Entorno

Para producción, configurar las siguientes variables:

- `DATABASE_URL`: URL de conexión R2DBC a MySQL
- `JDBC_DATABASE_URL`: URL de conexión JDBC para Flyway
- `DB_USERNAME`: Usuario de base de datos
- `DB_PASSWORD`: Contraseña de base de datos
- `PORT`: Puerto de la aplicación (default: 8080)

## Pruebas

### Colección de Postman

Se incluye una colección de Postman con todos los endpoints documentados:
- `Franquicias_API_Local.postman_collection.json` para pruebas locales
- `Franquicias_API_Railway.postman_collection.json` para pruebas en producción

### Pruebas Manuales

#### Flujo de Prueba Completo

1. Crear una franquicia:
```bash
curl -X POST https://pruebatecnicaaccenturebackend-production.up.railway.app/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"McDonald'"'"'s"}'
```

2. Crear una sucursal:
```bash
curl -X POST https://pruebatecnicaaccenturebackend-production.up.railway.app/api/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Sucursal Centro","franquiciaId":1}'
```

3. Crear productos:
```bash
curl -X POST https://pruebatecnicaaccenturebackend-production.up.railway.app/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Big Mac","stock":50,"sucursalId":1}'
```

4. Consultar producto con mayor stock:
```bash
curl https://pruebatecnicaaccenturebackend-production.up.railway.app/api/productos/mas-stock/franquicia/1
```

## Despliegue

### Railway

La aplicación está desplegada en Railway.app:
- URL: `https://pruebatecnicaaccenturebackend-production.up.railway.app`
- Base de datos: MySQL 8.0 gestionada por Railway
- Región: US East (Virginia)

### Docker

Para construir y ejecutar la imagen Docker localmente:

```bash
# Construir imagen
docker build -t franquicias-api .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e DATABASE_URL="r2dbc:mysql://host:3306/db" \
  -e JDBC_DATABASE_URL="jdbc:mysql://host:3306/db" \
  -e DB_USERNAME="user" \
  -e DB_PASSWORD="pass" \
  franquicias-api
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/accenture/franquicias/
│   │   ├── aplicacion/
│   │   │   ├── dto/              
│   │   │   └── service/          
│   │   ├── domain/
│   │   │   ├── exception/       
│   │   │   ├── model/           
│   │   │   └── repository/       # Interfaces de repositorio
│   │   ├── infraestructura/
│   │   │   ├── repository/       # Implementaciones R2DBC
│   │   │   └── web/            
│   │   └── FranquiciasApiApplication.java
│   └── resources/
│       ├── application.yml      
│       └── db/migration/         # Scripts Flyway
└── test/                         # Tests unitarios e integración
```

## Decisiones Técnicas

### Programación Reactiva
Se eligió Spring WebFlux y R2DBC para implementar programación reactiva end-to-end. Esto proporciona:
- Mayor eficiencia en el uso de recursos
- Mejor manejo de concurrencia
- Escalabilidad mejorada para operaciones I/O

### Clean Architecture
La separación en capas permite:
- Independencia de frameworks
- Facilidad para testing
- Mantenibilidad a largo plazo
- Claridad en las responsabilidades

### Flyway para Migraciones
Se utiliza Flyway (JDBC) para migraciones ya que R2DBC aún no soporta completamente herramientas de migración. Esto garantiza:
- Control de versiones del esquema
- Reproducibilidad de ambientes
- Trazabilidad de cambios

### Validaciones de Unicidad
Las validaciones de nombres únicos se implementan a nivel de aplicación y base de datos:
- Constraints únicos en la base de datos
- Validación en servicios para mensajes de error claros
- Uso de índices para optimizar las consultas de existencia

## Limitaciones Conocidas

- Las migraciones Flyway requieren conexión JDBC (no reactiva)
- La red privada de Railway requiere servicios en el mismo proyecto
- R2DBC tiene soporte limitado para algunas operaciones avanzadas de MySQL


## Autor

Jair de Jesús Bovea Fontalvo
- GitHub: [jabo26](https://github.com/jabo26)
- Email: jairbovea@gmail.com

## Licencia

Este proyecto fue desarrollado como prueba técnica para Accenture.