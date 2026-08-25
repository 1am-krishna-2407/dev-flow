# DevFlow

DevFlow is a SaaS-grade developer workflow automation platform built with Java Spring Boot, React, PostgreSQL, Redis, and Docker.

## Features

- JWT authentication with scoped roles
- User, project, and task management
- REST API with OpenAPI docs
- PostgreSQL persistence and Redis caching
- Docker Compose ready deployment
- React frontend scaffold with Tailwind UI

## Getting Started

### Backend

```bash
cd backend
./mvnw clean package
java -jar target/devflow-backend-0.1.0.jar
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

### Docker

If Maven or npm are not installed locally, the project can still be built and run using Docker.

```bash
docker-compose up --build
```

## API Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/projects`
- `POST /api/projects`
- `GET /api/tasks`
- `POST /api/tasks`

## Notes

This repository contains a deployable MVP foundation for a centralized developer command center. It is ready to extend with GitHub integration, build analytics, notifications, and real-time features.
