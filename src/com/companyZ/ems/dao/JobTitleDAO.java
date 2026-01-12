package com.companyZ.ems.dao;

import com.companyZ.ems.model.JobTitle;
import com.companyZ.ems.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobTitleDAO {
    
    public List<JobTitle> getAllJobTitles() {
        List<JobTitle> jobTitles = new ArrayList<>();
        String sql = "SELECT * FROM job_titles";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                JobTitle jt = new JobTitle();
                jt.setJobTitleId(rs.getInt("job_title_id"));
                jt.setJobTitle(rs.getString("job_title"));
                jobTitles.add(jt);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching job titles: " + e.getMessage());
        }
        
        return jobTitles;
    }
}