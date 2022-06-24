package com.aecb.data.api.models.securityquestions;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SecurityQuestionsResponse extends BaseResponse {

    @SerializedName("data")
    private Object securityQuestionsItems;

    @SerializedName("applicationStatus")
    private String applicationStatus;

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Object getSecurityQuestionsItems() {
        return securityQuestionsItems;
    }

    public void setSecurityQuestionsItems(Object securityQuestionsItems) {
        this.securityQuestionsItems = securityQuestionsItems;
    }
}