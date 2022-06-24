package com.aecb.data.api.models.response.getuserdetail;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GetProfileResponse extends BaseResponse {

    @SerializedName("data")
    GetUserDetailResponse data;

    public GetUserDetailResponse getData() {
        return data;
    }

    public void setData(GetUserDetailResponse data) {
        this.data = data;
    }

}