# Inventory and Sales REST API

A Spring Boot REST API for managing small business inventory, food items, and sales with automated reporting capabilities.

## Tech Stack
- Java 17
- Spring Boot 4.0.2
- Oracle Database FREE
- Spring Data JPA
- Hibernate
- Maven

## Prerequisites
- JDK 17 or higher
- Oracle Database (FREE, XE, or Enterprise)
- Maven 3.6 or higher

## Database Setup

1. **Install Oracle Database FREE** (or use existing Oracle installation)

2. **Create tables** (optional - Hibernate will auto-create them):
```sql
-- Tables will be created automatically by Hibernate
-- Or run the DDL scripts manually if preferred
```

3. **Update credentials** in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/FREE
spring.datasource.username=SYSTEM
spring.datasource.password=your_password
```

**Note:** Replace `/FREE` with your Oracle service name (e.g., `/XEPDB1`, `:ORCL`)

## Running the Application

1. **Clone the repository:**
```bash
git clone https://github.com/rooonnie/Inventory-and-Sales-REST-API-With-AI.git
cd Inventory-and-Sales-REST-API-With-AI
```

2. **Configure database connection** in `application.properties`

3. **Run the application:**
```bash
mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

**On first run, sample data will be automatically loaded:**
- 10 Materials (Flour, Sugar, Eggs, Milk, etc.)
- 4 Food Items (Chocolate Cake, Vanilla Cupcake, Cheese Pizza, Cappuccino)
- 6 Sample Sales

## API Endpoints

### Health Check
```
GET http://localhost:8080/api/health
```

### Materials Management
```
GET    http://localhost:8080/api/materials          - Get all materials
GET    http://localhost:8080/api/materials/{id}     - Get material by ID
POST   http://localhost:8080/api/materials          - Create new material
PUT    http://localhost:8080/api/materials/{id}     - Update material
DELETE http://localhost:8080/api/materials/{id}     - Delete material
```

### Food Items Management
```Features

✅ **Materials Management** - Track inventory with price and quantity  
✅ **Food Items with Ingredients** - Define recipes with material requirements  
✅ **Sales Tracking** - Automatic stock deduction and profit calculation  
✅ **Profit Calculation** - `(sale_price × quantity) - ingredient_costs`  
✅ **Period-based Reports** - Today, 2 days, week, month, year  
✅ **Low Stock Alerts** - Configurable threshold warnings  
✅ **Top Selling Items** - Ranked by quantity sold  

## Development Progress
- [x] Project structure setup
- [x] Entities (Material, FoodItem, Ingredient, Sale)
- [x] Repositories with custom queries
- [x] Services with business logic
- [x] REST Controllers
- [x] DTOs with validation
- [x] Reports (Profit, Stock, Top Items)
- [x] Sample data initialization
- [ ] Unit tes://localhost:8080/api/sales              - Get all sales
GET    http://localhost:8080/api/sales?startDate=&endDate= - Filter by date
POST   http://localhost:8080/api/sales              - Create sale (auto-deducts stock)
```

### Reports
```
GET http://localhost:8080/api/reports/profit?period=today|2days|week|month|year
GET http://localhost:8080/api/reports/materials?lowStockThreshold=10
GET http://localhost:8080/api/reports/top-items?period=today&limit=10
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
