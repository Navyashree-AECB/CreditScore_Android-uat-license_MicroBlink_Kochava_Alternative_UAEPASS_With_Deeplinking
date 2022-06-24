package com.aecb.base.network;

import com.google.gson.annotations.SerializedName;

public final class MyAppWrappedError {

    @SerializedName("message")
    public final Object message;
    @SerializedName("success")
    public final boolean success;
    @SerializedName("error")
    public String error;
    @SerializedName("refId")
    public final String refId;
    @SerializedName("status")
    public final String status;

    public MyAppWrappedError(Object message, boolean success, String refId, String status, String data,
                             String error) {
        this.message = message;
        this.success = success;
        this.refId = refId;
        this.status = status;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getRefId() {
        return refId;
    }

    public String getStatus() {
        return status;
    }

}