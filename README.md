# PayPie - Expense Management System

PayPie is a robust expense management system built using Domain-Driven Design (DDD) and Hexagonal Architecture principles. It provides a clean, maintainable, and scalable solution for managing shared expenses among groups of participants.

## 🏗️ Architecture

PayPie follows a clean architecture approach with clear separation of concerns:

### Core Layers

1. **Domain Layer** (`domain/`)
   - Contains business logic and rules
   - Includes aggregates, entities, and value objects
   - Pure business logic without external dependencies

2. **Application Layer** (`application/`)
   - Orchestrates use cases
   - Defines ports (interfaces) for external communication
   - Contains DTOs and use case implementations

3. **Adapter Layer** (`adapter/`)
   - Implements ports defined in the application layer
   - Separated into inbound (controllers) and outbound (repositories) adapters
   - Handles external communication

4. **Infrastructure Layer** (`infrastructure/`)
   - Provides technical capabilities
   - Implements persistence, messaging, and other technical concerns
   - Keeps technical details away from business logic

### Key Components

- **Aggregates**: `ExpenseGroup`, `Participant`
- **Value Objects**: `ExpenseGroupId`, `GroupName`, `ParticipantId`
- **Ports**: Input ports for use cases, output ports for repositories
- **Adapters**: REST controllers, repository implementations

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Gradle

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/sardul3/paypie.git
   cd paypie
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

### Running the Application

#### Using Docker Compose (Recommended)

The application is containerized and can be easily run using Docker Compose, which sets up the complete environment including the database.

1. Build the Docker images:
   ```bash
   docker-compose build
   ```

2. Start all services:
   ```bash
   docker-compose up -d
   ```

3. The application will be available at `http://localhost:8080`

4. Monitor the logs:
   ```bash
   docker-compose logs -f app
   ```

5. Access PostgreSQL database:
   ```bash
   # Direct connection to PostgreSQL
   docker exec -it expense-postgres psql -U expense_user -d expense_db
   
   # Or use PgAdmin available at http://localhost:5050
   # Login credentials:
   # Email: admin@example.com
   # Password: admin123
   ```

6. Stop the application:
   ```bash
   docker-compose down
   ```

7. Stop and remove volumes (this will delete your data):
   ```bash
   docker-compose down -v
   ```

#### Running Services Individually

You can also choose to run specific services:

```bash
# Run only the database
docker-compose up -d postgres

# Run database and admin interface
docker-compose up -d postgres dbviz
```

#### Running Locally

1. Start the application:
   ```bash
   ./gradlew bootRun
   ```

2. The application will be available at `http://localhost:8080`

## 📚 API Documentation

The API documentation is available at `/swagger-ui.html` when the application is running.

### Key Endpoints

- `POST /api/v1/expense-groups` - Create a new expense group
- `GET /api/v1/expense-groups/{id}` - Get expense group details
- `POST /api/v1/expense-groups/{id}/participants` - Add a participant to a group

## 🛠️ Development

### Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── io/github/sardul3/expense/
│   │       ├── domain/           # Domain layer
│   │       │   ├── model/        # Aggregates and entities
│   │       │   ├── valueobject/  # Value objects
│   │       │   └── common/       # Shared domain concepts
│   │       ├── application/      # Application layer
│   │       │   ├── usecase/      # Use cases
│   │       │   ├── port/         # Ports (interfaces)
│   │       │   └── dto/          # Data transfer objects
│   │       ├── adapter/          # Adapter layer
│   │       │   ├── in/           # Inbound adapters (controllers)
│   │       │   └── out/          # Outbound adapters (repositories)
│   │       └── infrastructure/   # Infrastructure layer
│   └── resources/
└── test/                         # Test code
```

### Building and Testing

1. Run tests:
   ```bash
   ./gradlew test
   ```

2. Build the application:
   ```bash
   ./gradlew build
   ```

3. Run integration tests:
   ```bash
   ./gradlew integrationTest
   ```

## 🔍 Architecture Principles

### Domain-Driven Design

- Rich domain model with clear boundaries
- Ubiquitous language reflected in code
- Aggregates maintain consistency boundaries
- Value objects for immutable concepts

### Hexagonal Architecture

- Clear separation between domain and infrastructure
- Ports define the application's boundaries
- Adapters implement ports without affecting the core
- Dependencies point inward

### Clean Architecture

- Business rules are independent of frameworks
- External concerns are kept at the boundaries
- Dependencies follow the dependency rule
- Use cases are clearly defined and isolated

## 📋 Documentation

### Architecture Decision Records (ADRs)

The project maintains a set of Architecture Decision Records (ADRs) to document significant architectural decisions. These records provide context, rationale, and consequences for each decision.

- [View all ADRs](docs/adr/)

### User Stories

User stories are available to provide context about the features and functionality from a user's perspective.

- [View all User Stories](docs/stories/)

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Get Your Hands Dirty on Clean Architecture by Tom Hombergs
