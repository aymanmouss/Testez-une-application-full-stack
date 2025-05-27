# Backend - Yoga Application API

## 🛠️ Technical Stack
- **Framework**: Spring Boot
- **Database**: MySQL
- **Testing**: JUnit, Mockito, Jacoco
- **Language**: Java 11

## 📋 Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+

## 🚀 Installation & Setup

### 1. Clone the project
```bash
git clone https://github.com/aymanmouss/Testez-une-application-full-stack
cd back
```

### 2. Install dependencies
```bash
mvn clean install
```

### 3. Database Setup
Connect to MySQL:
```bash
mysql -u root -p
```

Create the database:
```sql
CREATE DATABASE test;
USE test;
```

Run the SQL script:
```bash
# Execute the script located at: resources/sql/script.sql
mysql -u root -p test < src/main/resources/sql/script.sql
```

### 4. Start the application
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## 🧪 Testing

### Run Tests
Execute all tests:
```bash
mvn clean test
```

### Generate Coverage Report
Generate Jacoco coverage report:
```bash
mvn jacoco:report
```

Coverage report will be available at: `target/site/jacoco/index.html`

## 📊 Test Coverage
The project uses:
- **JUnit 5** for unit testing
- **Mockito** for mocking dependencies
- **Jacoco** for code coverage analysis

## 🗃️ Database Configuration
Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=your_username
spring.datasource.password=your_password
```