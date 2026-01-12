package com.companyZ.ems.model;

public class JobTitle {
    private int jobTitleId;
    private String jobTitle;
    
    public JobTitle() {}
    
    public JobTitle(int jobTitleId, String jobTitle) {
        this.jobTitleId = jobTitleId;
        this.jobTitle = jobTitle;
    }
    
    public int getJobTitleId() { return jobTitleId; }
    public void setJobTitleId(int jobTitleId) { this.jobTitleId = jobTitleId; }
    
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
}