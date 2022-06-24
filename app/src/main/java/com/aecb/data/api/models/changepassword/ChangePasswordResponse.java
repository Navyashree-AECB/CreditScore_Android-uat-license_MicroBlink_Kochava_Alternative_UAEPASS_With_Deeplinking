package com.aecb.data.api.models.changepassword;

import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.securityquestions.ParseUser;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse extends BaseResponse {
    @SerializedName("data")
    private ParseUser parseUser;

    public ParseUser getParseUser() {
        return parseUser;
    }

    public void setParseUser(ParseUser parseUser) {
        this.parseUser = parseUser;
    }
}