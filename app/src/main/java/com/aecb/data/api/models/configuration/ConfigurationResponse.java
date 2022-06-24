package com.aecb.data.api.models.configuration;

import com.aecb.data.api.models.BaseResponse;

public class ConfigurationResponse extends BaseResponse {

    private ConfigurationData data;

    public ConfigurationData getData() {
        return data;
    }

    public void setData(ConfigurationData configurationData) {
        this.data = configurationData;
    }
}
