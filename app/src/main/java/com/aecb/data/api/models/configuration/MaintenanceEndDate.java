package com.aecb.data.api.models.configuration;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MaintenanceEndDate {

    @SerializedName("iso")
    private String iso;

    @SerializedName("__type")
    private String type;

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return
                "MaintenanceEndDate{" +
                        "iso = '" + iso + '\'' +
                        ",__type = '" + type + '\'' +
                        "}";
    }
}