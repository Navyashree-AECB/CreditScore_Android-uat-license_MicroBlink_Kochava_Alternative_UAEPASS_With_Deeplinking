package com.aecb.data.api.models.biometric;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class UpdateBiometricRequest{

	@SerializedName("isEnabledFaceID")
	private boolean isEnabledFaceID;

	@SerializedName("isEnabledTouchID")
	private boolean isEnabledTouchID;

	public void setIsEnabledFaceID(boolean isEnabledFaceID){
		this.isEnabledFaceID = isEnabledFaceID;
	}

	public boolean isIsEnabledFaceID(){
		return isEnabledFaceID;
	}

	public void setIsEnabledTouchID(boolean isEnabledTouchID){
		this.isEnabledTouchID = isEnabledTouchID;
	}

	public boolean isIsEnabledTouchID(){
		return isEnabledTouchID;
	}

	@Override
 	public String toString(){
		return 
			"UpdateBiometricRequest{" + 
			"isEnabledFaceID = '" + isEnabledFaceID + '\'' + 
			",isEnabledTouchID = '" + isEnabledTouchID + '\'' + 
			"}";
		}
}