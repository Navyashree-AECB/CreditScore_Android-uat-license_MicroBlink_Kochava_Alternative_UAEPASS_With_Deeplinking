package com.aecb.data.api.models.nationalities;

import com.aecb.data.api.models.BaseResponse;

public class NationalityResponse extends BaseResponse {
    private NationalityList data;

    public NationalityList getData() {
        return data;
    }

    public void setData(NationalityList data) {
        this.data = data;
    }
}