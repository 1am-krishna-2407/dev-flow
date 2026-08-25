# DevFlow - Quick Start Guide

## 🚀 Quick Start (Without Docker)

### Prerequisites (Already Installed)
- Java 21.0.10 LTS ✅
- Maven 3.9.9 ✅
- Node.js v24.14.1 ✅
- npm 11.11.0 ✅
- PostgreSQL 15+ (install separately if needed)
- Redis 7+ (install separately if needed)

---

## Option 1: Run Backend Only (Fastest)

### 1. Start Backend Services
```bash
# Navigate to project
cd c:\Users\edith\OneDrive\Desktop\Project

# Run Spring Boot application (auto-compiles)
mvn -f backend/pom.xml spring-boot:run
```

**Expected Output:**
```
Started DevflowBackendApplication in X.XXX seconds
Tomcat started on port(s): 8080
```

### 2. Test Backend Health
```bash
# In another terminal
curl http://localhost:8080/api/health
# Response: {"status":"UP"}
```

### 3. Test API Endpoints
```bash
# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@devflow.io",
    "password": "TestPassword123"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@devflow.io",
    "password": "TestPassword123"
  }'

# Get projects
curl -X GET http://localhost:8080/api/projects \
  -H "Authorization: Bearer <token-from-login>"
```

---

## Option 2: Run Frontend Only (For UI Development)

### 1. Install Dependencies (if not done)
```bash
cd c:\Users\edith\OneDrive\Desktop\Project\frontend
npm install
```

### 2. Start Development Server
```bash
npm run dev
```

**Expected Output:**
```
VITE v8.0.16 ready in XXX ms
➜  Local:   http://localhost:5173/
```

### 3. Open in Browser
- Navigate to `http://localhost:5173`
- View development dashboard with Tailwind CSS styling

---

## Option 3: Full Stack Local Development

### Prerequisites
You'll need to set up PostgreSQL and Redis locally:

#### PostgreSQL Setup
```bash
# Windows - using PostgreSQL installer
# 1. Download from https://www.postgresql.org/download/windows/
# 2. Install with:
#    - Port: 5432
#    - Database: devflow
#    - User: postgres
#    - Password: (your choice)
# 3. Create database:
psql -U postgres -c "CREATE DATABASE devflow;"
```

#### Redis Setup
```bash
# Windows - using WSL2 or native Windows Redis
# Option A: Via Chocolatey
choco install redis-64

# Option B: Download from https://github.com/microsoftarchive/redis/releases

# Start Redis
redis-server.exe
```

### Start Full Stack

#### Terminal 1: Backend
```bash
cd c:\Users\edith\OneDrive\Desktop\Project
mvn -f backend/pom.xml spring-boot:run
```

#### Terminal 2: Frontend
```bash
cd c:\Users\edith\OneDrive\Desktop\Project\frontend
npm run dev
```

#### Terminal 3: PostgreSQL (if not as service)
```bash
psql -U postgres
```

#### Terminal 4: Redis (if not running as service)
```bash
redis-server.exe
```

### Access Application
- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/api/*
- **Health Check**: http://localhost:8080/api/health

---

## 🐳 Docker Deployment (When Ready)

### Install Docker Desktop
1. Download from https://www.docker.com/products/docker-desktop
2. Install and restart system
3. Verify: `docker --version && docker-compose --version`

### Build & Run with Docker
```bash
# Navigate to project
cd c:\Users\edith\OneDrive\Desktop\Project

# Build all services
docker-compose build

# Start all services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Access Docker Services
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api/*
- **PostgreSQL**: localhost:5432
- **Redis**: localhost:6379

---

## 📦 Available Commands

### Backend Commands
```bash
# Compile only
mvn -f backend/pom.xml clean compile

# Run tests
mvn -f backend/pom.xml test

# Package as JAR
mvn -f backend/pom.xml package

# Run with Spring Boot plugin
mvn -f backend/pom.xml spring-boot:run

# Generate documentation
mvn -f backend/pom.xml javadoc:javadoc
```

### Frontend Commands
```bash
# Install dependencies
npm install

# Run development server (http://localhost:5173)
npm run dev

# Build production bundle
npm run build

# Preview production build locally
npm run preview

# Type check
npx tsc --noEmit

# Lint with ESLint
npm run lint
```

### Docker Commands
```bash
# Build images
docker-compose build

# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f <service>

# Execute command in running container
docker exec -it devflow-backend bash
docker exec -it devflow-frontend sh

# Remove all images and volumes
docker-compose down --volumes --rmi all
```

---

## 🔧 Configuration

### Backend Configuration
Edit `backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/devflow
    username: postgres
    password: your-password
  jpa:
    hibernate:
      ddl-auto: update
  data:
    redis:
      host: localhost
      port: 6379
  security:
    jwt:
      secret: your-jwt-secret-key
      expiration: 900000  # 15 minutes
```

### Frontend Configuration
Edit `frontend/vite.config.ts`:
```typescript
export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
})
```

---

## 🐛 Troubleshooting

### Backend Port Already in Use
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill process (replace PID)
taskkill /PID <PID> /F

# Or use different port
mvn -f backend/pom.xml spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Frontend Build Issues
```bash
# Clear npm cache
npm cache clean --force

# Reinstall dependencies
rm -r node_modules package-lock.json
npm install

# Clear Vite cache
rm -r frontend/.vite
npm run dev
```

### Database Connection Failed
```bash
# Verify PostgreSQL is running
psql -U postgres -c "SELECT version();"

# Check database exists
psql -U postgres -l | grep devflow

# Create database if missing
psql -U postgres -c "CREATE DATABASE devflow;"
```

### Redis Connection Failed
```bash
# Check if Redis is running
redis-cli ping
# Should return: PONG

# Start Redis service
redis-server.exe

# Check Redis config
redis-cli INFO server
```

---

## 📊 Default Test Credentials

### User: Developer
- Email: `developer@devflow.io`
- Password: `Developer@123`
- Role: DEVELOPER

### User: Project Manager
- Email: `pm@devflow.io`
- Password: `PM@123`
- Role: PROJECT_MANAGER

### User: Admin
- Email: `admin@devflow.io`
- Password: `Admin@123`
- Role: ADMIN

---

## 🎯 Development Tips

1. **Hot Reload Frontend**: Changes to React files auto-reload in browser
2. **Hot Reload Backend**: Use `spring-boot-devtools` for auto-reload (configured)
3. **Database Migrations**: Use Flyway or Liquibase for production migrations (not yet implemented)
4. **API Testing**: Use Postman or Insomnia for testing REST endpoints
5. **Debugging Backend**: Use Java debugger in VS Code or IDE
6. **Performance Monitoring**: Redis for caching, PostgreSQL query optimization

---

## 📝 Environment Variables

### Backend (application.yml)
```yaml
# Database
DATASOURCE_URL=jdbc:postgresql://localhost:5432/devflow
DATASOURCE_USER=postgres
DATASOURCE_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your-super-secret-key-change-in-production
JWT_EXPIRATION=900000

# Server
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/api
```

### Frontend (.env)
```env
VITE_API_URL=http://localhost:8080/api
VITE_APP_NAME=DevFlow
```

---

## 🚀 Next Steps

1. ✅ **Environment Setup** - Done (Maven, Node, Java)
2. ✅ **Backend Build** - Done (40 Java files compiled)
3. ✅ **Frontend Build** - Done (Production bundle ready)
4. 🟡 **Local Testing** - Start services and test endpoints
5. 🟡 **Frontend Integration** - Add API client (Axios/Fetch)
6. 🟡 **Add Unit Tests** - Target 80% code coverage
7. 🟡 **Docker Deployment** - Run full stack in containers
8. 🟡 **Cloud Deployment** - Deploy to Azure/AWS/GCP

---

**Status**: Ready for local development and testing 🚀

Last Updated: 2026-06-21 19:52 IST
