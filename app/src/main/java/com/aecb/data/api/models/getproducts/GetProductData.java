package com.aecb.data.api.models.getproducts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductData {
    @SerializedName("refId")
    private String refId;

    @SerializedName("products")
    private List<ProductsItem> products;

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public List<ProductsItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsItem> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return
                "GetProductResponse{" +
                        "refId = '" + refId + '\'' +
                        ",products = '" + products + '\'' +
                        "}";
    }
}