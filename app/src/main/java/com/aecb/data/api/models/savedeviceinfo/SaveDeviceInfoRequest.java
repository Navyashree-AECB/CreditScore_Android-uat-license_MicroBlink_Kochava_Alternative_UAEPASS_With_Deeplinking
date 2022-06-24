package com.aecb.data.api.models.savedeviceinfo;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SaveDeviceInfoRequest {

	@SerializedName("deviceType")
	private String deviceType;

	@SerializedName("deviceTimeZone")
	private String deviceTimeZone;

	@SerializedName("deviceOsVersion")
	private String deviceOsVersion;

	@SerializedName("deviceManufacturer")
	private String deviceManufacturer;

	@SerializedName("deviceId")
	private String deviceId;

	@SerializedName("deviceToken")
	private String deviceToken;

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceTimeZone() {
		return deviceTimeZone;
	}

	public void setDeviceTimeZone(String deviceTimeZone) {
		this.deviceTimeZone = deviceTimeZone;
	}

	public String getDeviceOsVersion() {
		return deviceOsVersion;
	}

	public void setDeviceOsVersion(String deviceOsVersion) {
		this.deviceOsVersion = deviceOsVersion;
	}

	public String getDeviceManufacturer() {
		return deviceManufacturer;
	}

	public void setDeviceManufacturer(String deviceManufacturer) {
		this.deviceManufacturer = deviceManufacturer;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	@Override
	public String toString() {
		return
				"SaveDeviceInfoRequest{" +
						"deviceType = '" + deviceType + '\'' +
						",deviceTimeZone = '" + deviceTimeZone + '\'' +
						",deviceOsVersion = '" + deviceOsVersion + '\'' +
						",deviceManufacturer = '" + deviceManufacturer + '\'' +
						",deviceId = '" + deviceId + '\'' +
						",deviceToken = '" + deviceToken + '\'' +
						"}";
	}
}