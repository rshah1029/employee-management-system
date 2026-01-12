package com.companyZ.ems.dao;

import com.companyZ.ems.model.Payroll;
import com.companyZ.ems.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayrollDAO {
    
    public List<Payroll> getPayHistoryForEmployee(int empid) {
        List<Payroll> history = new ArrayList<>();
        String sql = "SELECT * FROM payroll WHERE empid = ? ORDER BY pay_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, empid);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Payroll pay = new Payroll();
                pay.setPayID(rs.getInt("payID"));
                pay.setPayDate(rs.getDate("pay_date"));
                pay.setEmpid(rs.getInt("empid"));
                pay.setEarnings(rs.getDouble("earnings"));
                pay.setFedTax(rs.getDouble("fed_tax"));
                pay.setFedMed(rs.getDouble("fed_med"));
                pay.setFedSS(rs.getDouble("fed_SS"));
                pay.setStateTax(rs.getDouble("state_tax"));
                pay.setRetire401k(rs.getDouble("retire_401k"));
                pay.setHealthCare(rs.getDouble("health_care"));
                history.add(pay);
            }
            
        } catch (SQLException e) {
            System.err.println("Pay history error: " + e.getMessage());
        }
        
        return history;
    }
    
    public Map<String, Double> getTotalPayByJobTitle(int month, int year) {
        Map<String, Double> results = new HashMap<>();
        String sql = "SELECT jt.job_title, SUM(p.earnings) as total_pay " +
                    "FROM payroll p " +
                    "JOIN employees e ON p.empid = e.empid " +
                    "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                    "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                    "WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ? " +
                    "GROUP BY jt.job_title";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                results.put(rs.getString("job_title"), rs.getDouble("total_pay"));
            }
            
        } catch (SQLException e) {
            System.err.println("Job title report error: " + e.getMessage());
        }
        
        return results;
    }
    
    public Map<String, Double> getTotalPayByDivision(int month, int year) {
        Map<String, Double> results = new HashMap<>();
        String sql = "SELECT d.Name, SUM(p.earnings) as total_pay " +
                    "FROM payroll p " +
                    "JOIN employees e ON p.empid = e.empid " +
                    "JOIN employee_division ed ON e.empid = ed.empid " +
                    "JOIN division d ON ed.div_ID = d.ID " +
                    "WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ? " +
                    "GROUP BY d.Name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                results.put(rs.getString("Name"), rs.getDouble("total_pay"));
            }
            
        } catch (SQLException e) {
            System.err.println("Division report error: " + e.getMessage());
        }
        
        return results;
    }
}