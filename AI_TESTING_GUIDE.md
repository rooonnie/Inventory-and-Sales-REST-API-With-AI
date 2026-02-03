# AI Chat Testing Guide

## Prerequisites
1. Ollama installed and running (http://localhost:11434)
2. Model downloaded: `ollama pull llama3.2`
3. Spring Boot application running

## Step 1: Check AI Status

**Request:**
```
GET http://localhost:8080/api/ai/status
```

**Expected Response:**
```json
{
  "ollamaAvailable": true,
  "status": "READY",
  "message": "AI service is ready!"
}
```

## Step 2: Get Help

**Request:**
```
GET http://localhost:8080/api/ai/help
```

**Expected Response:**
```json
{
  "description": "AI-powered inventory and sales assistant (Taglish)",
  "examples": [
    "bumili ako ng 10 eggs",
    "magkano kinita ko today?",
    "ano ang low stock items?",
    "ano ang best selling items ngayong linggo?",
    "pa-show ng lahat ng materials",
    "nagbenta ako ng 5 chocolate cakes"
  ],
  "capabilities": [...]
}
```

## Step 3: Test Conversations

### Example 1: View All Materials
**Request:**
```
POST http://localhost:8080/api/ai/chat
Content-Type: application/json

{
  "message": "pa-show ng lahat ng materials"
}
```

**Expected Response:**
```json
{
  "message": "Eto po ang listahan ng materials sa inventory...",
  "action": "VIEW_MATERIALS",
  "data": [...list of materials...],
  "needsConfirmation": false
}
```

### Example 2: Check Profit Today
**Request:**
```
POST http://localhost:8080/api/ai/chat
Content-Type: application/json

{
  "message": "magkano kinita ko today?"
}
```

**Expected Response:**
```json
{
  "message": "Tingnan natin ang profit mo ngayong araw...",
  "action": "VIEW_PROFIT",
  "data": {
    "period": "today",
    "totalProfit": 1500.00,
    "totalRevenue": 5000.00,
    ...
  },
  "needsConfirmation": false
}
```

### Example 3: Check Stock Levels
**Request:**
```
POST http://localhost:8080/api/ai/chat
Content-Type: application/json

{
  "message": "ano ang low stock items?"
}
```

**Expected Response:**
```json
{
  "message": "Eto po ang mga low stock items...",
  "action": "VIEW_STOCK",
  "data": {
    "lowStockCount": 2,
    "lowStockMaterials": [...],
    ...
  },
  "needsConfirmation": false
}
```

### Example 4: View Top Selling Items
**Request:**
```
POST http://localhost:8080/api/ai/chat
Content-Type: application/json

{
  "message": "ano ang best selling items ngayong week?"
}
```

**Expected Response:**
```json
{
  "message": "Eto po ang best-selling items ngayong linggo...",
  "action": "VIEW_TOP_ITEMS",
  "data": [
    {
      "foodItemId": 1,
      "foodItemName": "Chocolate Cake",
      "totalQuantitySold": 50
    },
    ...
  ],
  "needsConfirmation": false
}
```

## Postman Collection

You can import this to Postman:

```json
{
  "info": {
    "name": "Inventory AI Chat",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "AI Status",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/api/ai/status"
      }
    },
    {
      "name": "AI Chat - View Materials",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\"message\": \"pa-show ng lahat ng materials\"}"
        },
        "url": "http://localhost:8080/api/ai/chat"
      }
    },
    {
      "name": "AI Chat - Check Profit",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\"message\": \"magkano kinita ko today?\"}"
        },
        "url": "http://localhost:8080/api/ai/chat"
      }
    }
  ]
}
```

## Troubleshooting

### "AI service ay hindi available"
- Check if Ollama is running: `ollama serve`
- Verify: `curl http://localhost:11434`

### Slow responses
- First request may take 10-20 seconds (model loading)
- Subsequent requests should be faster (2-5 seconds)

### AI doesn't understand
- Try rephrasing in simpler terms
- Use Taglish: mix of Tagalog and English
- Be specific with numbers and dates

## Tips for Better Results

1. **Be specific**: "bumili ako ng 10 eggs" instead of "may bagong bilihan"
2. **Use numbers**: "5 pieces", "100 pesos"
3. **Mention dates**: "today", "ngayong week", "last month"
4. **Mix languages**: Taglish works best!
