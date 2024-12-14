# Onboarding Service with RabbitMQ and Docker Compose

This project provides an API for onboarding users and interacts with a RabbitMQ instance running locally via Docker Compose.

## Prerequisites

- Java 17
- Docker
- Docker Compose

## Setup and Running Locally

### Step 1: Start RabbitMQ with Docker Compose

To run RabbitMQ locally, we are using Docker Compose. This will set up RabbitMQ on your machine for local development.

1. **Clone the repository**:
2. **Run docker-compose -f compose.yaml up**

## Step 2: Onboard User Request

Once RabbitMQ is up and running, you can onboard users via the `/user/onboard` API endpoint.

### API Endpoint

- **URL**: `http://localhost:8080/user/onboard`
- **Method**: `POST`

### Request Body (JSON)

Use the following JSON structure to send a user onboarding request:

```json
{
  "userId": "ABC123",
  "userName": "ABC",
  "email": "abc@gmail.com"
}
```

### Curl Command
```
curl --location 'http://localhost:8080/user/onboard' \
--header 'X-ACI-App-Correlation-ID: <your-correlation-id>' \
--header 'Content-Type: application/json' \
--data-raw '{
"userId": "ABC123",
"userName": "ABC",
"email": "abc@gmail.com"
}'
```


