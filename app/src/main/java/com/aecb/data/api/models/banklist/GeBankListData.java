package com.aecb.data.api.models.banklist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeBankListData {
    @SerializedName("bankList")
    private List<BankListItem> bankList;

    public void setBankList(List<BankListItem> bankList) {
        this.bankList = bankList;
    }

    public List<BankListItem> getBankList() {
        return bankList;
    }
}