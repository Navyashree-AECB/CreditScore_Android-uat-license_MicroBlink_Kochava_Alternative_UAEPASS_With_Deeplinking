package com.aecb.data.api.models.securityquestions;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RoundSuccessData {

	@SerializedName("parseUser")
	private ParseUser parseUser;

	@SerializedName("portalUserId")
	private String portalUserId;

	public ParseUser getParseUser() {
		return parseUser;
	}

	public void setParseUser(ParseUser parseUser) {
		this.parseUser = parseUser;
	}

	public String getPortalUserId() {
		return portalUserId;
	}

	public void setPortalUserId(String portalUserId) {
		this.portalUserId = portalUserId;
	}

	@Override
	public String toString() {
		return
				"RoundOneSuccessData{" +
						"parseUser = '" + parseUser + '\'' +
						",portalUserId = '" + portalUserId + '\'' +
						"}";
	}
}