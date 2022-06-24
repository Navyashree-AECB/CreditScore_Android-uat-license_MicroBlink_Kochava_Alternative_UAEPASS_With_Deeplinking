package com.aecb.data.api.models;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("ActionMessage")
    private String actionMessage;

    @SerializedName("ActionStatus")
    private Boolean actionStatus;

    public final String getActionMessage() {
        return this.actionMessage;
    }

    public final Boolean getActionStatus() {
        return this.actionStatus;
    }

}
