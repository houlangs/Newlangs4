# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

HLYUN4 (厚浪云4) is a secondary domain distribution system (二级域名分发系统) with a microservices architecture consisting of:
- **Backend**: Spring Boot 3.3.1 application (`hl4-backend/`)
- **Frontend**: Vue 3 + PrimeVue dashboard (`hl4-dashboard/`)
- **Infrastructure**: Docker Compose setup with MySQL, Redis, and Nginx

## Essential Commands

### Backend Development (hl4-backend/)
```bash
mvn clean package -Dmaven.test.skip=true  # Build without tests
mvn spring-boot:run                       # Run Spring Boot application
```

### Frontend Development (hl4-dashboard/)
```bash
npm install                               # Install dependencies
npm run dev                              # Development server (port 5173)
npm run build                            # Production build
npm run preview                          # Preview production build
```

### Docker Deployment
```bash
docker-compose up -d                     # Start all services
docker-compose up --build -d            # Rebuild and restart
docker-compose logs -f [service-name]   # View service logs
```

### Database Access
```bash
# Connect to MySQL from Docker
docker exec -it hl4-mysql mysql -u root -p hl4
# Password: hl4_mysql_root_password
```

## Architecture & Key Components

### Backend Architecture (`hl4-backend/`)
- **Controllers** (`src/main/java/com/hlyun4/controller/`): REST API endpoints
- **Service Layer** (`src/main/java/com/hlyun4/service/`): Business logic
- **Mapper Layer** (`src/main/java/com/hlyun4/mapper/`): MyBatis Plus data access
- **Entity Layer** (`src/main/java/com/hlyun4/entity/`): Database models
- **Configuration** (`src/main/resources/application.yml`): Service configuration
- **SA-Token Integration**: Authentication/authorization framework
- **DNSPod Integration**: Tencent Cloud DNS API for domain management

### Frontend Architecture (`hl4-dashboard/`)
- **Views** (`src/views/`): Page components
- **Components** (`src/components/`): Reusable UI components
- **Stores** (`src/stores/`): Pinia state management
- **API Client** (`src/api/`): HTTP request utilities
- **Router Configuration** (`src/router/index.js`): Route definitions
- **PrimeVue Integration**: UI component library

### Database Schema
- **Users**: User management and authentication
- **Domains**: Domain record management
- **DNS Records**: DNS configuration mapping
- **Settings**: System configuration parameters

### Key Configuration Files
- `docker-compose.yml`: Service orchestration and environment setup
- `hl4-backend/src/main/resources/application.yml`: Spring Boot configuration
- `hl4-dashboard/vite.config.js`: Frontend build configuration
- `.github/workflows/maven-publish.yml`: CI/CD pipeline

## Development Workflow

1. **Backend Changes**: Modify Java code, rebuild with Maven, restart Spring Boot service
2. **Frontend Changes**: Modify Vue components, Vite handles hot reload automatically
3. **Database Changes**: Update MyBatis Plus entities and mappers, ensure schema compatibility
4. **Environment Variables**: Check docker-compose.yml for service configurations

## Important Notes

- **Default Admin**: admin@houlangs.com / 123456 (change in production)
- **Service Ports**: Frontend (8080 via Nginx), Backend (8080 Spring Boot)
- **Database**: MySQL 8.0 with automatic initialization script
- **Caching**: Redis enabled for session management and performance
- **Third-party Services**: DNSPod API integration for domain management