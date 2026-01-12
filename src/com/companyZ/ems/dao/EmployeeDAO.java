package com.companyZ.ems.dao;

import com.companyZ.ems.model.*;
import com.companyZ.ems.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeDAO {
    
    public List<EmployeeDetail> searchEmployees(String searchType, String searchValue) {
        List<EmployeeDetail> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT e.*, d.ID as div_id, d.Name as div_name, " +
            "jt.job_title_id, jt.job_title " +
            "FROM employees e " +
            "LEFT JOIN employee_division ed ON e.empid = ed.empid " +
            "LEFT JOIN division d ON ed.div_ID = d.ID " +
            "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
            "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
            "WHERE ");
        
        switch (searchType.toLowerCase()) {
            case "empid":
                sql.append("e.empid = ?");
                break;
            case "name":
                sql.append("(e.Fname LIKE ? OR e.Lname LIKE ?)");
                break;
            case "ssn":
                sql.append("e.SSN = ?");
                break;
            case "dob":
                sql.append("e.HireDate = ?");
                break;
            default:
                return results;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            if (searchType.equalsIgnoreCase("name")) {
                String pattern = "%" + searchValue + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
            } else if (searchType.equalsIgnoreCase("empid")) {
                stmt.setInt(1, Integer.parseInt(searchValue));
            } else {
                stmt.setString(1, searchValue);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmpid(rs.getInt("empid"));
                emp.setFname(rs.getString("Fname"));
                emp.setLname(rs.getString("Lname"));
                emp.setEmail(rs.getString("email"));
                emp.setHireDate(rs.getDate("HireDate"));
                emp.setSalary(rs.getDouble("Salary"));
                emp.setSsn(rs.getString("SSN"));
                
                Division div = new Division();
                div.setId(rs.getInt("div_id"));
                div.setName(rs.getString("div_name"));
                
                JobTitle jt = new JobTitle();
                jt.setJobTitleId(rs.getInt("job_title_id"));
                jt.setJobTitle(rs.getString("job_title"));
                
                EmployeeDetail detail = new EmployeeDetail(emp, null, div, jt);
                results.add(detail);
            }
            
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }
        
        return results;
    }
    
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET Fname=?, Lname=?, email=?, Salary=?, SSN=? WHERE empid=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, employee.getFname());
            stmt.setString(2, employee.getLname());
            stmt.setString(3, employee.getEmail());
            stmt.setDouble(4, employee.getSalary());
            stmt.setString(5, employee.getSsn());
            stmt.setInt(6, employee.getEmpid());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Update error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean insertEmployee(Employee employee, int divisionId, int jobTitleId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert into employees table
            String empSql = "INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement empStmt = conn.prepareStatement(empSql);
            empStmt.setInt(1, employee.getEmpid());
            empStmt.setString(2, employee.getFname());
            empStmt.setString(3, employee.getLname());
            empStmt.setString(4, employee.getEmail());
            empStmt.setDate(5, new java.sql.Date(employee.getHireDate().getTime()));
            empStmt.setDouble(6, employee.getSalary());
            empStmt.setString(7, employee.getSsn());
            empStmt.executeUpdate();
            
            // Insert into employee_division
            String divSql = "INSERT INTO employee_division (empid, div_ID) VALUES (?, ?)";
            PreparedStatement divStmt = conn.prepareStatement(divSql);
            divStmt.setInt(1, employee.getEmpid());
            divStmt.setInt(2, divisionId);
            divStmt.executeUpdate();
            
            // Insert into employee_job_titles
            String jobSql = "INSERT INTO employee_job_titles (empid, job_title_id) VALUES (?, ?)";
            PreparedStatement jobStmt = conn.prepareStatement(jobSql);
            jobStmt.setInt(1, employee.getEmpid());
            jobStmt.setInt(2, jobTitleId);
            jobStmt.executeUpdate();
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback error: " + ex.getMessage());
                }
            }
            System.err.println("Insert error: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }
    
    public int updateSalaryByRange(double percentage, double minSalary, double maxSalary) {
        String sql = "UPDATE employees SET Salary = Salary * (1 + ? / 100) " +
                    "WHERE Salary >= ? AND Salary < ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, percentage);
            stmt.setDouble(2, minSalary);
            stmt.setDouble(3, maxSalary);
            
            return stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Salary update error: " + e.getMessage());
            return 0;
        }
    }
    
    public List<Employee> getEmployeesHiredInRange(Date startDate, Date endDate) {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE HireDate BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmpid(rs.getInt("empid"));
                emp.setFname(rs.getString("Fname"));
                emp.setLname(rs.getString("Lname"));
                emp.setEmail(rs.getString("email"));
                emp.setHireDate(rs.getDate("HireDate"));
                emp.setSalary(rs.getDouble("Salary"));
                emp.setSsn(rs.getString("SSN"));
                results.add(emp);
            }
            
        } catch (SQLException e) {
            System.err.println("Date range search error: " + e.getMessage());
        }
        
        return results;
    }

    public List<Employee> getEmployeesInSalaryRange(double minSalary, double maxSalary) {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE Salary >= ? AND Salary < ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, minSalary);
            stmt.setDouble(2, maxSalary);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmpid(rs.getInt("empid"));
                emp.setFname(rs.getString("Fname"));
                emp.setLname(rs.getString("Lname"));
                emp.setEmail(rs.getString("email"));
                emp.setHireDate(rs.getDate("HireDate"));
                emp.setSalary(rs.getDouble("Salary"));
                emp.setSsn(rs.getString("SSN"));
                results.add(emp);
            }
            
        } catch (SQLException e) {
            System.err.println("Salary range query error: " + e.getMessage());
        }
        
        return results;
    }
}