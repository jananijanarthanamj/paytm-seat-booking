# Seat Reservation at Scale – Deploy & Observe

A Spring Boot backend service for concurrent seat reservation with strong consistency, idempotency, per-user booking limits, cancellation, authentication, and observability.

The application is designed to prevent double-selling of seats even when multiple users attempt to reserve the same seat concurrently.

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA / Hibernate
* MySQL
* Maven
* Docker
* Spring Boot Actuator
* Micrometer / Prometheus
* REST APIs

## Key Features

* Create shows with a configurable number of seats and price.
* Reserve one or more seats.
* Prevent double-selling during concurrent reservations.
* Pessimistic database locking for seat-level concurrency control.
* Per-user reservation limit.
* Idempotency support for safe request retries.
* Reject reuse of an idempotency key with a different request/user.
* Cancel confirmed reservations and release seats.
* Token-based user authentication.
* Validation and appropriate HTTP error responses.
* Liveness/readiness health endpoint.
* Prometheus metrics endpoint.
* Dockerized application.
* Publicly deployed application with MySQL.

## Live Application

**Base URL**

```text
https://incredible-reflection-production-47c0.up.railway.app
```

### Health Check

```text
GET /actuator/health
```

Full URL:

```text
https://incredible-reflection-production-47c0.up.railway.app/actuator/health
```

### Prometheus Metrics

```text
GET /actuator/prometheus
```

Full URL:

```text
https://incredible-reflection-production-47c0.up.railway.app/actuator/prometheus
```

## Authentication

Protected APIs use a simple Bearer token to identify the authenticated user.

Example:

```text
Authorization: Bearer UserA
```

The token value is used as the authenticated user identity for reservation and cancellation operations.

For testing, you can use values such as:

```text
UserA
UserB
UserC
```

No real account or password is required for the demo authentication flow.

## API Endpoints

### 1. Create a Show

**POST**

```text
/shows
```

**Authorization**

```text
Bearer AdminUser
```

**Request Body**

```json
{
  "name": "Paytm Concert",
  "seats": 10,
  "price_paise": 50000
}
```

The show is created with all seats initially available.

---

### 2. Get Show and Seat Status

**GET**

```text
/shows/{showId}
```

Example:

```text
GET /shows/1
```

The response provides:

* Total seats
* Available seats
* Confirmed seats
* Individual seat status

The seat-count invariant is maintained:

```text
availableSeats + confirmedSeats = totalSeats
```

---

### 3. Reserve Seats

**POST**

```text
/shows/{showId}/reserve
```

**Authorization**

```text
Authorization: Bearer UserA
```

**Required Header**

```text
Idempotency-Key: booking-001
```

**Content-Type**

```text
application/json
```

**Request Body**

```json
{
  "seatNumbers": [
    "S1",
    "S2"
  ]
}
```

A successful reservation returns:

```text
201 Created
```

If the requested seat is already confirmed by another user, the request returns:

```text
409 Conflict
```

---

### 4. Cancel a Reservation

**DELETE**

```text
/shows/reservations/{reservationId}/cancel
```

**Authorization**

```text
Authorization: Bearer UserA
```

Example:

```text
DELETE /shows/reservations/1/cancel
```

Successful cancellation returns:

```text
204 No Content
```

The cancelled seats become available for reservation again.

---

## Idempotency

Every reservation request requires an `Idempotency-Key`.

Example:

```text
Idempotency-Key: booking-001
```

If the same user retries the same request with the same idempotency key, the original reservation is returned instead of creating another reservation.

If the same idempotency key is reused with a different request or different user, the request is rejected with:

```text
409 Conflict
```

This protects the system from duplicate reservations caused by request retries.

## Concurrency and Double-Selling Protection

Seat reservation uses database-level locking to ensure that concurrent requests competing for the same seat are serialized correctly.

For example, if multiple users simultaneously attempt to reserve:

```text
S1
```

only one request can successfully confirm the seat.

Expected result:

```text
1 request  -> 201 Created
remaining -> 409 Conflict
```

The implementation uses pessimistic database locking together with transactional operations to maintain consistency under concurrent requests.

## Per-User Reservation Limit

The default maximum number of confirmed seats per user is:

```text
4 seats
```

A request that would cause a user to exceed the configured limit is rejected with:

```text
409 Conflict
```

The implementation also protects the per-user limit under concurrent requests.

## Error Handling

The application handles invalid and conflicting requests using appropriate HTTP responses, including:

| Scenario                                             |           Response |
| ---------------------------------------------------- | -----------------: |
| Successful reservation                               |      `201 Created` |
| Successful cancellation                              |   `204 No Content` |
| Invalid request                                      |  `400 Bad Request` |
| Missing/non-existent resource                        |    `404 Not Found` |
| Seat conflict / booking limit / idempotency conflict |     `409 Conflict` |
| Unauthorized protected request                       | `401 Unauthorized` |

## Observability

### Health

```text
GET /actuator/health
```

The endpoint exposes application health and liveness/readiness groups.

### Prometheus

```text
GET /actuator/prometheus
```

The endpoint exposes application, JVM, HTTP, database connection pool, and other runtime metrics through Prometheus-compatible metrics.

## Docker

The application includes a multi-stage Dockerfile.

The first stage builds the Spring Boot application using Maven and Java 21.

The second stage runs the generated application using a Java 21 runtime image.

Build the image:

```bash
docker build -t paytm-seat-booking:latest .
```

Run the application:

```bash
docker run -p 8080:8080 paytm-seat-booking:latest
```

Database connection values can be supplied through environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

## Running Locally

### Prerequisites

* Java 21
* Maven
* MySQL
* Docker (optional)

### Database

Create a MySQL database:

```sql
CREATE DATABASE paytm_seat_booking;
```

Configure the following environment variables:

```text
DB_URL=jdbc:mysql://localhost:3306/paytm_seat_booking
DB_USERNAME=root
DB_PASSWORD=<your-password>
```

### Build

Windows:

```cmd
mvnw.cmd clean package -DskipTests
```

Linux/macOS:

```bash
./mvnw clean package -DskipTests
```

### Run

```bash
java -jar target/<generated-jar-name>.jar
```

The application runs on:

```text
http://localhost:8080
```

## Testing Scenarios

The implementation was validated against the following scenarios:

* Successful show creation.
* Successful seat reservation.
* Concurrent attempts to reserve the same seat.
* Concurrent per-user reservation limit.
* Same idempotency key with the same request.
* Same idempotency key with a different request.
* Same idempotency key used by different users.
* Reservation cancellation.
* Re-booking a cancelled seat.
* Unauthorized cancellation attempts.
* Missing idempotency key.
* Empty seat list.
* Duplicate seats in a request.
* Non-existent show.
* Non-existent seat.
* Invalid/null request body.
* Authentication and token-derived user identity.
* Health endpoint.
* Prometheus metrics endpoint.

## Project Structure

```text
src/main/java/paytm/com/example
├── Controller
│   └── ShowController.java
├── dto
├── Entity
├── Repository
├── Service
│   └── ShowService.java
├── exception
├── security
│   └── AuthenticatedUserFilter.java
└── PaytmApplication.java
```

## Repository

```text
https://github.com/jananijanarthanamj/paytm-seat-booking.git
```

## Deployment

The application is containerized using Docker and deployed as a publicly accessible Spring Boot service with a managed MySQL database.

The live deployment is available at:

```text
https://incredible-reflection-production-47c0.up.railway.app
```

## Learning Outcomes

This assignment provided practical experience in designing and deploying a backend system where correctness under concurrency is more important than a simple CRUD implementation.

Key areas explored during the implementation include:

* Concurrent request handling
* Database locking
* Transactional consistency
* Idempotent API design
* Per-user concurrency control
* REST API validation
* Authentication
* Docker containerization
* Cloud deployment
* Application health checks
* Prometheus-based observability
* Concurrent API testing
