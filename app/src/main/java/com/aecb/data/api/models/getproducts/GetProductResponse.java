package com.aecb.data.api.models.getproducts;

import com.aecb.data.api.models.BaseResponse;

public class GetProductResponse extends BaseResponse {
    private GetProductData data;

    public GetProductData getData() {
        return data;
    }

    public void setData(GetProductData data) {
        this.data = data;
    }
}