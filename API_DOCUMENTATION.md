# Bharat Bhumi API Documentation

> REST API for digital land records, ownership management, document verification, and audit tracking.

---

## 1. Overview

**Bharat Bhumi** is a digital land-record management platform designed to provide secure and structured access to land-related information.

The backend provides APIs for:

- Land parcels
- Owners
- Ownership records
- Land documents
- Document verification
- Audit logs
- Authentication and authorization

---

## 2. Technology Stack

| Component | Technology |
|---|---|
| Backend | Spring Boot |
| Language | Java |
| Database | PostgreSQL |
| Security | Spring Security + JWT |
| API Documentation | Swagger / OpenAPI |
| Build Tool | Maven |

---

## 3. Base URL

```text
http://localhost:8080/api/v1
```

---

## 4. Authentication

Bharat Bhumi uses **JWT-based authentication**.

### Login

```http
POST /auth/login
```

### Request

```json
{
  "username": "admin",
  "password": "admin123"
}
```

The API returns a JWT token along with the user's role.

For protected endpoints, include the token in the request header:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 5. Roles & Access

| Role | Access |
|---|---|
| USER | Read-only access |
| ADMIN | Full CRUD access |

`USER` can access GET endpoints, while `ADMIN` can perform GET, POST, PUT, PATCH, and DELETE operations.

---

# 6. API Endpoints

## Land Parcels

| Method | Endpoint | Access |
|---|---|---|
| GET | `/parcels` | USER / ADMIN |
| GET | `/parcels/{ulpin}` | USER / ADMIN |
| GET | `/parcels/search` | USER / ADMIN |
| POST | `/parcels` | ADMIN |
| PUT | `/parcels/{ulpin}` | ADMIN |
| DELETE | `/parcels/{ulpin}` | ADMIN |

Supports parcel search, filtering, and pagination.

### Search Parameters

- `ulpin`
- `name`
- `propertyType`
- `address`
- `minArea`
- `maxArea`
- `page`
- `size`
- `sort`

---

## Owners

| Method | Endpoint | Access |
|---|---|---|
| GET | `/owners` | USER / ADMIN |
| GET | `/owners/{id}` | USER / ADMIN |
| POST | `/owners` | ADMIN |
| PUT | `/owners/{id}` | ADMIN |
| DELETE | `/owners/{id}` | ADMIN |

---

## Ownership

| Method | Endpoint | Access |
|---|---|---|
| GET | `/ownerships` | USER / ADMIN |
| GET | `/ownerships/{id}` | USER / ADMIN |
| GET | `/ownerships/owner/{ownerId}` | USER / ADMIN |
| GET | `/ownerships/parcel/{ulpin}` | USER / ADMIN |
| POST | `/ownerships` | ADMIN |
| PUT | `/ownerships/{id}` | ADMIN |
| DELETE | `/ownerships/{id}` | ADMIN |

---

## Documents

| Method | Endpoint | Access |
|---|---|---|
| GET | `/documents` | USER / ADMIN |
| GET | `/documents/{id}` | USER / ADMIN |
| GET | `/documents/parcel/{ulpin}` | USER / ADMIN |
| POST | `/documents` | ADMIN |
| PUT | `/documents/{id}` | ADMIN |
| PUT | `/documents/{id}/verify` | ADMIN |
| DELETE | `/documents/{id}` | ADMIN |

Document verification automatically records the relevant action in the audit log.

---

## Audit Logs

| Method | Endpoint | Access |
|---|---|---|
| GET | `/audit-logs` | USER / ADMIN |
| GET | `/audit-logs/{id}` | USER / ADMIN |
| GET | `/audit-logs/entity` | USER / ADMIN |
| GET | `/audit-logs/type/{entityType}` | USER / ADMIN |
| GET | `/audit-logs/user/{performedBy}` | USER / ADMIN |
| POST | `/audit-logs` | ADMIN |

Audit logs provide traceability for important system operations.

---

# 7. Error Handling

The API uses standard HTTP status codes.

| Status | Meaning |
|---|---|
| 200 | Request successful |
| 201 | Resource created |
| 400 | Invalid request / validation error |
| 401 | Authentication required |
| 403 | Access denied |
| 404 | Resource not found |
| 409 | Duplicate resource |
| 500 | Internal server error |

### Example Error Response

```json
{
  "status": 404,
  "message": "Resource not found"
}
```

---

# 8. Swagger / OpenAPI

Interactive API documentation:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Swagger can be used to test authenticated API endpoints using the JWT token.

### Swagger Authentication

Click **Authorize** and enter only the JWT token:

```text
<JWT_TOKEN>
```

Do not enter:

```text
Bearer <JWT_TOKEN>
```

Swagger automatically adds the `Bearer` prefix.

---

# 9. Backend Architecture

```text
Frontend
   |
   v
REST API
   |
   v
Controllers
   |
   v
Services
   |
   v
Repositories
   |
   v
PostgreSQL
```

### Security Flow

```text
Login
  |
  v
JWT Token
  |
  v
JWT Authentication Filter
  |
  v
Spring Security
  |
  v
Role-Based Authorization
  |
  v
Protected API
```

---

# 10. Current Features

- Secure JWT authentication
- Role-based authorization
- Land parcel CRUD
- Parcel search, filtering, and pagination
- Owner management
- Ownership management and validation
- Document management
- Document verification
- Audit logging
- Global exception handling
- PostgreSQL persistence
- Swagger / OpenAPI documentation
- CORS support

---

## Development

Default demo accounts:

```text
ADMIN
username: admin
password: admin123

USER
username: user
password: user123
```

> These credentials are intended only for local development and demonstration.