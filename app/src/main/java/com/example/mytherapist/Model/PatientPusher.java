package com.example.mytherapist.Model;

public class PatientPusher {

    private String profileimage, useremail,therapistemail,userid,userimage;

    public PatientPusher() {
    }

    public PatientPusher(String profileimage, String useremail, String therapistemail, String userid, String userimage) {
        this.profileimage = profileimage;
        this.useremail = useremail;
        this.therapistemail = therapistemail;
        this.userid = userid;
        this.userimage = userimage;
    }

    public String getprofileimage() {
        return profileimage;
    }

    public void setprofileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getuseremail() {
        return useremail;
    }

    public void setuseremail(String useremail) {
        this.useremail = useremail;
    }

    public String gettherapistemail() {
        return therapistemail;
    }

    public void settherapistemail(String therapistemail) {
        this.therapistemail = therapistemail;
    }

    public String getuserid() {
        return userid;
    }

    public void setuserid(String userid) {
        this.userid = userid;
    }

    public String getuserimage() {
        return userimage;
    }

    public void setuserimage(String userimage) {
        this.userimage = userimage;
    }
}
