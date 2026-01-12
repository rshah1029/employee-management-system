package com.companyZ.ems.model;

public class Address {
    private int empid;
    private String street;
    private int cityId;
    private String cityName;
    private int stateId;
    private String stateName;
    private String zip;
    private String gender;
    private String race;
    private String dob;
    private String phone;
    
    public Address() {}
    
    public int getEmpid() { return empid; }
    public void setEmpid(int empid) { this.empid = empid; }
    
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    
    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }
    
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    
    public int getStateId() { return stateId; }
    public void setStateId(int stateId) { this.stateId = stateId; }
    
    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }
    
    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }
    
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}