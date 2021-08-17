# Bank kata factory14

## 1 Description
This app is used for:
- Register a user in the bank
- Register money account for a user ( using preferred currency )
- Top-up / Withdraw operations
- Transfer money from one user account to another.

### 1.1 Used technologies

- Java 11
- H2 in-memory DB [`H2 DB`](https://www.h2database.com/html/main.html)
- Javalin [`Simple web framework`](https://javalin.io)
- Liquibase [`DB managger`](https://www.liquibase.org/)

### 1.2 Used design principles

- CQRS
- Event sourcing

### 1.3 Architecture design schema

![image](cqrs_schema.png)