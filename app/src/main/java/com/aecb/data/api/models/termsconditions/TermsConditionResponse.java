package com.aecb.data.api.models.termsconditions;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

public class TermsConditionResponse extends BaseResponse {

    @SerializedName("data")
    private TermsConditionData termsConditionData;

    public TermsConditionData getTermsConditionData() {
        return termsConditionData;
    }

    public void setTermsConditionData(TermsConditionData termsConditionData) {
        this.termsConditionData = termsConditionData;
    }
}