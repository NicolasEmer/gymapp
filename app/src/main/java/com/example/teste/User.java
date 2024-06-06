package com.example.teste;

public class User {
    String id, firstName, bio, phone;

    public User() {
    }

    public String getPhone() {
        return phone;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
