package com.myjob.jobseeker.dtos;

import java.util.List;

public class AllUserDto {

    private int id;

    private String email;
    
    private String password;
    
    private String fullName;

    private String role;
    
    private String nationality;

    private String address;
    
    private String sexe;
    
    private String birthDate;

    private String activitySector;

    private String situation;

    private String availability;

    private String rangeSalary;

    private String preferredActivitySector;

    private String phone;

    private List<ExperienceDto> experiences;

    private List<EducationDto> educations;

    public List<ExperienceDto> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceDto> experiences) {
        this.experiences = experiences;
    }

    public List<EducationDto> getEducations() {
        return educations;
    }

    public void setEducations(List<EducationDto> educations) {
        this.educations = educations;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



}
