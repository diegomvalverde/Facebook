package com.example.proyectoii;

import java.util.ArrayList;

public class Profile {

    private String name;
    private String lastName;
    private String profilePhoto;
    private ArrayList<String> photos = new ArrayList<>();
    private ArrayList<String> educations = new ArrayList<>();
    private String city;
    private String bornDate;
    private String phoneNumber;
    private String email;
    private String gender;


    public Profile(String name, String lastName, String profilePhoto, ArrayList<String> photos, ArrayList<String> educations, String city, String bornDate, String phoneNumber, String email, String gender) {
        this.name = name;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.photos = photos;
        this.educations = educations;
        this.city = city;
        this.bornDate = bornDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public ArrayList<String> getEducations() {
        return educations;
    }

    public void setEducations(ArrayList<String> educations) {
        this.educations = educations;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
