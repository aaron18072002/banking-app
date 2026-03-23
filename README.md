# Core Banking API 🏦

A robust, secure, and highly scalable RESTful API built to handle core banking operations. Currently architected as a well-structured monolith with a clear roadmap for transitioning into a distributed microservices architecture.

## 🚀 Features

* **Identity & Access Management (IAM):** * Granular Role-Based Access Control (RBAC) separating Customers, Tellers, Branch Managers, and Admins.
    * Strict entity validation (Regex patterns, Custom Age Limit validation for legal compliance).
    * eKYC-ready data structures handling National Identity numbers and demographic data.
* **Enterprise-Grade Security:**
    * Stateless authentication using JWT (JSON Web Tokens).
    * Secure password hashing using BCrypt.
    * Standardized, consistent API error handling (`@RestControllerAdvice`).
* **Data Integrity:**
    * Optimized Database queries avoiding `LazyInitializationException` using `@EntityGraph` and `JOIN FETCH`.

## 🛠 Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security 6
* **Persistence:** Spring Data JPA / Hibernate
* **Database:** MySQL
* **Utilities:** Lombok, Jakarta Validation, MapStruct (optional)

## 🗺 Roadmap & Future Architecture

While currently operating as a monolithic application for rapid development and data consistency, the system is designed with domain isolation in mind.

**Upcoming Milestones:**
- [ ] Implement JWT Token generation and validation filters.
- [ ] Build internal transaction features (Transfer, Deposit, Withdrawal).
- [ ] **Phase 2:** Refactor the monolith into a Microservices architecture.
- [ ] **Phase 3:** Integrate **Apache Kafka** for asynchronous event-driven communication between services (e.g., Notification Service, Audit Service).

## 💻 Getting Started

### Prerequisites
* JDK 17 or higher
* Maven 3.8+
* MySQL Server 8.0+
* IDE (IntelliJ IDEA recommended with Lombok Plugin enabled)

