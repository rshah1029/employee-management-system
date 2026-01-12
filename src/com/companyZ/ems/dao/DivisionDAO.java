package com.companyZ.ems.dao;

import com.companyZ.ems.model.Division;
import com.companyZ.ems.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DivisionDAO {
    
    public List<Division> getAllDivisions() {
        List<Division> divisions = new ArrayList<>();
        String sql = "SELECT * FROM division";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Division div = new Division();
                div.setId(rs.getInt("ID"));
                div.setName(rs.getString("Name"));
                div.setCity(rs.getString("city"));
                div.setAddressLine1(rs.getString("addressLine1"));
                div.setAddressLine2(rs.getString("addressLine2"));
                div.setState(rs.getString("state"));
                div.setCountry(rs.getString("country"));
                div.setPostalCode(rs.getString("postalCode"));
                divisions.add(div);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching divisions: " + e.getMessage());
        }
        
        return divisions;
    }
}