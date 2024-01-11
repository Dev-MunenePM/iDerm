package com.example.iderm;

public class Postinfo {
    private String imageUrl;
    private String ownerName;
    private String type;
    private String userId;
    private String agent;
    private String timePosted;
    private String number;
    private String description;
    public Postinfo(){

    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Postinfo(String imageUrl, String ownerName, String timePosted, String agent) {
        this.imageUrl = imageUrl;
        this.ownerName = ownerName;
        this.type = type;
        this.userId = userId;
        this.timePosted = timePosted;
        this.number = number;
        this.agent=agent;
        this.description=description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
