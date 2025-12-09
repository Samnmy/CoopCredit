# 💳 Credit Application Service - CoopCredit

## 📋 Description
Comprehensive credit application system featuring **Hexagonal Architecture**, **JWT security**, **Observability**, and **Microservices** for the financial cooperative **CoopCredit**.

---

## 🏗️ Architecture
This project adheres to modern design principles to ensure **scalability** and **maintainability**.


[Image of Hexagonal Architecture diagram]


* **Hexagonal Architecture** (Ports & Adapters): Clear separation between the core business logic (Domain) and implementation details (Infrastructure).
* **Microservices**: Consists of two independent services: the main service (`credit-application-service`) and a risk central mock service (`risk-central-mock-service`).
* **Clean Architecture**: Strict layering to keep the domain isolated.

---

## ✨ Key Features

### 📊 Member Management
* Registration and editing of members (Affiliates).
* Unique document validation.
* Status control (**ACTIVE/INACTIVE**).

### 💳 Credit Application Management
* Full application and evaluation flow.
* Integration with external risk central (via a mock service).
* Internal approval policies.
* Statuses: **PENDING**, **APPROVED**, **REJECTED**.

### 🔐 Security
* **JWT** Authentication (stateless).
* Roles: `AFFILIATE`, `ANALYST`, `ADMIN`.
* Role-Based Access Control (**RBAC**).

### 📈 Observability
* **Actuator** endpoints for monitoring.
* **Prometheus** metrics with **Micrometer**.
* Detailed **Health Checks**.
* **Structured Logging**.

### 🐳 Containerization
* **Docker multi-stage builds** for optimized images.
* **Docker Compose** for complete environment orchestration (PostgreSQL, Prometheus, Grafana).

---

## 🛠️ Technologies

### Backend
| Category | Technologies |
| :--- | :--- |
| Language/Framework | **Java 17**, **Spring Boot 3.5.8** |
| Security | **Spring Security + JWT** |
| Persistence | **Spring Data JPA**, **PostgreSQL + Flyway** |
| Utilities | **MapStruct**, **OpenFeign** |
| Monitoring | **Actuator + Micrometer** |

### Infrastructure
* **Docker + Docker Compose**
* **PostgreSQL**
* **Prometheus**
* **Grafana**

### Testing
* **JUnit 5**
* **Mockito**
* **Testcontainers**

---

## 🚀 Installation

### 1. Prerequisites
Ensure you have the following installed:
* **Java 17** or higher
* **Maven 3.8+**
* **Docker 20.10+**
* **Docker Compose 2.0+**

### 2. Clone the Repository
```bash
git clone [repository-url]
cd credit-application-service
```
3. ConfigurationCreate an environment variables file (optional, if not using defaults):Bashcp .env.example .env
4. Run with Docker Compose (Recommended)This is the fastest way to bring up the entire ecosystem (services, database, and monitoring).Bash# Build and run all services in the background
docker-compose up --build -d

# View logs for all services
```bash
docker-compose logs -f
```
# Stop and remove containers, networks, and volumes safely
docker-compose down -v
5. Run LocallyIf you prefer running services on your local machine:Bash# 1. Start database (using Docker)
docker-compose up postgres -d

# 2. Compile the main project

```bash
mvn clean package
```
```bash
# 3. Run the main service
java -jar target/credit-application-service-1.0.0.jar

# 4. In ANOTHER TERMINAL, run the Risk Central Mock Service
cd risk-central-mock-service
mvn spring-boot:run
🌐 EndpointsAuthenticationMethodEndpointDescriptionRolesPOST/api/auth/loginLog in and get JWTPUBLICPOST/api/auth/registerRegister new user (Affiliate)PUBLICAffiliatesMethodEndpointDescriptionRolesPOST/api/affiliatesCreate new affiliateADMINGET/api/affiliatesList all affiliatesANALYST, ADMINGET/api/affiliates/{id}Get affiliate detailsAFFILIATE, ANALYST, ADMINCredit ApplicationsMethodEndpointDescriptionRolesPOST/api/credit-applicationsCreate new applicationAFFILIATEGET/api/credit-applications/{id}Get application detailsAFFILIATE, ANALYST, ADMINGET/api/credit-applications/affiliate/{id}List applications by affiliateAFFILIATE, ANALYST, ADMINPOST/api/credit-applications/{id}/evaluateEvaluate applicationANALYST, ADMINObservabilityMethodEndpointDescriptionGET/api/actuator/healthSystem health checkGET/api/actuator/metricsGeneral metricsGET/api/actuator/prometheusPrometheus format metricsGET/api/actuator/infoApplication information👥 Roles and PermissionsRoleDescriptionKey PermissionsROLE_AFFILIATERegular MemberCreate own credit applications, view their status, and personal info.ROLE_ANALYSTCredit AnalystView and evaluate PENDING applications, view affiliate info, and system metrics.ROLE_ADMINAdministratorCreate and manage affiliates, full access to all functionalities, user/role management.📊 Credit Application FlowThe application and evaluation process involves the following steps:Registration & Auth: Affiliate registers and logs in, obtaining a JWT.Application: Affiliate creates the credit application (POST /api/credit-applications).Risk Evaluation: The system internally consults the Risk Central Service (mock).Policy Application: The system applies internal approval/rejection policies.Initial Decision: Automatic approval or rejection based on results.Analyst Review (Optional): Analyst can review special cases and apply the final decision.🧪 TestingThe project includes unit, integration, and coverage tests.Bash# Execute all tests
mvn test

# Execute integration tests (configured with the 'integration' profile)
mvn verify

# Execute with coverage (generates a JaCoCo report)
mvn test jacoco:report

# Execute integration tests using Testcontainers
mvn test -Pintegration
📈 MonitoringIf you used docker-compose up, you can access the observability tools:PrometheusURL: http://localhost:9090Purpose: Application metrics collection.GrafanaURL: http://localhost:3000Credentials: User: admin, Password: adminPurpose: Metrics visualization and pre-configured dashboards.Health ChecksYou can check the application status directly:Bash# Application health check
curl http://localhost:8080/api/actuator/health

# Prometheus format metrics
curl http://localhost:8080/api/actuator/prometheus
🔧 ConfigurationEnvironment VariablesCan be configured via a .env file or system variables:VariableDescriptionDefault ValueSPRING_DATASOURCE_URLPostgreSQL connection URL.jdbc:postgresql://localhost:5432/coopcreditSPRING_DATASOURCE_USERNAMEPostgreSQL username.postgresSPRING_DATASOURCE_PASSWORDPostgreSQL password.postgresJWT_SECRETSecret key for signing JWT. Change in production!your-secret-key-hereJWT_EXPIRATIONJWT expiration time in milliseconds.86400000RISK_CENTRAL_SERVICE_URLURL for the risk service.http://localhost:8081Logging ConfigurationStructured logging is configured in application.yml:YAMLlogging:
  level:
    com.coopcredit: DEBUG # Log level for the application package
  file:
    name: logs/application.log # Log file path
🐳 DockerAvailable Imagescredit-application-service: Main servicerisk-central-mock-service: Risk mock servicepostgres: Database (official image)prometheus: Monitoring (official image)grafana: Dashboards (official image)Useful Docker CommandsBash# Build the main service image
docker build -t credit-application-service .

# View container logs
docker logs -f credit-application-service

# Run integration tests in containers (uses docker-compose.test.yml)
docker-compose -f docker-compose.test.yml up

🗂️ Project StructureThe structure reflects the Hexagonal Architecture:Plaintextcredit-application-service/
├── src/
│   ├── main/
│   │   ├── java/com/coopcredit/creditapplicationservice/
│   │   │   ├── config/                    # Spring configurations
│   │   │   ├── controller/                # Adapters (REST API - Inbound)
│   │   │   ├── domain/                    # Ports & Domain (Pure Business Logic)
│   │   │   │   ├── model/                 # Domain Entities
│   │   │   │   ├── port/                  # Ports (Inbound/Outbound Interfaces)
│   │   │   │   └── service/               # Use Cases
│   │   │   ├── application/               # Application Layer (DTOs, Mappers)
│   │   │   └── infrastructure/            # Adapters (Technical Implementations - Outbound)
│   │   │       ├── persistence/           # JPA Adapter
│   │   │       ├── rest/                  # OpenFeign Adapter (Risk Central)
│   │   │       └── security/              # Security (JWT Adapter)
│   │   └── resources/
│   │       ├── db/migration/              # Flyway Migrations
│   │       └── application.yml            # Application Configuration
│   └── test/                              # Tests
├── risk-central-mock-service/             # Mock service module
├── docker-compose.yml                     # Service orchestration
├── Dockerfile                             # Docker Build file
├── prometheus.yml                         # Prometheus config
└── README.md                              # This file
📝 API DocumentationSwagger/OpenAPIInteractive documentation is available once the service is running:Swagger UI: http://localhost:8080/api/swagger-ui.htmlOpenAPI Docs: http://localhost:8080/api/v3/api-docsPostman CollectionA Postman collection is included in the /postman folder, which contains:Environment configuration.Requests for all endpoints.Payload examples.🐛 TroubleshootingCommon IssueVerificationSolutionDatabase connection failure`docker psgrep postgres` (check if running)Flyway migration errorApplication logsdocker-compose down -v and restart the database.JWT issuesCheck JWT_SECRET in .envObtain a new token via POST /api/auth/login.Risk Central Service unavailablecurl http://localhost:8081/api/healthRebuild and run mock service: cd risk-central-mock-service; mvn spring-boot:run🏁 Execution Instructions (Summary)Test Users:| Role | Username | Password || :--- | :--- | :--- || Admin | admin | admin123 || Analyst | analyst | analyst123 || Affiliate | johndoe | password123 |IMPORTANT: For production, all secrets and passwords must be changed.Bash# 1. Start database
docker-compose up postgres -d

# 2. Build and run the main service
mvn clean package
java -jar target/credit-application-service-1.0.0.jar

# 3. In ANOTHER TERMINAL, run the mock service
cd risk-central-mock-service
mvn spring-boot:run
Thank you for using the Credit Application Service! 🚀