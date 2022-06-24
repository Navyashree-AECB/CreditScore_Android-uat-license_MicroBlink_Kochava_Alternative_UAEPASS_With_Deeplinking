package com.aecb.data.api.models.configuration;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class TermsAndConditions {

    @SerializedName("versionNumber")
    private int versionNumber;

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public String toString() {
        return
                "TermsAndConditions{" +
                        "versionNumber = '" + versionNumber + '\'' +
                        "}";
    }
}