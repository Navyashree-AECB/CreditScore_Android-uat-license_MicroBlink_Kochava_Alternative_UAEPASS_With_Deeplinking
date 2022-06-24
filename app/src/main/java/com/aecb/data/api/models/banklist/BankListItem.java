package com.aecb.data.api.models.banklist;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class BankListItem {

    @SerializedName("bankCode")
    private String bankCode;

    @SerializedName("bankNameAR")
    private String bankNameAR = "";

    @SerializedName("bankDepartmentName")
    private Object bankDepartmentName;

    @SerializedName("bankEmail")
    private String bankEmail;

    @SerializedName("bankName")
    private String bankName;

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankNameAR(String bankNameAR) {
        this.bankNameAR = bankNameAR;
    }

    public String getBankNameAR() {
        return bankNameAR;
    }

    public void setBankDepartmentName(Object bankDepartmentName) {
        this.bankDepartmentName = bankDepartmentName;
    }

    public Object getBankDepartmentName() {
        return bankDepartmentName;
    }

    public void setBankEmail(String bankEmail) {
        this.bankEmail = bankEmail;
    }

    public String getBankEmail() {
        return bankEmail;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }
}