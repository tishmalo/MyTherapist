package com.example.mytherapist.Model;

public class PaymentModel {

    private String amount, number, therapistemail,useremail;


    public PaymentModel() {
    }

    public PaymentModel(String amount, String number, String therapistemail, String useremail) {
        this.amount = amount;
        this.number = number;
        this.therapistemail = therapistemail;
        this.useremail = useremail;

    }



    public String getamount() {
        return amount;
    }

    public void setamount(String amount) {
        this.amount = amount;
    }

    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }

    public String gettherapistemail() {
        return therapistemail;
    }

    public void settherapistemail(String therapistemail) {
        this.therapistemail = therapistemail;
    }

    public String getuseremail() {
        return useremail;
    }

    public void setuseremail(String useremail) {
        this.useremail = useremail;
    }
}
