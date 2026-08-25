# DevFlow

DevFlow is a centralized developer workflow automation platform built with Java Spring Boot, React, PostgreSQL, Redis, and Docker.

## Features

- JWT authentication with role-based access control
- User, project, and task management
- Clean architecture with controller, service, repository, and domain layers
- REST APIs with OpenAPI documentation
- Docker and Docker Compose deployment
- React frontend scaffold with Tailwind CSS

## Getting Started

### Backend

1. Navigate to `backend`
2. Run `mvn clean package`
3. Run `java -jar target/devflow-backend-0.1.0.jar`

### Frontend

1. Navigate to `frontend`
2. Run `npm install`
3. Run `npm run dev`

### Docker

Run `docker-compose up --build`

## API Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/projects`
- `POST /api/projects`
- `GET /api/tasks`
- `POST /api/tasks`

## Notes

This project is an MVP foundation for a SaaS platform and can be extended with GitHub integration, build monitoring, notifications, search, analytics, and real-time WebSockets.
