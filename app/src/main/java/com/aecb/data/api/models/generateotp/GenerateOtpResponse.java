package com.aecb.data.api.models.generateotp;

import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.registeruser.OtpGeneratedData;

public class GenerateOtpResponse extends BaseResponse {
    private OtpGeneratedData data;

    public OtpGeneratedData getData() {
        return data;
    }

    public void setData(OtpGeneratedData data) {
        this.data = data;
    }
}