package com.aecb.data.api.models.registeruser;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RegisterUserResponse extends BaseResponse {

    @SerializedName("data")
    Object registerUserItems;

    public Object getRegisterUserItems() {
        return registerUserItems;
    }

    public void setRegisterUserItems(Object registerUserItems) {
        this.registerUserItems = registerUserItems;
    }
}