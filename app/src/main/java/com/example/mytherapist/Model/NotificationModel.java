package com.example.mytherapist.Model;

public class NotificationModel {

    private String date,email,profileimage,username, therapistemail;

    public NotificationModel() {
    }

    public NotificationModel(String date, String email, String profileimage, String username, String therapistemail) {
        this.date = date;
        this.email = email;
        this.profileimage = profileimage;
        this.username = username;
        this.therapistemail = therapistemail;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getprofileimage() {
        return profileimage;
    }

    public void setprofileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getherapistemail() {
        return therapistemail;
    }

    public void settherapistemail(String therapistemail) {
        this.therapistemail = therapistemail;
    }
}
