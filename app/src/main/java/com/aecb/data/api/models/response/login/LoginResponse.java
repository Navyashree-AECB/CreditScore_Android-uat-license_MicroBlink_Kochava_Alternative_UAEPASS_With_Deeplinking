package com.aecb.data.api.models.response.login;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class LoginResponse extends BaseResponse {

    @SerializedName("data")
    LoginData data;

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

}