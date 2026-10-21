# MediFind Backend

**Course:** INSY 8322 – Web Technology and Internet  
**Lecturer:** Patrick DUSHIMIMANA  
**Student Name:** Fabrice Mukunzi  
**Student ID:** 29620  
**Institution:** Adventist University of Central Africa (AUCA)  
**Academic Year:** 2026-2027 (1)  

---

## 1. Project Overview

MediFind helps people in Rwanda locate nearby pharmacies that have their prescribed medications in stock. A customer submits a medicine request; the request is matched to an available pharmacy serving a particular service area and holding designated medicine classes (e.g., Antibiotics, Antimalarials, Painkillers). Once fulfilled, the pharmacy records the items dispensed.

The backend is built strictly following AUCA class conventions:
- **No DTOs & No try/catch**: Entities are used directly as request and response payloads.
- **Service/Controller Architecture**: Services return `List`, `Optional`, or `String` messages. Controllers evaluate returned messages with `if/else` conditions and map them to their corresponding `HttpStatus`.
- **Query Type Adherence**:
  - Requirements marked **DERIVED** use Spring Data JPA method name conventions exclusively (`IgnoreCase`, `OrderBy`, `Between`, `existsBy`, etc.).
  - Requirements marked **JPQL** use `@Query` annotations exclusively (`JOIN`, `LOWER`, `NOT EXISTS`, `IS EMPTY`, `DISTINCT`, `GROUP BY`, `@Modifying`, `@Transactional`).
- **Entity Identification**: All entity identifiers are JPA-generated `UUID`s.

---

## 2. Domain Model & Entities

| Entity | Table | Fields | Relationships |
|---|---|---|---|
| `MedicineClass` | `medicine_class` | `UUID id`, `String name` | `@ManyToMany(mappedBy = "medicineClasses") List<Pharmacy> pharmacies` (marked `@JsonIgnore`) |
| `ServiceArea` | `service_area` | `UUID id`, `int areaNumber`, `String name` | None (independent) |
| `Pharmacy` | `pharmacy` | `UUID id`, `String name`, `String street`, `String phone` | `@ManyToOne ServiceArea serviceArea`, `@ManyToMany List<MedicineClass> medicineClasses` via `pharmacy_medicine_class` |
| `Customer` | `customer` | `UUID id`, `String firstName`, `String lastName`, `String nationalId` (unique), `String phone` | None |
| `MedicineRequest` | `medicine_request` | `UUID id`, `LocalDate requestDate`, `RequestStatus status` (`@Enumerated(EnumType.STRING)`), `String medicineName` | `@ManyToOne Customer customer`, `@ManyToOne Pharmacy pharmacy` |
| `DispensedItem` | `dispensed_item` | `UUID id`, `String medicineName`, `String dosage`, `int quantity`, `LocalDate dispensedDate` | `@ManyToOne MedicineRequest medicineRequest` |

---

## 3. Implemented Requirements Matrix

| ID | Requirement | Endpoint | Query Type / Implementation |
|---|---|---|---|
| **A1** | Entities & Relationships | - | 5 Entities + Enum with JPA mappings, foreign keys, `@JsonIgnore` to prevent infinite loops |
| **A2** | CRUD for Entities | `POST /save`, `GET /all`, `GET /{id}`, `PUT /update/{id}`, `DELETE /delete/{id}` | Standard JPA repositories, HTTP 201, 200, 404 mappings |
| **B1** | Find customers by lastName | `GET /api/customers/by-last-name?lastName=...` | **DERIVED**: `findByLastNameIgnoreCaseOrderByFirstNameAsc` |
| **B2** | Medicine requests by status | `GET /api/medicine-requests/by-status?status=...` | **DERIVED**: `findByStatusOrderByRequestDateAsc` |
| **B3** | Medicine requests in date range | `GET /api/medicine-requests/between?start=...&end=...` | **DERIVED**: `findByRequestDateBetweenOrderByRequestDateAsc` |
| **C1** | Pharmacies by medicine class | `GET /api/pharmacies/by-medicine-class?name=...` | **JPQL**: `@Query` with collection `JOIN` and `LOWER()` |
| **C2** | Free service areas | `GET /api/service-areas/free` | **JPQL**: `@Query` with `NOT EXISTS` subquery |
| **C3** | Medicine classes nobody holds | `GET /api/medicine-classes/unused` | **JPQL**: `@Query` with `IS EMPTY` |
| **C4** | Customers of a pharmacy | `GET /api/customers/of-pharmacy/{pharmacyId}` | **JPQL**: `@Query` with `DISTINCT` path navigation, 404 validation |
| **D1** | Safe saving | `POST /api/medicine-requests/save` | Business Rule + **DERIVED**: `existsByPharmacyAndRequestDateAndStatusNot`, validates past dates (400) and conflicts (409) |
| **D2** | Status flow | `PATCH /api/medicine-requests/{id}/status?status=...` | State machine validation (`SUBMITTED` -> `MATCHED`/`DECLINED` -> `FULFILLED`), general PUT update preserves status |
| **D3** | Add medicine class to pharmacy | `PUT /api/pharmacies/{pharmacyId}/medicine-classes/{classId}` | Many-to-Many collection management, duplicate check (409) |
| **D4** | Move pharmacy to service area | `PATCH /api/pharmacies/{pharmacyId}/service-area?areaNumber=...` | Business Rule + **DERIVED**: `findByAreaNumber`, `existsByServiceArea` |
| **E1** | Write dispensed item | `POST /api/dispensed-items/save?medicineRequestId=...` | Checks request exists (404) and is `FULFILLED` (400), sets `dispensedDate` to today |
| **E2** | Dispensed item history of customer | `GET /api/dispensed-items/customer/{nationalId}` | **JPQL**: Path navigation `d.medicineRequest.customer.nationalId`, `ORDER BY d.dispensedDate DESC` |
| **E3** | Delete and protect | `DELETE /api/dispensed-items/delete/{id}` & modified Request DELETE | **DERIVED**: `existsByMedicineRequest`, blocks request delete with 409 if items exist |
| **F1** | Requests per status stats | `GET /api/medicine-requests/stats/by-status` | **JPQL**: `GROUP BY mr.status, COUNT(mr)` returning `List<Object[]>` |
| **F2** | Pharmacy is absent | `PATCH /api/medicine-requests/cancel-day?pharmacyId=...&date=...` | **JPQL**: Bulk `UPDATE` with `@Modifying` and `@Transactional` |
| **+5** | Dashboard summary | `GET /api/dashboard/summary` | Reuses C2, `count()`, `countByStatus`, returns `LinkedHashMap<String, Long>` |
| **+5** | Pagination | `GET /api/medicine-requests/page?page=0&size=5&sort=requestDate,desc` | Spring Data `Pageable` with total pages and elements |

---

## 4. Prerequisites & Setup

- **Java Development Kit (JDK):** Version 21
- **Apache Maven:** 3.9+
- **PostgreSQL Database:** PostgreSQL 15+ (running on port 5432)

### 4.1 Database Configuration

Ensure PostgreSQL is running and create the `medifind_db` database:

```sql
CREATE DATABASE medifind_db;
```

Verify `src/main/resources/application.properties`:
```properties
spring.application.name=medi-find
spring.datasource.url=jdbc:postgresql://localhost:5432/medifind_db
spring.datasource.username=postgres
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

---

## 5. Running the Application

### 5.1 Build and Run
Clone the repository and run:
```bash
mvn clean install
mvn spring-boot:run
```
The server will start on `http://localhost:8080`.

### 5.2 Seeding Seed Data (Step 0)
Run the automated seed script to populate initial testing data:
```powershell
powershell -ExecutionPolicy Bypass -File .\seed.ps1
```

The script initializes:
- **4 Medicine Classes**: Antibiotics, Painkillers, Antimalarials, Vitamins (Vitamins unused for C3).
- **4 Service Areas**: Areas 1, 2, 3, 4 (Area 4 has no pharmacy for C2).
- **3 Pharmacies**: Pharmacie Conseil, Kigali Pharma, LifeCare Pharmacy.
- **5 Customers**: Including 2 sharing lastName "Umutoni" (B1) and customer with nationalId `1199780054321098` (E2).
- **10 Medicine Requests**: Spread over October/November 2026 across all 4 statuses (`SUBMITTED`, `MATCHED`, `FULFILLED`, `DECLINED`).

---

## 6. Postman Collection

Import the included collection from the repository root:
`medi-find-midterm.postman_collection.json`

The collection is organized by examination sections (`Part A`, `Part B`, `Part C`, `Part D`, `Part E`, `Part F`, `Bonus`) with corresponding IDs and tests for both success (HTTP 200/201) and error responses (HTTP 400, 404, 409).
