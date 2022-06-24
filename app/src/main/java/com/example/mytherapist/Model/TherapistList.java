package com.example.mytherapist.Model;

public class TherapistList {

    private String email, username, profileimage;

    public TherapistList(String email, String username, String profileimage) {
        this.email = email;
        this.username = username;
        this.profileimage = profileimage;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}
