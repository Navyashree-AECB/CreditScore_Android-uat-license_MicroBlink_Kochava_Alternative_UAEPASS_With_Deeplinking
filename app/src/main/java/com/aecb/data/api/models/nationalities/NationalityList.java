package com.aecb.data.api.models.nationalities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NationalityList {
    @SerializedName("nationalities")
    private List<NationalitiesItem> nationalities;

    public List<NationalitiesItem> getNationalities() {
        return nationalities;
    }

    public void setNationalities(List<NationalitiesItem> nationalities) {
        this.nationalities = nationalities;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "nationalities = '" + nationalities + '\'' +
                        "}";
    }
}