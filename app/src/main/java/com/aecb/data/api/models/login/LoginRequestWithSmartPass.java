package com.aecb.data.api.models.login;

public class LoginRequestWithSmartPass {

    private int channel;
    private String smartpassId;
    private String username;
    private String emiratesID;
    private String smartpassData;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getSmartpassId() {
        return smartpassId;
    }

    public void setSmartpassId(String smartpassId) {
        this.smartpassId = smartpassId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmiratesID() {
        return emiratesID;
    }

    public void setEmiratesID(String emiratesID) {
        this.emiratesID = emiratesID;
    }

    public String getSmartpassData() {
        return smartpassData;
    }

    public void setSmartpassData(String smartpassData) {
        this.smartpassData = smartpassData;
    }

}

