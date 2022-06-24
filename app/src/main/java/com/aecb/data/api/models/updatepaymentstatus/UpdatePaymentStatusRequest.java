package com.aecb.data.api.models.updatepaymentstatus;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class UpdatePaymentStatusRequest {

    @SerializedName("data")
    private UpdatePaymentStatusData data;

    @SerializedName("success")
    private boolean success;

    @SerializedName("refid")
    private String refid;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public UpdatePaymentStatusData getData() {
        return data;
    }

    public void setData(UpdatePaymentStatusData data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "UpdatePaymentStatusRequest{" +
                        "data = '" + data + '\'' +
                        ",success = '" + success + '\'' +
                        ",refid = '" + refid + '\'' +
                        ",message = '" + message + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}