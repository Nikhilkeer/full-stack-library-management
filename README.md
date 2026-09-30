# 📚 Library Management System

A full-stack Library Management application built with **Spring Boot**, **Spring Security (JWT)** and **MySQL**, with a simple web frontend.

## Features
- JWT authentication (register / login)
- Role-based access: **ADMIN** manages books, authors and members; **MEMBER** borrows and returns books
- Book catalog with live search; cover images are fetched from Open Library by ISBN (a 📕 placeholder is shown when no cover is found)
- Borrow / return flow with automatic available-copies update (transactional)
- Validation and global exception handling with clean JSON errors
- Swagger / OpenAPI documentation
- Unit tests (JUnit 5 + Mockito) and controller tests (MockMvc)

## Tech Stack
Java 23, Spring Boot 4.1, Spring Data JPA (Hibernate), Spring Security + JWT (jjwt), MySQL 8, Maven, Lombok, springdoc-openapi, HTML/CSS/JavaScript

## Getting Started

1. Create the database:
```sql
   CREATE DATABASE library_management;
```
2. Set environment variables:
   - `DB_PASSWORD` – your MySQL password
   - `JWT_SECRET` – a long random string (64+ characters), e.g. generated with `openssl rand -hex 64`
3. Run the application (PowerShell example):
```powershell
   $env:DB_PASSWORD="your_password"
   $env:JWT_SECRET="your_long_random_secret"
   .\mvnw spring-boot:run
```
4. Open:
   - App: http://localhost:8080/login.html
   - Swagger UI: http://localhost:8080/swagger-ui/index.html

## Roles
| Role | Can do |
|------|--------|
| ADMIN | Add/delete books, add authors, view members |
| MEMBER | Browse books, borrow, return, view own borrows |

## Main API Endpoints
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/auth/register, /api/auth/login | Public |
| GET | /api/books, /api/books/{id} | Authenticated |
| POST / PUT / DELETE | /api/books | ADMIN |
| POST | /api/authors | ADMIN |
| POST | /api/borrow-records/borrow | Authenticated |
| PUT | /api/borrow-records/{id}/return | Authenticated |

## Running Tests
```powershell
.\mvnw test
```
Note: `LibraryManagementApplicationTests` starts the full application and needs the database and environment variables above.

## Project Structure
`controller → service → repository → entity`, plus `dto`, `exception`, `config` (security/JWT), and a static frontend under `src/main/resources/static`.