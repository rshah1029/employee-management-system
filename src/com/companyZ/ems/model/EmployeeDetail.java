package com.companyZ.ems.model;

public class EmployeeDetail {
    private Employee employee;
    private Address address;
    private Division division;
    private JobTitle jobTitle;
    
    public EmployeeDetail() {}
    
    public EmployeeDetail(Employee employee, Address address, Division division, JobTitle jobTitle) {
        this.employee = employee;
        this.address = address;
        this.division = division;
        this.jobTitle = jobTitle;
    }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    
    public Division getDivision() { return division; }
    public void setDivision(Division division) { this.division = division; }
    
    public JobTitle getJobTitle() { return jobTitle; }
    public void setJobTitle(JobTitle jobTitle) { this.jobTitle = jobTitle; }
}