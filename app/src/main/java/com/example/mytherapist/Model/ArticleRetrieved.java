package com.example.mytherapist.Model;

public class ArticleRetrieved {

    String title="";
    String description="";
    String date="";
    String src= "";


    public ArticleRetrieved() {
    }

    public ArticleRetrieved(String title, String description, String date, String src) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.src = src;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getsrc() {
        return src;
    }

    public void setsrc(String src) {
        this.src = src;
    }
}
