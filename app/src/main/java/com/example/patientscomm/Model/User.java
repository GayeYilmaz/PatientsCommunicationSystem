package com.example.patientscomm.Model;

public class User{
   private String id;
   private String name;
   private String surname;
   private String doctor;
   private String hospital;
   private String disease;
   private String password;
   private String email;
   private String gender;
   private String imageURL;
   private String status;
   private String search;

    public User() {
    }

    public User(String id,String name, String surname, String doctor, String hospital, String disease, String password, String email, String gender,String imageURL,String status,String search) {
        this.id=id;
        this.name = name;
        this.surname = surname;
        this.doctor = doctor;
        this.hospital = hospital;
        this.disease = disease;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.imageURL = imageURL;
        this.status  = status;
        this.search = search;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
