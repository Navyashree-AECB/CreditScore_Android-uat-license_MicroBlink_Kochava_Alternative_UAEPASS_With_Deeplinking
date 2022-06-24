package com.aecb.data.api.models.registeruser;

import com.aecb.data.api.models.securityquestions.RoundQuestionsData;
import com.aecb.data.api.models.securityquestions.RoundSuccessData;

public class RegisterUserItems {
    private OtpGeneratedData otpGeneratedData;

    private RoundSuccessData roundSuccessData;

    private RoundQuestionsData roundQuestionsData;

    private String applicationStatus;

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public OtpGeneratedData getOtpGeneratedData() {
        return otpGeneratedData;
    }

    public void setOtpGeneratedData(OtpGeneratedData otpGeneratedData) {
        this.otpGeneratedData = otpGeneratedData;
    }

    public RoundSuccessData getRoundSuccessData() {
        return roundSuccessData;
    }

    public void setRoundSuccessData(RoundSuccessData roundSuccessData) {
        this.roundSuccessData = roundSuccessData;
    }

    public RoundQuestionsData getRoundQuestionsData() {
        return roundQuestionsData;
    }

    public void setRoundQuestionsData(RoundQuestionsData roundQuestionsData) {
        this.roundQuestionsData = roundQuestionsData;
    }
}
