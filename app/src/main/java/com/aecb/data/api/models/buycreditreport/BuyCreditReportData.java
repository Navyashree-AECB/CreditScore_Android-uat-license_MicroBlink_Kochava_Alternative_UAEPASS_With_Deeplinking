package com.aecb.data.api.models.buycreditreport;

public class BuyCreditReportData {
    private String portalUserId;
    private String noScore;
    private boolean isNoHit;
    private boolean isScoreError;

    public boolean isNoHit() {
        return isNoHit;
    }

    public void setNoHit(boolean noHit) {
        isNoHit = noHit;
    }

    public boolean isScoreError() {
        return isScoreError;
    }

    public void setScoreError(boolean scoreError) {
        isScoreError = scoreError;
    }

    public String getPortalUserId() {
        return portalUserId;
    }

    public void setPortalUserId(String portalUserId) {
        this.portalUserId = portalUserId;
    }

    public String getNoScore() {
        return noScore;
    }

    public void setNoScore(String noScore) {
        this.noScore = noScore;
    }
}