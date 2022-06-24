package com.aecb.data.api.models.emailrecipients;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EmailRecipients implements Serializable {

    @SerializedName("name")
    private String fullName;

    @SerializedName("emailId")
    private String email;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}