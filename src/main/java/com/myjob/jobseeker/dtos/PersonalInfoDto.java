package com.myjob.jobseeker.dtos;

public class PersonalInfoDto {

    private int id;

    private String nationality;

    private String address;
    
    private String sexe;
    
    private String fullName;

    private String birthDate;

    private String activitySector;

    private String situation;

    private String availability;

    private String rangeSalary;

    private String preferredActivitySector;

    private String phone;

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getRangeSalary() {
        return rangeSalary;
    }

    public void setRangeSalary(String rangeSalary) {
        this.rangeSalary = rangeSalary;
    }

    public String getPreferredActivitySector() {
        return preferredActivitySector;
    }

    public void setPreferredActivitySector(String preferredActivitySector) {
        this.preferredActivitySector = preferredActivitySector;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getActivitySector() {
        return activitySector;
    }

    public void setActivitySector(String activitySector) {
        this.activitySector = activitySector;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    


}
