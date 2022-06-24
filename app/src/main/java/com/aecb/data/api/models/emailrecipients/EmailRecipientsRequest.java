package com.aecb.data.api.models.emailrecipients;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class EmailRecipientsRequest {

    @SerializedName("additionalReceivers")
    private List<EmailRecipients> additionalReceivers;

    @SerializedName("applicationId")
    private String applicationId;

    public List<EmailRecipients> getAdditionalReceivers() {
        return additionalReceivers;
    }

    public void setAdditionalReceivers(List<EmailRecipients> additionalReceivers) {
        this.additionalReceivers = additionalReceivers;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

}