package com.companyZ.ems.dao;

import com.companyZ.ems.model.User;
import com.companyZ.ems.util.DatabaseConnection;
import com.companyZ.ems.util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    public User authenticate(int empid, String username, String password) {
        String sql = "SELECT empid, username, password_hash, role FROM users WHERE empid = ? AND username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, empid);
            stmt.setString(2, username);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (PasswordHasher.verifyPassword(password, storedHash)) {
                    User user = new User();
                    user.setEmpid(rs.getInt("empid"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean createUser(int empid, String username, String password, String role) {
        String sql = "INSERT INTO users (empid, username, password_hash, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, empid);
            stmt.setString(2, username);
            stmt.setString(3, PasswordHasher.hashPassword(password));
            stmt.setString(4, role);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
}