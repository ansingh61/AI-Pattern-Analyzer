# AI Pattern Analyzer Pro

Java Maven + React desktop trading terminal based on the Prompt 9 specification.

## Structure

- `backend-java`: Spring Boot API with mock market data, pattern analysis, matches, and paper orders.
- `desktop`: React + Vite terminal interface.

## Run the API

```powershell
cd backend-java
mvn spring-boot:run
```

The API listens on `http://localhost:8080`.

### Backend endpoints

- `GET /api/health`, `/api/system/status`
- `GET /api/market/symbols`, `/api/market/candles`
- `POST /api/analysis/extract`, `POST /api/portfolio/paper-orders`
- `GET /api/matches`
- `GET /api/scanner/signals`, `POST /api/scanner/runs`
- `GET /api/research/summary`, `POST /api/research/runs`
- `GET /api/brokers`, `POST /api/brokers/{id}/connect`, `POST /api/brokers/{id}/disconnect`
- `GET /api/portfolio/summary`
- `GET /api/settings`, `PUT /api/settings`

The backend is mock-first and stateless for local development. Broker adapters, authentication, PostgreSQL persistence, and WebSocket events can be added behind these contracts without changing the React screen structure.

## Run the React terminal

```powershell
cd desktop
npm install
npm run dev
```

Open `http://localhost:5173`.

The first slice is mock-first by design: it demonstrates the broker/chart/selection/analysis/match/paper-order workflow without requiring exchange credentials.
