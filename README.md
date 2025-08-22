# ING Hubs Loan API

A RESTful API for managing loans, customers, and installments. Built using Java 21, Spring Boot 3.5, JPA, Spring Security, and OpenAPI (Swagger) documentation.

---

## Table of Contents

* [Features](#features)
* [Technologies](#technologies)
* [Getting Started](#getting-started)
* [API Endpoints](#api-endpoints)
* [Validation & Exceptions](#validation--exceptions)
* [Security](#security)
* [Swagger Documentation](#swagger-documentation)
* [Project Structure](#project-structure)
* [Testing / Curl Examples](#testing--curl-examples)
* [Notes](#notes)

---

## Features

* Create, list, and manage loans
* Track installments and payment status
* Validate loan creation rules:

  * Interest rate between 0.1–0.5
  * Number of installments: 6, 9, 12, 24
  * Installments amount equally distributed
  * Due date on the first day of the month
* Pay installments with constraints:

  * Earliest installments are paid first
  * Multiple installments can be paid if amount suffices
  * Cannot pay installments more than 3 months in advance
* Global exception handling for API errors
* Role-based access: `ADMIN`, `EMPLOYEE`, `EMPLOYEE`

---

## Technologies

* Java 21
* Spring Boot 3.5
* Spring Data JPA / Hibernate
* Spring Security
* H2 in-memory database
* Lombok
* Springdoc OpenAPI / Swagger UI
* Maven

---

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/kubracaliskan-94/ing-hubs-loan-api.git
cd ing-hubs-loan-api
```

2. Build and run the application:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

3. Access the H2 console:

```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:inghubsloandb
```

4. Access Swagger UI for API docs:

```
http://localhost:8080/swagger-ui/index.html
```

#### Default Users for Testing

| Role      | Username   | Password    |
|-----------|-----------|------------|
| ADMIN     | admin      | admin123   |
| CUSTOMER  | customer1  | customer123|
| CUSTOMER  | customer2  | customer123|

> Note: Customers can only access **their own loans and installments**. Admin has full access.

---

## API Endpoints

### Customers

* `GET /api/v1/customers` – List all customers

### Loans

## 🔧 API Endpoints

| Method | Endpoint | Description | Auth/Access |
|--------|----------|-------------|-------------|
| POST   | `/api/v1/loans` | Create a new loan | ADMIN, EMPLOYEE |
| POST   | `/api/v1/loans/{loanId}/pay` | Pay installments of a loan | ADMIN, EMPLOYEE, CUSTOMER (only own loan) |
| GET    | `/api/v1/loans/{loanId}` | Retrieve loan by ID | ADMIN, EMPLOYEE, CUSTOMER (only own loan) |
| GET    | `/api/v1/customers/{customerId}/loans/` | Retrieve loans by Customer ID | ADMIN, EMPLOYEE, CUSTOMER (only own loans) |
| GET    | `/api/v1/loans/{loanId}/installments` | List installments of a loan | ADMIN, EMPLOYEE, CUSTOMER (only own loan) |
| GET    | `/api/v1/loans` | List all loans | ADMIN, EMPLOYEE |


**Note:** Customers can **only access their own data**. They cannot retrieve loans or installments belonging to other customers. This is enforced through AOP-based authorization checks (`@CheckLoanAccessForCustomerId` / `@CheckLoanAccessForLoanId`).

---
##  Test the API

You can test the API using **Swagger UI** or **cURL** with basic authentication.

### 1 Using Swagger UI

1. Open [Swagger UI](http://localhost:8080/swagger-ui/index.html).
2. Click **Authorize** and log in using one of the default users.
3. Execute endpoints directly from the UI.

### 2 Using cURL

#### Create a Loan (Basic Auth)

```bash
curl -u username:password -X POST "http://localhost:8080/api/v1/loans" \
  -H "Content-Type: application/json" \
  -d '{
        "customerId": 1,
        "loanAmount": 5000,
        "numberOfInstallments": 12,
        "interestRate": 0.15
      }'
```

**Sample Response:**

```json
{
  "id": 1,
  "customerId": 1,
  "loanAmount": 5000,
  "numberOfInstallments": 12,
  "interestRate": 0.15,
  "isPaid": false,
  "createDate": "2025-08-22T23:53:23.9505605",
  "updateDate": "2025-08-22T23:53:23.9505605"
}
```

> You can use **customer1** credentials to test customer-specific endpoints. Customers will only see their own loans. Admins can access all loans.


## Validation & Exceptions

* Loan creation and payment validated using **strategy pattern**
* Global exception handler for:

  * `NoInstallmentsPayableException`
  * `CustomerLimitExceededException`
  * `InvalidInstallmentCountException`
  * `InvalidInterestRateException`
  * `ResourceNotFoundException`
  * `AccessDeniedException`
  * `LoanAlreadyPaidException`
  * `MethodArgumentNotValidException`


Example Global Exception Handler:

```java
@RestControllerAdvice
public class LoanApiGlobalExceptionHandler {

  @ExceptionHandler({
          CustomerLimitExceededException.class,
          InvalidInterestRateException.class,
          InvalidInstallmentCountException.class,
          CustomerLimitExceededException.class
  })
  public ResponseEntity<ApiErrorResponse> handleLoanCreationExceptions(RuntimeException ex, HttpServletRequest request) {
    log.error("Error occurred at {}", request.getRequestURI(), ex);
    return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  @ExceptionHandler({
          LoanAlreadyPaidException.class,
          NoInstallmentsPayableException.class
  })
  public ResponseEntity<ApiErrorResponse> handlePayLoanExceptions(RuntimeException ex, HttpServletRequest request) {
    log.error("Error occurred at {}", request.getRequestURI(), ex);
    return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
    log.error("Error occurred at {}", request.getRequestURI(), ex);
    return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
  }

  private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }
}
```

---

## Security

* Role-based access:

  * `ADMIN` – Full access
  * `EMPLOYEE` – Create and view loans/customers/installments
  * `CUSTOMER` – Can pay and view their own loans and installments 
* Anonymous requests blocked for sensitive endpoints

---

## Swagger Documentation

* OpenAPI docs generated using Springdoc
* Access Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

* API models and endpoints documented with `@Operation` annotations

---

## Project Structure

```
src/main/java/com/ing/hubs/loan/api
│
├── aop/             # AOP-based authorization (e.g.)
├── config/          # App configurations (security, Swagger, etc.)
├── controller/      # REST controllers
├── exception/       # Custom exceptions & global handlers
├── initializer/     # Startup logic 
├── listener/        # Event listeners
├── mapper/          # Entity <-> DTO mappers
├── model/           # Domain models
│   ├── dto/         # Data Transfer Objects
│   ├── entity/      # JPA entities
│   └── enums/       # Enums
├── repository/      # Spring Data repositories
├── security/        # Security configuration (annotations, access checks)
├── service/         # Business services
└── validation/      # Validation (context, rules, executors)

```
## Planned Improvements

### 1. Idempotency
- Add `Idempotency-Key` header support to prevent duplicate loan payments  
  (e.g., when the same request is retried due to network issues).

### 2. Test Coverage ≥ 80%
- Improve existing unit and integration tests.
- Ensure full coverage for Service and Controller layers.
- Integrate JaCoCo or SonarQube for reporting.

### 3. Migration to JWT Authentication from Basic Authentication
- Generate JWT token upon login.
- Secure API requests with `Authorization: Bearer <token>`.
- Add refresh token mechanism for long-lived sessions.
- Extend Spring Security configuration with JWT filters.

### 4. Logging
- Add centralized logging for all API requests and responses.
- Include request IDs for traceability.
- Log critical errors and validation failures for monitoring and debugging.


---