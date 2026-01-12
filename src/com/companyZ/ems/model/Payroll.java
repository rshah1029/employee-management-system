package com.companyZ.ems.model;

import java.util.Date;

public class Payroll {
    private int payID;
    private Date payDate;
    private int empid;
    private double earnings;
    private double fedTax;
    private double fedMed;
    private double fedSS;
    private double stateTax;
    private double retire401k;
    private double healthCare;
    
    public Payroll() {}
    
    public int getPayID() { return payID; }
    public void setPayID(int payID) { this.payID = payID; }
    
    public Date getPayDate() { return payDate; }
    public void setPayDate(Date payDate) { this.payDate = payDate; }
    
    public int getEmpid() { return empid; }
    public void setEmpid(int empid) { this.empid = empid; }
    
    public double getEarnings() { return earnings; }
    public void setEarnings(double earnings) { this.earnings = earnings; }
    
    public double getFedTax() { return fedTax; }
    public void setFedTax(double fedTax) { this.fedTax = fedTax; }
    
    public double getFedMed() { return fedMed; }
    public void setFedMed(double fedMed) { this.fedMed = fedMed; }
    
    public double getFedSS() { return fedSS; }
    public void setFedSS(double fedSS) { this.fedSS = fedSS; }
    
    public double getStateTax() { return stateTax; }
    public void setStateTax(double stateTax) { this.stateTax = stateTax; }
    
    public double getRetire401k() { return retire401k; }
    public void setRetire401k(double retire401k) { this.retire401k = retire401k; }
    
    public double getHealthCare() { return healthCare; }
    public void setHealthCare(double healthCare) { this.healthCare = healthCare; }
    
    public double getTotalDeductions() {
        return fedTax + fedMed + fedSS + stateTax + retire401k + healthCare;
    }
    
    public double getNetPay() {
        return earnings - getTotalDeductions();
    }
}