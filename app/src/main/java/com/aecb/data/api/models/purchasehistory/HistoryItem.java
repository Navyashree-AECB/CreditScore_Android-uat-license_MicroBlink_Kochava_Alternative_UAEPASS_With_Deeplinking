package com.aecb.data.api.models.purchasehistory;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class HistoryItem implements Serializable {

    @SerializedName("score")
    private int score = 300;

    @SerializedName("ReportName")
    private String reportName;

    @SerializedName("isPdfAvailable")
    private boolean isPdfAvailable;

    @SerializedName("scoreRange")
    private String scoreRange;

    @SerializedName("reportId")
    private String reportId;

    @SerializedName("reportNumber")
    private String reportNumber;

    @SerializedName("reportTypeId")
    private String reportTypeId;

    @SerializedName("createdon")
    private String createdon;

    @SerializedName("isAlternativeScore")
    private boolean isAlternativeScore;

    @SerializedName("noScore")
    private boolean noScore = false;

    public boolean isPdfAvailable() {
        return isPdfAvailable;
    }

    public void setPdfAvailable(boolean pdfAvailable) {
        isPdfAvailable = pdfAvailable;
    }

    public boolean isNoScore() {
        return noScore;
    }

    public void setNoScore(boolean noScore) {
        this.noScore = noScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public boolean isIsPdfAvailable() {
        return isPdfAvailable;
    }

    public void setIsPdfAvailable(boolean isPdfAvailable) {
        this.isPdfAvailable = isPdfAvailable;
    }

    public String getScoreRange() {
        return scoreRange;
    }

    public void setScoreRange(String scoreRange) {
        this.scoreRange = scoreRange;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(String reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public boolean isAlternativeScore() {
        return isAlternativeScore;
    }

    public void setAlternativeScore(boolean alternativeScore) {
        isAlternativeScore = alternativeScore;
    }

    @Override
    public String toString() {
        return
                "HistoryItem{" +
                        "score = '" + score + '\'' +
                        ",reportName = '" + reportName + '\'' +
                        ",isPdfAvailable = '" + isPdfAvailable + '\'' +
                        ",scoreRange = '" + scoreRange + '\'' +
                        ",reportId = '" + reportId + '\'' +
                        ",reportNumber = '" + reportNumber + '\'' +
                        ",reportTypeId = '" + reportTypeId + '\'' +
                        ",createdon = '" + createdon + '\'' +
                        "}";
    }
}