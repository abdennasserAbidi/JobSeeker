package com.myjob.jobseeker.dtos;

public class ExperienceDto {

    private int id;
    private int idUser;
    private String message;
    private String title;
    private String companyName;
    private String dateStart;
    private String dateEnd;
    private String place;
    private String type;
    private String typeContract;
    private int salary;
    private String freelanceFee;
    private String anotherActivitySector;
    private int nbHours;
    private int nbDays;
    private int hourlyRate;
    
    public String getFreelanceFee() {
        return freelanceFee;
    }
    public void setFreelanceFee(String freelanceFee) {
        this.freelanceFee = freelanceFee;
    }
    public int getNbHours() {
        return nbHours;
    }
    public void setNbHours(int nbHours) {
        this.nbHours = nbHours;
    }
    public int getNbDays() {
        return nbDays;
    }
    public void setNbDays(int nbDays) {
        this.nbDays = nbDays;
    }
    public int getHourlyRate() {
        return hourlyRate;
    }
    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
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
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public String getTypeContract() {
        return typeContract;
    }
    public void setTypeContract(String typeContract) {
        this.typeContract = typeContract;
    }
    public String getAnotherActivitySector() {
        return anotherActivitySector;
    }
    public void setAnotherActivitySector(String anotherActivitySector) {
        this.anotherActivitySector = anotherActivitySector;
    }

    

}
