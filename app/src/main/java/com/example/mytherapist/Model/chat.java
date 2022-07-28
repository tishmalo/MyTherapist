package com.example.mytherapist.Model;

public class chat {

    private String sender;
    private String receiver;
    private String message;

    public chat() {
    }

    public chat(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public String getsender() {
        return sender;
    }

    public void setsender(String sender) {
        this.sender = sender;
    }

    public String getreceiver() {
        return receiver;
    }

    public void setreceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }
}
