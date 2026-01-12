package com.companyZ.ems.model;

public class User {
    private int empid;
    private String username;
    private String passwordHash;
    private String role;
    
    public User() {}
    
    public User(int empid, String username, String role) {
        this.empid = empid;
        this.username = username;
        this.role = role;
    }
    
    public int getEmpid() { return empid; }
    public void setEmpid(int empid) { this.empid = empid; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}