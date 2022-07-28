package com.example.mytherapist.Model;

public class TherapistReviewModel {

    private String review, username,profilepic;

    public TherapistReviewModel() {
    }

    public TherapistReviewModel(String review, String username, String profilepic) {
        this.review = review;
        this.username = username;
        this.profilepic = profilepic;
    }

    public String getreview() {
        return review;
    }

    public void setreview(String review) {
        this.review = review;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getprofilepic() {
        return profilepic;
    }

    public void setprofilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
