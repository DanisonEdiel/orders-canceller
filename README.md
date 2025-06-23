# Orders Canceler Service

This service allows order cancellation through a "soft delete", changing the order status to "cancelled" and recording the cancellation date and reason.

## Features

- Order cancellation through status change (soft delete)
- Cancellation date and reason recording
- JWT Authentication
- OpenAPI/Swagger API Documentation

## Requirements

- Java 21
- PostgreSQL 14
- Docker

## Configuration

### Environment Variables

```
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=jdbc:postgresql://<rds-endpoint>:5432/orders_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=<secure-password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update

AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=<your-access-key>
AWS_SECRET_ACCESS_KEY=<your-secret-key>

SNS_TOPIC_ARN=arn:aws:sns:us-east-1:<account-id>:orders-topic
CLOUDWATCH_LOG_GROUP=/aws/lambda/orders-canceler

SERVER_PORT=8080
```

## API Endpoints

### Cancel Order

```
PUT /api/orders/{orderId}/cancel
```

Request Body:
```json
{
  "cancellationReason": "Reason for cancellation"
}
```

## Deployment

The service is automatically deployed to EC2 via GitHub Actions when a push is made to the main branch.

### Local Deployment with Docker

```bash
docker build -t orders-canceler .
```

### Production Deployment

The service uses GitHub Actions for CI/CD pipeline and is deployed to AWS EC2. The deployment process includes:

1. Build and test the application
2. Create Docker image
3. Push Docker image to Docker Hub
4. Deploy to AWS EC2

## Security

The application uses:
- JWT for authentication
- HTTPS for secure communication
- Environment variables for sensitive configuration
- AWS IAM roles for AWS service access

## Monitoring

The service includes:
- Spring Boot Actuator for health checks
- CloudWatch integration for logging
- Prometheus metrics collection
- SNS notifications for critical events

## Development

To run locally:

1. Set up environment variables
2. Run `./gradlew bootRun`
3. Access Swagger documentation at `/swagger-ui.html`
docker run -p 8082:8082 --env-file .env orders-canceler
```

### Despliegue con Docker Compose

```bash
docker-compose up -d
