# LMT — Life Management Tool

A to-do app built around the Eisenhower matrix. Organize your life into columns
(work, studies, home...), and sort each column's tasks into four quadrants:
**Do**, **Plan**, **Delegate** and **Eliminate**. Tasks can be dragged between
quadrants and columns, and everything is saved to the database.

## Features

- Create, rename and delete columns (life domains)
- Add tasks with an importance and urgency level
- Drag and drop tasks between quadrants — the new position is saved
- Delete tasks and columns with confirmation
- All data persisted through the REST API

## Tech stack

| Part | Stack |
| --- | --- |
| `lmt-frontend` | React 18, TypeScript, Vite, Tailwind CSS, react-beautiful-dnd |
| `lmt-backend` | Spring Boot 3, Spring Data JPA, MySQL |

## Running locally

### Backend

Requires Java 17+ and a MySQL server. By default it connects to
`jdbc:mysql://localhost:3306/lmt_springboot` with `root`/`root`. Override with
the `DB_URL`, `DB_USERNAME` and `DB_PASSWORD` environment variables
(required in production).

```bash
cd lmt-backend
./mvnw spring-boot:run
```

The API runs on `http://localhost:8080`, with Swagger docs at `/api-docs`.

### Frontend

Requires Node.js and pnpm. Point the app at your backend in
`lmt-frontend/.env`:

```
VITE_BACKEND_DOMAIN=http://localhost:8080
```

```bash
cd lmt-frontend
pnpm install
pnpm dev
```

The app runs on `http://localhost:5173`.

## API overview

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/domainColumn/all` | All columns with their tasks |
| POST | `/api/domainColumn/add` | Create a column |
| PUT | `/api/domainColumn/update` | Rename a column |
| DELETE | `/api/domainColumn/delete` | Delete a column and its tasks |
| POST | `/api/task/add` | Create a task |
| PUT | `/api/task/update` | Move a task (quadrant/column) |
| DELETE | `/api/task/delete` | Delete a task |
