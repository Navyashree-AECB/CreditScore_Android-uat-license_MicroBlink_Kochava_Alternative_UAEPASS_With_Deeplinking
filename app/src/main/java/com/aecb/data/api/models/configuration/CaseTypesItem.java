package com.aecb.data.api.models.configuration;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class CaseTypesItem {

    @SerializedName("code")
    private String code;

    @SerializedName("prefillCategory")
    private String prefillCategory;

    @SerializedName("labelArabic")
    private String labelArabic;

    @SerializedName("labelEnglish")
    private String labelEnglish;

    @SerializedName("type")
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrefillCategory() {
        return prefillCategory;
    }

    public void setPrefillCategory(String prefillCategory) {
        this.prefillCategory = prefillCategory;
    }

    public String getLabelArabic() {
        return labelArabic;
    }

    public void setLabelArabic(String labelArabic) {
        this.labelArabic = labelArabic;
    }

    public String getLabelEnglish() {
        return labelEnglish;
    }

    public void setLabelEnglish(String labelEnglish) {
        this.labelEnglish = labelEnglish;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return
                "CaseTypesItem{" +
                        "code = '" + code + '\'' +
                        ",prefillCategory = '" + prefillCategory + '\'' +
                        ",labelArabic = '" + labelArabic + '\'' +
                        ",labelEnglish = '" + labelEnglish + '\'' +
                        ",type = '" + type + '\'' +
                        "}";
    }
}