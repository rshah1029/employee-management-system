# Employee Management System

A Java-based employee management system with role-based authentication and MySQL database integration, developed for CSC3350 Software Development course at Georgia State University.

## Features

### Authentication & Authorization
- **HR Admin Role:** Full CRUD operations on employee database
- **Employee Role:** Read-only access to personal information
- Secure login system with session management

### Employee Management
- Search employees by name, DOB, SSN, or employee ID
- Add, update, and delete employee records (HR Admin only)
- View employee information (all users)

### Salary Management
- Update employee salaries with percentage-based calculations
- Apply raises to employees within specific salary ranges
- Example: 3.2% increase for salaries between $58K-$105K

### Reporting Features
- Pay statement history (sorted by most recent)
- Total payroll by job title
- Total payroll by division
- Employee hiring reports by date range

## Tech Stack

- **Language:** Java
- **Database:** MySQL
- **Tools:** DBeaver for database design and management
- **Architecture:** DAO pattern with MVC structure

## Database Schema

The system uses a normalized relational database with 7+ tables:
- `employees` - Core employee information
- `payroll` - Salary and payment records
- `divisions` - Department/division data
- `job_titles` - Job position information
- `address` - Employee addresses (with foreign keys to city/state tables)
- `city` - City reference table
- `state` - State reference table

Primary-foreign key relationships ensure data integrity across all tables.

## Project Structure
```
src/com/company/
├── dao/           # Data Access Objects for database operations
├── model/         # Java model classes (Employee, Payroll, Division, etc.)
├── security/      # Authentication and session management
├── ui/            # Terminal-based user interface
└── util/          # Database connection and utility classes
```

## Setup Instructions

### Prerequisites
- Java JDK 8 or higher
- MySQL Server
- DBeaver (or any MySQL client)

### Database Setup
1. Install MySQL and create a database named `employeeData2`
2. Import the database schema (SQL file coming soon)
3. Configure database connection:
   - Copy `util/DatabaseConnection.example.java` to `util/DatabaseConnection.java`
   - Update the `USER` and `PASSWORD` constants with your MySQL credentials

### Running the Application
1. Clone this repository
2. Navigate to the project directory
3. Compile the project:
```bash
   javac -d bin src/com/company/**/*.java
```
4. Run the application:
```bash
   java -cp bin com.company.ui.EmployeeManagementUI
```

## Usage

### Login
- **HR Admin credentials:** `[Add your admin credentials here]`
- **Employee credentials:** `[Add test employee credentials here]`

### Main Features
1. **Search Employee:** Enter name, DOB, SSN, or employee ID
2. **Update Employee:** Select employee from search results and modify information (HR Admin only)
3. **Update Salaries:** Apply percentage-based raises to salary ranges (HR Admin only)
4. **View Reports:** Access various payroll and employee reports

## Course Information

**Course:** CSC3350 - Software Development  
**Institution:** Georgia State University  
**Semester:** Fall 2025  
**Instructor:** Dr. William Gregory Johnson

## Development Process

This project followed a structured software development lifecycle:
- Requirements analysis and user story development
- UML diagrams (use case, sequence, class diagrams)
- Database schema design with normalization
- Iterative development with code reviews
- Comprehensive testing and validation

## License

This project was developed as part of academic coursework at Georgia State University.