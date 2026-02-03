# Inventory and Sales REST API with AI

A Spring Boot REST API for managing small business inventory, food items, and sales with reporting capabilities.

## Tech Stack
- Java 17
- Spring Boot 4.0.2
- PostgreSQL
- Spring Data JPA
- Maven

## Prerequisites
- JDK 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Database Setup

1. Install PostgreSQL and create a database:
```sql
CREATE DATABASE inventory_sales_db;
```

2. Update `src/main/resources/application.properties` with your PostgreSQL credentials if needed:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

1. Clone the repository:
```bash
git clone https://github.com/rooonnie/Inventory-and-Sales-REST-API-With-AI.git
cd Inventory-and-Sales-REST-API-With-AI
```

2. Run the application:
```bash
mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing

Health check endpoint:
```
GET http://localhost:8080/api/health
```

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/project/sales_and_inventory_with_ai/
│   │       ├── controller/      # REST Controllers
│   │       ├── service/          # Business Logic
│   │       ├── repository/       # Data Access Layer
│   │       ├── entity/           # JPA Entities
│   │       ├── dto/              # Data Transfer Objects
│   │       └── config/           # Configuration Classes
│   └── resources/
│       └── application.properties
└── test/
```

## API Documentation
(To be added as endpoints are developed)

## Development Progress
- [x] Project structure setup
- [ ] Entities
- [ ] Repositories
- [ ] Services
- [ ] Controllers
- [ ] DTOs
- [ ] Reports

## License
MIT
