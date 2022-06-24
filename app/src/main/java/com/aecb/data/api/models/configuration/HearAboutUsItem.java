package com.aecb.data.api.models.configuration;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class HearAboutUsItem {

    @SerializedName("title_ar")
    private String titleAr;

    @SerializedName("title_en")
    private String titleEn;

    @SerializedName("value")
    private String value;

    @SerializedName("order")
    private int order;

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return
                "HearAboutUsItem{" +
                        "title_ar = '" + titleAr + '\'' +
                        ",title_en = '" + titleEn + '\'' +
                        ",value = '" + value + '\'' +
                        ",order = '" + order + '\'' +
                        "}";
    }
}