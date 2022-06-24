package com.aecb.data.api.models.buycreditreport;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

public class BuyCreditReportResponse extends BaseResponse {

    @SerializedName("data")
    private BuyCreditReportData buyCreditReportData;

    public BuyCreditReportData getBuyCreditReportData() {
        return buyCreditReportData;
    }

    public void setBuyCreditReportData(BuyCreditReportData buyCreditReportData) {
        this.buyCreditReportData = buyCreditReportData;
    }
}