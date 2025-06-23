# Orders Canceler Service

Este servicio permite la cancelación de órdenes mediante un "soft delete", cambiando el estado de la orden a "cancelled" y registrando la fecha y motivo de cancelación.

## Características

- Cancelación de órdenes mediante cambio de estado (soft delete)
- Registro de fecha y motivo de cancelación
- Autenticación mediante JWT
- Documentación API con OpenAPI/Swagger

## Requisitos

- Java 17
- PostgreSQL

## Configuración

### Variables de entorno

```
DB_URL=jdbc:postgresql://host:port/database
DB_USERNAME=username
DB_PASSWORD=password
AUTH_SERVICE_URL=http://auth-service-url
JWT_SECRET=your_jwt_secret
```

## Endpoints

### Cancelar orden

```
PUT /api/orders/{orderId}/cancel
```

Payload:
```json
{
  "cancellationReason": "Motivo de la cancelación"
}
```

## Despliegue

El servicio se despliega automáticamente en EC2 mediante GitHub Actions cuando se realiza un push a la rama main.

### Despliegue local con Docker

```bash
docker build -t orders-canceler .
docker run -p 8082:8082 --env-file .env orders-canceler
```

### Despliegue con Docker Compose

```bash
docker-compose up -d
