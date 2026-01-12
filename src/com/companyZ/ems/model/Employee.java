package com.companyZ.ems.model;

import java.util.Date;

public class Employee {
    private int empid;
    private String fname;
    private String lname;
    private String email;
    private Date hireDate;
    private double salary;
    private String ssn;
    
    public Employee() {}
    
    public Employee(int empid, String fname, String lname, String email, 
                   Date hireDate, double salary, String ssn) {
        this.empid = empid;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.hireDate = hireDate;
        this.salary = salary;
        this.ssn = ssn;
    }
    
    public int getEmpid() { return empid; }
    public void setEmpid(int empid) { this.empid = empid; }
    
    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }
    
    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    
    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
    
    public String getFullName() {
        return fname + " " + lname;
    }
}