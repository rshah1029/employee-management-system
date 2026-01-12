package com.companyZ.ems.ui;

import com.companyZ.ems.dao.*;
import com.companyZ.ems.model.*;
import com.companyZ.ems.security.Session;
import com.companyZ.ems.util.InputValidator;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeeManagementUI {
    
    private Scanner scanner;
    private UserDAO userDAO;
    private EmployeeDAO employeeDAO;
    private PayrollDAO payrollDAO;
    private DivisionDAO divisionDAO;
    private JobTitleDAO jobTitleDAO;
    
    public EmployeeManagementUI() {
        this.scanner = new Scanner(System.in);
        this.userDAO = new UserDAO();
        this.employeeDAO = new EmployeeDAO();
        this.payrollDAO = new PayrollDAO();
        this.divisionDAO = new DivisionDAO();
        this.jobTitleDAO = new JobTitleDAO();
    }
    
    public void start() {
        System.out.println("=================================================");
        System.out.println("   Welcome to Employee Management System");
        System.out.println("   Company Z - Secure Employee Portal");
        System.out.println("=================================================");
        
        while (true) {
            if (!Session.isLoggedIn()) {
                if (!login()) {
                    System.out.println("Exiting system. Goodbye!");
                    break;
                }
            }
            
            if (Session.isAdmin()) {
                showAdminMenu();
            } else {
                showEmployeeMenu();
            }
        }
        
        scanner.close();
    }
    
    private boolean login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter Employee ID (or 0 to exit): ");
        int empid = scanner.nextInt();
        scanner.nextLine();
        
        if (empid == 0) return false;
        
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        User user = userDAO.authenticate(empid, username, password);
        
        if (user != null) {
            Session.setCurrentUser(user);
            System.out.println("\n✓ Login successful! Welcome, " + username);
            System.out.println("Role: " + user.getRole());
            return true;
        } else {
            System.out.println("\n✗ Invalid credentials. Please try again.");
            return true;
        }
    }
    
    private void showAdminMenu() {
        while (Session.isLoggedIn()) {
            System.out.println("\n=================================================");
            System.out.println("HR ADMIN DASHBOARD - " + Session.getCurrentUser().getUsername());
            System.out.println("=================================================");
            System.out.println("1.  Search Employee");
            System.out.println("2.  Update Employee Information");
            System.out.println("3.  Add New Employee");
            System.out.println("4.  Update Salary by Range");
            System.out.println("5.  View Total Pay by Job Title");
            System.out.println("6.  View Total Pay by Division");
            System.out.println("7.  View Employees Hired in Date Range");
            System.out.println("8.  Logout");
            System.out.println("=================================================");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: searchEmployee(); break;
                case 2: updateEmployee(); break;
                case 3: addNewEmployee(); break;
                case 4: updateSalaryByRange(); break;
                case 5: viewTotalPayByJobTitle(); break;
                case 6: viewTotalPayByDivision(); break;
                case 7: viewEmployeesHiredInRange(); break;
                case 8: 
                    Session.logout();
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void showEmployeeMenu() {
        while (Session.isLoggedIn()) {
            System.out.println("\n=================================================");
            System.out.println("EMPLOYEE DASHBOARD - " + Session.getCurrentUser().getUsername());
            System.out.println("=================================================");
            System.out.println("1.  View My Personal Information");
            System.out.println("2.  View My Pay History");
            System.out.println("3.  Logout");
            System.out.println("=================================================");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: viewPersonalInfo(); break;
                case 2: viewPayHistory(); break;
                case 3:
                    Session.logout();
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void searchEmployee() {
        System.out.println("\n--- SEARCH EMPLOYEE ---");
        System.out.println("Search by:");
        System.out.println("1. Employee ID");
        System.out.println("2. Name");
        System.out.println("3. SSN");
        System.out.print("Select search type: ");
        
        int searchType = scanner.nextInt();
        scanner.nextLine();
        
        String type = "";
        String value = "";
        
        switch (searchType) {
            case 1:
                type = "empid";
                System.out.print("Enter Employee ID: ");
                value = scanner.nextLine();
                break;
            case 2:
                type = "name";
                System.out.print("Enter Name (first or last): ");
                value = scanner.nextLine();
                break;
            case 3:
                type = "ssn";
                System.out.print("Enter SSN (format: XXX-XX-XXXX): ");
                value = scanner.nextLine();
                break;
            default:
                System.out.println("Invalid search type.");
                return;
        }
        
        List<EmployeeDetail> results = employeeDAO.searchEmployees(type, value);
        
        if (results.isEmpty()) {
            System.out.println("\n✗ No records found.");
        } else {
            System.out.println("\n--- SEARCH RESULTS ---");
            displayEmployeeList(results);
        }
    }
    
    private void displayEmployeeList(List<EmployeeDetail> employees) {
        System.out.println(String.format("%-6s %-15s %-15s %-25s %-20s %-25s %-12s",
            "EmpID", "First Name", "Last Name", "Email", "Division", "Job Title", "Salary"));
        System.out.println("=".repeat(130));
        
        for (EmployeeDetail ed : employees) {
            Employee e = ed.getEmployee();
            String divName = ed.getDivision() != null ? ed.getDivision().getName() : "N/A";
            String jobTitle = ed.getJobTitle() != null ? ed.getJobTitle().getJobTitle() : "N/A";
            
            System.out.println(String.format("%-6d %-15s %-15s %-25s %-20s %-25s $%-11.2f",
                e.getEmpid(), e.getFname(), e.getLname(), e.getEmail(),
                divName, jobTitle, e.getSalary()));
        }
    }
    
    private void updateEmployee() {
        System.out.println("\n--- UPDATE EMPLOYEE ---");
        System.out.print("Enter Employee ID to update: ");
        int empid = scanner.nextInt();
        scanner.nextLine();
        
        List<EmployeeDetail> results = employeeDAO.searchEmployees("empid", String.valueOf(empid));
        
        if (results.isEmpty()) {
            System.out.println("✗ Employee not found.");
            return;
        }
        
        Employee emp = results.get(0).getEmployee();
        System.out.println("\nCurrent Information:");
        System.out.println("Name: " + emp.getFullName());
        System.out.println("Email: " + emp.getEmail());
        System.out.println("Salary: $" + emp.getSalary());
        System.out.println("SSN: " + emp.getSsn());
        
        System.out.println("\n--- Update Fields (press Enter to skip) ---");
        
        System.out.print("New First Name [" + emp.getFname() + "]: ");
        String fname = scanner.nextLine();
        if (!fname.isEmpty()) emp.setFname(fname);
        
        System.out.print("New Last Name [" + emp.getLname() + "]: ");
        String lname = scanner.nextLine();
        if (!lname.isEmpty()) emp.setLname(lname);
        
        System.out.print("New Email [" + emp.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            if (InputValidator.isValidEmail(email)) {
                emp.setEmail(email);
            } else {
                System.out.println("✗ Invalid email format. Keeping original.");
            }
        }
        
        System.out.print("New Salary [" + emp.getSalary() + "]: ");
        String salaryStr = scanner.nextLine();
        if (!salaryStr.isEmpty()) {
            try {
                double salary = Double.parseDouble(salaryStr);
                if (InputValidator.isValidSalary(salary)) {
                    emp.setSalary(salary);
                } else {
                    System.out.println("✗ Invalid salary. Keeping original.");
                }
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid number format. Keeping original.");
            }
        }
        
        if (employeeDAO.updateEmployee(emp)) {
            System.out.println("\n✓ Employee data updated successfully!");
        } else {
            System.out.println("\n✗ Failed to update employee data.");
        }
    }
    
    private void updateSalaryByRange() {
        System.out.println("\n--- UPDATE SALARY BY RANGE ---");
        
        double percentage = 0;
        double minSalary = 0;
        double maxSalary = 0;
        
        // Get percentage with error handling
        while (true) {
            try {
                System.out.print("Enter percentage increase (e.g., 3.2 for 3.2%): ");
                String input = scanner.nextLine().trim();
                percentage = Double.parseDouble(input);
                if (percentage > 0 && percentage <= 50) {
                    break;
                }
                System.out.println("✗ Percentage must be between 0 and 50.");
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid number format. Please enter a number like 3.2");
            }
        }
        
        // Get minimum salary with error handling
        while (true) {
            try {
                System.out.print("Minimum Salary (enter as number, e.g., 58000): ");
                String input = scanner.nextLine().trim();
                input = input.toLowerCase().replace("k", "000").replace(",", "");
                minSalary = Double.parseDouble(input);
                if (minSalary > 0) {
                    break;
                }
                System.out.println("✗ Salary must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid format. Enter as number (e.g., 58000 or 58k)");
            }
        }
        
        // Get maximum salary with error handling
        while (true) {
            try {
                System.out.print("Maximum Salary (enter as number, e.g., 105000): ");
                String input = scanner.nextLine().trim();
                input = input.toLowerCase().replace("k", "000").replace(",", "");
                maxSalary = Double.parseDouble(input);
                if (maxSalary > minSalary) {
                    break;
                }
                System.out.println("✗ Maximum salary must be greater than minimum salary.");
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid format. Enter as number (e.g., 105000 or 105k)");
            }
        }
        
        // Get list of employees who will be affected BEFORE the update
        List<Employee> affectedEmployees = employeeDAO.getEmployeesInSalaryRange(minSalary, maxSalary);
        
        if (affectedEmployees.isEmpty()) {
            System.out.println("\n✗ No employees found in salary range $" + 
                              String.format("%.2f", minSalary) + " - $" + 
                              String.format("%.2f", maxSalary));
            return;
        }
        
        // Show preview of affected employees
        System.out.println("\n=== EMPLOYEES TO BE UPDATED ===");
        System.out.println(String.format("%-6s %-20s %-15s %-15s", 
            "EmpID", "Name", "Current Salary", "New Salary"));
        System.out.println("=".repeat(70));
        
        for (Employee emp : affectedEmployees) {
            double newSalary = emp.getSalary() * (1 + percentage / 100);
            System.out.println(String.format("%-6d %-20s $%-14.2f $%-14.2f",
                emp.getEmpid(), 
                emp.getFullName(), 
                emp.getSalary(), 
                newSalary));
        }
        System.out.println("=".repeat(70));
        System.out.println("Total employees to update: " + affectedEmployees.size());
        
        // Confirm update
        System.out.print("\nConfirm salary update of " + percentage + "% for these " + 
                        affectedEmployees.size() + " employee(s)? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("✗ Operation cancelled. No changes made.");
            return;
        }
        
        // Perform the update
        int count = employeeDAO.updateSalaryByRange(percentage, minSalary, maxSalary);
        
        if (count > 0) {
            System.out.println("\n✓ SUCCESS! Salary updated for " + count + " employee(s).");
            
            // Show updated salaries
            System.out.println("\n=== UPDATED SALARIES ===");
            List<Employee> updatedEmployees = employeeDAO.getEmployeesInSalaryRange(
                minSalary * (1 + percentage / 100), 
                maxSalary * (1 + percentage / 100)
            );
            
            System.out.println(String.format("%-6s %-20s %-15s", 
                "EmpID", "Name", "New Salary"));
            System.out.println("=".repeat(50));
            
            for (Employee emp : updatedEmployees) {
                System.out.println(String.format("%-6d %-20s $%-14.2f",
                    emp.getEmpid(), 
                    emp.getFullName(), 
                    emp.getSalary()));
            }
            System.out.println("=".repeat(50));
        } else {
            System.out.println("\n✗ No employees were updated. Please check your salary range.");
        }
    }
    
    private void addNewEmployee() {
        System.out.println("\n--- ADD NEW EMPLOYEE ---");
        
        System.out.print("Employee ID: ");
        int empid = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("First Name: ");
        String fname = scanner.nextLine();
        
        System.out.print("Last Name: ");
        String lname = scanner.nextLine();
        
        String email;
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine();
            if (InputValidator.isValidEmail(email)) break;
            System.out.println("✗ Invalid email format. Try again.");
        }
        
        Date hireDate;
        while (true) {
            System.out.print("Hire Date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine();
            try {
                hireDate = InputValidator.parseDate(dateStr);
                break;
            } catch (ParseException e) {
                System.out.println("✗ Invalid date format. Try again.");
            }
        }
        
        double salary;
        while (true) {
            System.out.print("Salary: ");
            salary = scanner.nextDouble();
            scanner.nextLine();
            if (InputValidator.isValidSalary(salary)) break;
            System.out.println("✗ Invalid salary. Try again.");
        }
        
        String ssn;
        while (true) {
            System.out.print("SSN (XXX-XX-XXXX): ");
            ssn = scanner.nextLine();
            if (InputValidator.isValidSSN(ssn)) break;
            System.out.println("✗ Invalid SSN format. Try again.");
        }
        
        // Select Division
        List<Division> divisions = divisionDAO.getAllDivisions();
        System.out.println("\nAvailable Divisions:");
        for (Division d : divisions) {
            System.out.println(d.getId() + ". " + d.getName());
        }
        System.out.print("Select Division ID: ");
        int divId = scanner.nextInt();
        scanner.nextLine();
        
        // Select Job Title
        List<JobTitle> jobTitles = jobTitleDAO.getAllJobTitles();
        System.out.println("\nAvailable Job Titles:");
        for (JobTitle jt : jobTitles) {
            System.out.println(jt.getJobTitleId() + ". " + jt.getJobTitle());
        }
        System.out.print("Select Job Title ID: ");
        int jobTitleId = scanner.nextInt();
        scanner.nextLine();
        
        Employee newEmp = new Employee(empid, fname, lname, email, hireDate, salary, ssn);
        
        if (employeeDAO.insertEmployee(newEmp, divId, jobTitleId)) {
            System.out.println("\n✓ New employee added successfully!");
        } else {
            System.out.println("\n✗ Failed to add new employee.");
        }
    }
    
    private void viewTotalPayByJobTitle() {
        System.out.println("\n--- TOTAL PAY BY JOB TITLE ---");
        
        System.out.print("Enter Month (1-12): ");
        int month = scanner.nextInt();
        
        System.out.print("Enter Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        
        Map<String, Double> results = payrollDAO.getTotalPayByJobTitle(month, year);
        
        if (results.isEmpty()) {
            System.out.println("\n✗ No payroll data found for the specified period.");
        } else {
            System.out.println("\n--- Report: " + month + "/" + year + " ---");
            System.out.println(String.format("%-35s %15s", "Job Title", "Total Pay"));
            System.out.println("=".repeat(52));
            
            for (Map.Entry<String, Double> entry : results.entrySet()) {
                System.out.println(String.format("%-35s $%14.2f", 
                    entry.getKey(), entry.getValue()));
            }
        }
    }
    
    private void viewTotalPayByDivision() {
        System.out.println("\n--- TOTAL PAY BY DIVISION ---");
        
        System.out.print("Enter Month (1-12): ");
        int month = scanner.nextInt();
        
        System.out.print("Enter Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        
        Map<String, Double> results = payrollDAO.getTotalPayByDivision(month, year);
        
        if (results.isEmpty()) {
            System.out.println("\n✗ No payroll data found for the specified period.");
        } else {
            System.out.println("\n--- Report: " + month + "/" + year + " ---");
            System.out.println(String.format("%-35s %15s", "Division", "Total Pay"));
            System.out.println("=".repeat(52));
            
            for (Map.Entry<String, Double> entry : results.entrySet()) {
                System.out.println(String.format("%-35s $%14.2f", 
                    entry.getKey(), entry.getValue()));
            }
        }
    }
    
    private void viewEmployeesHiredInRange() {
        System.out.println("\n--- EMPLOYEES HIRED IN DATE RANGE ---");
        
        Date startDate, endDate;
        
        try {
            System.out.print("Start Date (YYYY-MM-DD): ");
            startDate = InputValidator.parseDate(scanner.nextLine());
            
            System.out.print("End Date (YYYY-MM-DD): ");
            endDate = InputValidator.parseDate(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("✗ Invalid date format.");
            return;
        }
        
        List<Employee> results = employeeDAO.getEmployeesHiredInRange(startDate, endDate);
        
        if (results.isEmpty()) {
            System.out.println("\n✗ No employees hired in this date range.");
        } else {
            System.out.println("\n--- Employees Hired: " + 
                InputValidator.formatDate(startDate) + " to " + 
                InputValidator.formatDate(endDate) + " ---");
            System.out.println(String.format("%-6s %-15s %-15s %-25s %-12s %-12s",
                "EmpID", "First Name", "Last Name", "Email", "Hire Date", "Salary"));
            System.out.println("=".repeat(105));
            
            for (Employee e : results) {
                System.out.println(String.format("%-6d %-15s %-15s %-25s %-12s $%-11.2f",
                    e.getEmpid(), e.getFname(), e.getLname(), e.getEmail(),
                    InputValidator.formatDate(e.getHireDate()), e.getSalary()));
            }
        }
    }
    
    private void viewPersonalInfo() {
        int empid = Session.getCurrentUser().getEmpid();
        List<EmployeeDetail> results = employeeDAO.searchEmployees("empid", String.valueOf(empid));
        
        if (results.isEmpty()) {
            System.out.println("✗ Unable to retrieve your information.");
            return;
        }
        
        EmployeeDetail detail = results.get(0);
        Employee emp = detail.getEmployee();
        
        System.out.println("\n=================================================");
        System.out.println("        YOUR PERSONAL INFORMATION");
        System.out.println("=================================================");
        System.out.println("Employee ID:   " + emp.getEmpid());
        System.out.println("Name:          " + emp.getFullName());
        System.out.println("Email:         " + emp.getEmail());
        System.out.println("Hire Date:     " + InputValidator.formatDate(emp.getHireDate()));
        System.out.println("Salary:        $" + String.format("%.2f", emp.getSalary()));
        System.out.println("SSN:           " + emp.getSsn());
        if (detail.getDivision() != null) {
            System.out.println("Division:      " + detail.getDivision().getName());
        }
        if (detail.getJobTitle() != null) {
            System.out.println("Job Title:     " + detail.getJobTitle().getJobTitle());
        }
        System.out.println("=================================================");
    }
    
    private void viewPayHistory() {
        int empid = Session.getCurrentUser().getEmpid();
        List<Payroll> history = payrollDAO.getPayHistoryForEmployee(empid);
        
        if (history.isEmpty()) {
            System.out.println("\n✗ No pay history found.");
            return;
        }
        
        System.out.println("\n=================================================");
        System.out.println("        YOUR PAY HISTORY");
        System.out.println("=================================================");
        System.out.println(String.format("%-12s %-12s %-12s %-12s",
            "Pay Date", "Gross Pay", "Deductions", "Net Pay"));
        System.out.println("=".repeat(50));
        
        for (Payroll pay : history) {
            System.out.println(String.format("%-12s $%-11.2f $%-11.2f $%-11.2f",
                InputValidator.formatDate(pay.getPayDate()),
                pay.getEarnings(),
                pay.getTotalDeductions(),
                pay.getNetPay()));
        }
        System.out.println("=================================================");
    }
    
    // MAIN METHOD - Entry point of application
    public static void main(String[] args) {
        EmployeeManagementUI ui = new EmployeeManagementUI();
        ui.start();
    }
}