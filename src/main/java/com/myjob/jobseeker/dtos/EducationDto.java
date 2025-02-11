package com.myjob.jobseeker.dtos;

public class EducationDto {

    private int id;
    private int idUser;
    private String message;
    private String title;
    private String schoolName;
    private String dateStart;
    private String dateEnd;
    private String place;
    private String fieldStudy;
    private String grade;
    private String description;
    private String degree;
    private boolean stillStudying;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getIdUser() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSchoolName() {
        return schoolName;
    }
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    public String getDateStart() {
        return dateStart;
    }
    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }
    public String getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getFieldStudy() {
        return fieldStudy;
    }
    public void setFieldStudy(String fieldStudy) {
        this.fieldStudy = fieldStudy;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDegree() {
        return degree;
    }
    public void setDegree(String degree) {
        this.degree = degree;
    }
    public boolean isStillStudying() {
        return stillStudying;
    }
    public void setStillStudying(boolean stillStudying) {
        this.stillStudying = stillStudying;
    }

    

}
