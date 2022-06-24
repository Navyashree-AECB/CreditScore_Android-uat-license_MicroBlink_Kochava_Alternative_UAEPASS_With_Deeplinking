package com.aecb.data.db.repository.commonresponse;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.aecb.data.api.models.settings.ConfigurationData;

@Entity(tableName = "Responses")
public class DbResponse {

    @PrimaryKey
    @ColumnInfo(name = "responseId")
    private int responseId;
    @ColumnInfo(name = "settings")
    private ConfigurationData configurationData;
    @ColumnInfo(name = "PaymentMethod")
    private String paymentMethod;

    public DbResponse(int responseId, ConfigurationData configurationData, String paymentMethod) {
        this.responseId = responseId;
        this.configurationData = configurationData;
        this.paymentMethod = paymentMethod;
    }

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public ConfigurationData getConfigurationData() {
        return configurationData;
    }

    public void setConfigurationData(ConfigurationData configurationData) {
        this.configurationData = configurationData;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}