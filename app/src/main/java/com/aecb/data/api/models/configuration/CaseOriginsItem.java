package com.aecb.data.api.models.configuration;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class CaseOriginsItem {

    @SerializedName("code")
    private int code;

    @SerializedName("name")
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return
                "CaseOriginsItem{" +
                        "code = '" + code + '\'' +
                        ",name = '" + name + '\'' +
                        "}";
    }
}