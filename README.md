# 💳 Credit Application Service - CoopCredit

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**CoopCredit** is a comprehensive credit application system built with a focus on modern software architecture principles. It leverages **Hexagonal Architecture** to ensure maintainability and testability, coupled with a **Microservices** approach for scalability. This system is designed for the financial cooperative ecosystem.

---

## 📑 Table of Contents
- [Description](#-description)
- [Architecture](#-architecture)
- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [API Endpoints](#-api-endpoints)
- [Observability](#-observability)

---

## 📋 Description
**CoopCredit** provides a robust platform for managing credit applications. It facilitates the entire lifecycle of a credit request, from member registration to automated risk evaluation and final approval.

## 🏗️ Architecture
This project adheres to modern design principles to ensure **scalability** and **maintainability**.

* **Hexagonal Architecture (Ports & Adapters):** Clear separation between the core business logic (Domain) and implementation details (Infrastructure).
* **Microservices Strategy:** 
    * `credit-application-service`: The core domain service.
    * `risk-central-mock-service`: A simulation of an external financial risk evaluation system.
* **Clean Architecture:** Strict layering to keep the domain isolated from frameworks and drivers.

---

## ✨ Key Features

### 📊 Member Management
* **Affiliate Registration:** Secure onionboarding of new members.
* **Validation:** Unique document validation to prevent duplicates.
* **State Management:** Control of member statuses (**ACTIVE/INACTIVE**).

### 💳 Credit Application Management
* **Full Lifecycle:** Application submission, evaluation, and decisioning.
* **Risk Integration:** Seamless integration with external risk central (simulated).
* **Policy Engine:** Internal approval policies applied automatically.
* **Status Tracking:** Real-time status updates: **PENDING**, **APPROVED**, **REJECTED**.

### 🔐 Security
* **JWT Authentication:** Stateless, secure token-based authentication.
* **RBAC:** Role-Based Access Control with distinct roles:
    * `AFFILIATE`
    * `ANALYST`
    * `ADMIN`

### 📈 Observability
* **Monitoring:** Actuator endpoints exposed.
* **Metrics:** Prometheus metrics integrated with Micrometer.
* **Health Checks:** Detailed system health status.
* **Logging:** Structured logging for easier debugging and tracing.

### 🐳 Containerization
* **Docker:** Multi-stage builds for optimized, small-footprint images.
* **Docker Compose:** Full environment orchestration including PostgreSQL and Observability tools.

---

## 🛠️ Tech Stack

| Category | Technology |
| :--- | :--- |
| **Language** | Java 17 |
| **Framework** | Spring Boot 3 |
| **Database** | PostgreSQL |
| **Security** | Spring Security, JWT |
| **Build Tool** | Maven |
| **Containerization** | Docker, Docker Compose |
| **Migration** | Flyway |

---

## 📂 Project Structure

```bash
CoopCredit/
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
```

---

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites
* [Java Development Kit (JDK) 17+](https://www.oracle.com/java/technologies/downloads/)
* [Maven](https://maven.apache.org/) (Wrapper included)
* [Docker Desktop](https://www.docker.com/) (or Docker Engine + Compose)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd CoopCredit
   ```

2. **Start Infrastructure Services**
   Use Docker Compose to start the database (and valid observability tools if configured).
   ```bash
   docker-compose up postgres -d
   ```

3. **Verify Database Connection**
   Ensure PostgreSQL is running on port `5432`.

### Running the Application

This system consists of two services that need to run simultaneously.

#### 1. Start the Main Service (`credit-application-service`)
Open a terminal:
```bash
cd credit-application-service
mvn clean spring-boot:run
```
*Application will start on `http://localhost:8080`*

#### 2. Start the Mock Service (`risk-central-mock-service`)
Open a **new terminal tab/window**:
```bash
cd risk-central-mock-service
mvn spring-boot:run
```
*Mock service will start on `http://localhost:8081` (or configured port)*

---

## 📡 API Endpoints

Here are the primary endpoints available in the system.

| Method | Endpoint | Description | Role Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Login to receive JWT Token | Public |
| `POST` | `/api/users` | Register a new user | Admin |
| `POST` | `/api/credits` | Submit a credit application | Affiliate |
| `GET` | `/api/credits/{id}` | Get application status | Affiliate/Analyst |
| `POST` | `/risk-evaluation` | (Mock) Evaluate credit risk | System |

---

## 🧪 Testing with Postman

To facilitate API testing, a Postman collection `CoopCredit_Postman_Collection.json` is included in the root directory.

### Importing the Collection
1. Open Postman.
2. Click **Import** -> **File** -> **Upload Files**.
3. Select `CoopCredit_Postman_Collection.json`.

### Expected Results

| Request | Endpoint | Expected Status | Response Body / Note |
| :--- | :--- | :--- | :--- |
| **Login** | `POST /api/auth/login` | `200 OK` | `{ "token": "ey..." }` <br> *Token is auto-saved as `jwt_token`* |
| **Create Affiliate** | `POST /api/affiliates` | `201 Created` | `{ "id": 1, "document": "...", "status": "ACTIVE" }` |
| **Get All Affiliates** | `GET /api/affiliates` | `200 OK` | `[ { "id": 1, ... }, ... ]` |
| **Create Credit App** | `POST /api/credit-applications` | `201 Created` | `{ "id": 1, "status": "PENDING", ... }` |
| **Risk Evaluation** | `POST /api/risk-evaluation` | `200 OK` | `{ "score": 750, "nivelRiesgo": "LOW", ... }` |

> [!NOTE]
> Make sure to run the **Login** request first. The collection is configured to automatically extract the JWT token and use it for subsequent authenticated requests.

---
