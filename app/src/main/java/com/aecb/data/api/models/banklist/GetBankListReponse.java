package com.aecb.data.api.models.banklist;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GetBankListReponse {

    @SerializedName("data")
    private GeBankListData data;

    public void setData(GeBankListData data) {
        this.data = data;
    }

    public GeBankListData getData() {
        return data;
    }
}