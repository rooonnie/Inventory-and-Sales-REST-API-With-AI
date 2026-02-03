# Inventory and Sales REST API with AI

A Spring Boot REST API for managing small business inventory, food items, and sales with **AI-powered conversational interface** using Ollama for natural language interactions in Taglish (Tagalog/English).

## Tech Stack
- Java 17
- Spring Boot 4.0.2
- Oracle Database FREE
- Spring Data JPA
- Hibernate
- Maven
- **Ollama (AI/LLM Integration)**

## Prerequisites
- JDK 17 or higher
- Oracle Database (FREE, XE, or Enterprise)
- Maven 3.6 or higher
- **Ollama installed and running** (for AI features)

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

## Ollama Setup (for AI Features)

1. **Install Ollama:** Download from https://ollama.ai

2. **Pull the model:**
```bash
ollama pull llama3.2
```

3. **Start Ollama** (runs on localhost:11434):
```bash
ollama serve
```

4. **Verify Ollama is running:**
```bash
curl http://localhost:11434
```

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
✅ **Top Selling Items** - Ranked by quantity sold  
✅ **AI-Powered Chat** - Conversational interface in Taglish using Ollama  
✅ **Natural Language Processing** - Understand Filipino/English commands  

## How to Use AI Chat

The AI assistant understands conversational Taglish (Tagalog/English mix):

### Check AI Status
```
GET http://localhost:8080/api/ai/status
```

### Chat with AI
```
POST http://localhost:8080/api/ai/chat
Content-Type: application/json

{
  "message": "magkano kinita ko ngayong araw?"
}
```

### Supported Commands
- **Inventory**: "bumili ako ng...", "may bagong stock..."
- **Sales**: "nagbenta ako ng...", "sold..."
- **Reports**: "magkano kinita ko...", "ano ang best selling..."
- **Stock**: "pa-check ng stock", "ano ang low stock items"
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

## 

### AI Chat (Conversational Interface) 🤖
```
POST http://localhost:8080/api/ai/chat
GET  http://localhost:8080/api/ai/status
GET  http://localhost:8080/api/ai/help
```

**AI Chat Request:**
```json
{
  "message": "bumili ako ng 10 eggs"
}
```

**AI Chat Response:**
```json
{
  "message": "Magkano po ang presyo per piece ng egg?",
  "action": "ADD_MATERIAL",
  "data": {...},
  "needsConfirmation": true
}
```

**Example Conversations:**
- "bumili ako ng 10 eggs" - Add materials
- "magkano kinita ko today?" - Check profit
- "ano ang low stock items?" - View low stock
- "pa-show ng lahat ng materials" - View all materials
- "ano ang best selling items ngayong week?" - Top itemsDevelopment Progress
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
│   │       ├── ai/               # AI Integration
│   │       │   ├── controller/   # AI Chat endpoints
│   │       │   ├── service/      # Ollama service
│   │       │   └── dto/          # AI DTOs
│   │       ├── controller/       # REST Controllers
│   │       ├── service/          # Business Logic
│   │       ├── repository/       # Data Access Layer
│   │       ├── entity/           # JPA Entities
│   │       ├── dto/              # Data Transfer Objects
│   │       ├── config/           # Configuration Classes
│   │       └── exception/        # Global exception handling
│   └── resources/
│       └── application.properties
└── test/
```

## Development Progress
- [x] Project structure setup
- [x] Entities (Material, FoodItem, Ingredient, Sale)
- [x] Repositories with custom queries
- [x] Services with business logic
- [x] REST Controllers
- [x] DTOs with validation
- [x] Reports (Profit, Stock, Top Items)
- [x] Sample data initialization
- [x] Unit tests
- [x] AI Integration with Ollama
- [x] Conversational Taglish interface

## Troubleshooting

### AI Chat not working?
1. Check if Ollama is running: `GET /api/ai/status`
2. Verify Ollama: `curl http://localhost:11434`
3. Check model is installed: `ollama list`
4. Pull model if missing: `ollama pull llama3.2`

### Database connection issues?
1. Verify Oracle is running
2. Check service name in connection URL
3. Test credentials with SQL Developer

## License
MIT
