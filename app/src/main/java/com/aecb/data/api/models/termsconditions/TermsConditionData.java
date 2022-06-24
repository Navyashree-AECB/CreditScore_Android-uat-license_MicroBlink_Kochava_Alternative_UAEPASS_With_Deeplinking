package com.aecb.data.api.models.termsconditions;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class TermsConditionData {

    @SerializedName("field_content_ar")
    private String fieldContentAr;

    @SerializedName("title")
    private String title;

    @SerializedName("field_unique_id")
    private String fieldUniqueId;

    @SerializedName("field_version_no")
    private String fieldVersionNo;

    @SerializedName("field_content_en")
    private String fieldContentEn;

    public String getFieldContentAr() {
        return fieldContentAr;
    }

    public void setFieldContentAr(String fieldContentAr) {
        this.fieldContentAr = fieldContentAr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFieldUniqueId() {
        return fieldUniqueId;
    }

    public void setFieldUniqueId(String fieldUniqueId) {
        this.fieldUniqueId = fieldUniqueId;
    }

    public String getFieldVersionNo() {
        return fieldVersionNo;
    }

    public void setFieldVersionNo(String fieldVersionNo) {
        this.fieldVersionNo = fieldVersionNo;
    }

    public String getFieldContentEn() {
        return fieldContentEn;
    }

    public void setFieldContentEn(String fieldContentEn) {
        this.fieldContentEn = fieldContentEn;
    }

    @Override
    public String toString() {
        return
                "TermsConditionData{" +
                        "field_content_ar = '" + fieldContentAr + '\'' +
                        ",title = '" + title + '\'' +
                        ",field_unique_id = '" + fieldUniqueId + '\'' +
                        ",field_version_no = '" + fieldVersionNo + '\'' +
                        ",field_content_en = '" + fieldContentEn + '\'' +
                        "}";
    }
}