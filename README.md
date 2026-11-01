# MediFind Backend

**Project:** MediFind  
**Student Name:** Fabrice Mukunzi  
**Student ID:** 29620  

MediFind helps people in Rwanda find a nearby pharmacy that has their prescribed medicines in stock. This is its REST API, built with Spring Boot, Spring Data JPA and PostgreSQL.

## Prerequisites

- JDK 21
- Maven 3.9+
- PostgreSQL 15+ on port 5432

## Database

Create the database:

```sql
CREATE DATABASE medifind_db;
```

Then set your own PostgreSQL username and password in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medifind_db
spring.datasource.username=postgres
spring.datasource.password=password
```

## Run

```bash
mvn clean install
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. Tables are created automatically (`ddl-auto=update`).

## Seed Data (Step 0)

With the application running, the seed data is entered through the API's own `POST /api/{path}/save` endpoints, for example with the A2 save requests in the Postman collection:

- 4 medicine classes: Antibiotics, Painkillers, Antimalarials, Vitamins (nobody holds Vitamins)
- 4 service areas numbered 1 to 4 (area 4 has no pharmacy)
- 3 pharmacies: Pharmacie Conseil, Kigali Pharma, LifeCare Pharmacy
- 5 customers, two with lastName "Umutoni", one with nationalId `1199780054321098`
- 10 medicine requests over October and November 2026, using all four statuses

Notes:
- `nationalId` is unique, so a customer cannot be saved twice.
- The seed data was entered before the D1 business rules were added. Since D1, `POST /api/medicine-requests/save` rejects requests dated before today (400) and saves every new request as `SUBMITTED`. Use `PATCH /api/medicine-requests/{id}/status?status=...` (D2) to move requests through the allowed status flow.

## Postman Collection

Import `medi-find-midterm.postman_collection.json` from the repository root. Requests are grouped by part and named by requirement ID (A2, B1, C4, D2, ...), covering both success and error responses (400, 404, 409).
