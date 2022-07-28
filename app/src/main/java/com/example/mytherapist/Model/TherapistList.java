package com.example.mytherapist.Model;

public class TherapistList {

    private String email, username, profileimage;

    public TherapistList() {
    }

    public TherapistList(String email, String username, String profileimage) {
        this.email = email;
        this.username = username;
        this.profileimage = profileimage;
    }


    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getprofileimage() {
        return profileimage;
    }

    public void setprofileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}
