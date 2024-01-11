package com.example.iderm;

public class uploadinfo {
    public String imageURL;
    public  String ownerName;
    public String docType;
    public  String number;
    String timePosted;
    String agent;
    public uploadinfo(){

    }

    public uploadinfo(String imageURL, String ownerName,String timePosted, String agent) {
        this.imageURL = imageURL;
        this.ownerName = ownerName;
        this.docType = docType;
        this.number = number;
        this.timePosted = timePosted;
        this.agent=agent;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }
}


