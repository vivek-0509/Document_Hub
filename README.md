# SST Internal Tools - College Management System

A comprehensive backend solution for managing key college operations and services.

## 📋 Overview

SST Internal Tools is a modular Spring Boot application designed to streamline administrative processes and services in a college environment. Built with a clean, domain-driven architecture, the system provides RESTful APIs for various college functions including student accommodations, announcements, gallery management, mess services, transportation, and more.

## 🏗️ Architecture

The project follows a feature-based modular architecture with clear separation of concerns:

```
com.sstinternaltools.sstinternal_tools/
├── announcements/           # Campus-wide notification system
├── auth/                    # Authentication and authorization
├── gallery/                 # Media gallery management  
├── hostel/                  # Student accommodation management
├── jwt/                     # JWT token implementation
├── mess/                    # Dining services management
├── placement/               # Career services and recruitment
├── transport/               # Transportation services
└── user/                    # User management and profiles
```

Each domain module maintains a consistent internal structure:

| Layer | Purpose |
|-------|---------|
| `controller` | REST API endpoints that handle HTTP requests |
| `dto` | Data Transfer Objects for API request/response handling |
| `facade` | Orchestration layer for complex operations across services |
| `mapper` | Conversion utilities between entities and DTOs |
| `repository` | Data access layer with JPA repositories |
| `service` | Business logic implementation |
| `entity` | Domain model objects (where applicable) |

## 🚀 Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- IntelliJ IDEA (recommended)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/sst-internal-tools.git
   cd sst-internal-tools
   ```

2. **Configure environment variables**
   
   Create a `.env` file in the project root with the following variables:
   ```
   # Database Configuration
   DATABASE_URL=jdbc:postgresql://localhost:5432/sst-internal-tools
   DATABASE_USER=sst
   DATABASE_PASSWORD=tools
   ```

3. **Start the database using Docker**
   ```bash
   docker-compose up -d
   ```
   This will start a PostgreSQL instance with the configured credentials.

4. **Build the project**
   ```bash
   ./mvnw clean install
   ```

5. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The server starts at `http://localhost:8080`

## 🔌 API Endpoints

The application exposes RESTful endpoints organized by domain:

### Authentication
- `POST /api/auth/login` - Authenticate users
- `POST /api/auth/register` - Register new users

### Announcements
- `GET /api/announcements` - List all announcements
- `POST /api/announcements` - Create new announcement
- `GET /api/announcements/{id}` - Get specific announcement

### Hostel Management
- `GET /api/hostel/rooms` - List available rooms
- `POST /api/hostel/application` - Submit accommodation request

### Mess Services
- `GET /api/mess/menu` - Get current menu
- `POST /api/mess/feedback` - Submit meal feedback

*Note: Complete API documentation with Swagger/OpenAPI will be available at `/swagger-ui.html` in future releases.*

## 🛠️ Tech Stack

- **Spring Boot** - Application framework
- **Spring Data JPA** - Data persistence
- **Spring Security** - Authentication and authorization
- **PostgreSQL** - Primary database
- **JWT** - Token-based authentication
- **Docker** - Containerization
- **Maven** - Dependency management
- **JUnit & Mockito** - Testing framework

## 🧪 Testing

Run the test suite:

```bash
./mvnw test
```

## 📁 Project Configuration

The project uses environment variables for configuration, which provides several benefits:

1. **Security**: Sensitive information like database credentials stays out of version control
2. **Environment-specific settings**: Easily switch between development, testing, and production environments
3. **Containerization support**: Seamless integration with Docker and cloud platforms

Key configuration files:

- `docker-compose.yaml` - Defines the PostgreSQL service
- `application.properties` - Core application settings using environment variables
- `.env` - Local environment variable definitions (not committed to version control)

## 👥 Development Team

- **Project Manager**: Mrinal Bhattacharya
- **Developers**: Arnav Gupta, Vivek Singh Solanki, Srinidhi Naren, Rudray Mehra

## 📝 Contributing Guidelines

We welcome contributions to the SST Internal Tools project. To maintain code quality:

1. **Follow naming conventions** and project structure
2. **Write unit tests** for new functionality
3. **Document your code** with appropriate comments
4. **Submit a pull request** for review

### Code Style

- Use consistent indentation (4 spaces)
- Follow Java naming conventions
- Keep methods focused and concise
- Document public APIs with Javadoc
- Write meaningful commit messages

## 📄 License

This project is proprietary software for internal use only.

---

## 🚧 Future Roadmap

- **API Documentation**: Implement Swagger/OpenAPI
- **Authentication Enhancements**: OAuth2 integration
- **Monitoring**: Add Actuator endpoints
- **Caching**: Implement Redis for improved performance
- **CI/CD Pipeline**: Automated testing and deployment
- **Container Orchestration**: Kubernetes deployment configuration

---

For any questions or support, please contact the project maintainers.
