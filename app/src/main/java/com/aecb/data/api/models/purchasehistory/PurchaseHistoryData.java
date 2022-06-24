package com.aecb.data.api.models.purchasehistory;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PurchaseHistoryData {

    @SerializedName("history")
    private List<HistoryItem> history;

    public List<HistoryItem> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryItem> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "history = '" + history + '\'' +
                        "}";
    }
}