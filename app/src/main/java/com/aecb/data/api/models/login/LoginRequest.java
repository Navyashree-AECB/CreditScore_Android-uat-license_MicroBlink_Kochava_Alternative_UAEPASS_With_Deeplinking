package com.aecb.data.api.models.login;

public class LoginRequest {

    private String username;
    private String password;
    private UaePassRequest uaePassData;

    public UaePassRequest getUaePassData() {
        return uaePassData;
    }

    public void setUaePassData(UaePassRequest uaePassData) {
        this.uaePassData = uaePassData;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}