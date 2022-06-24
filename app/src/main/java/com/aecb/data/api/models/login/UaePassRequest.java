package com.aecb.data.api.models.login;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UaePassRequest implements Serializable {

	@SerializedName("mFirstnameAR")
	private String firstnameAR;

	@SerializedName("mFullnameAR")
	private String fullnameAR;

	@SerializedName("mHomeAddressMobilePhoneNumber")
	private String homeAddressMobilePhoneNumber;

	@SerializedName("mLastnameEN")
	private String lastnameEN;

	@SerializedName("mPassportNumber")
	private String passportNumber;

	@SerializedName("mHomeAddressEmirateDescriptionAR")
	private String homeAddressEmirateDescriptionAR;

	@SerializedName("mFirstnameEN")
	private String firstnameEN;

	@SerializedName("mFullnameEN")
	private String fullnameEN;

	@SerializedName("mLastnameAR")
	private String lastnameAR;

	@SerializedName("mDomain")
	private String domain;

	@SerializedName("mHomeAddressEmirateDescriptionEN")
	private String homeAddressEmirateDescriptionEN;

	@SerializedName("mEmail")
	private String email;

	@SerializedName("mAcr")
	private String acr;

	@SerializedName("mHomeAddressAreaDescriptionAR")
	private String homeAddressAreaDescriptionAR;

	@SerializedName("mCardHolderSignatureImage")
	private String cardHolderSignatureImage;

	@SerializedName("mHomeAddressCityDescriptionEN")
	private String homeAddressCityDescriptionEN;

	@SerializedName("mMobile")
	private String mobile;

	@SerializedName("mHomeAddressAreaDescriptionEN")
	private String homeAddressAreaDescriptionEN;

	@SerializedName("mPhoto")
	private String photo;

	@SerializedName("mNationalityAR")
	private String nationalityAR;

	@SerializedName("mHomeAddressCityDescriptionAR")
	private String homeAddressCityDescriptionAR;

	@SerializedName("mHomeAddressTypeCode")
	private String homeAddressTypeCode;

	@SerializedName("mHomeAddressPOBox")
	private String homeAddressPOBox;

	@SerializedName("mSub")
	private String sub;

	@SerializedName("mHomeAddressCityCode")
	private String homeAddressCityCode;

	@SerializedName("mNationalityEN")
	private String nationalityEN;

	@SerializedName("mUserType")
	private String userType;

	@SerializedName("mAmr")
	private List<String> amr;

	@SerializedName("mHomeAddressEmirateCode")
	private String homeAddressEmirateCode;

	@SerializedName("mHomeAddressAreaCode")
	private String homeAddressAreaCode;

	@SerializedName("mIdn")
	private String idn;

	@SerializedName("mDob")
	private String dob;

	@SerializedName("mGender")
	private String gender;

	@SerializedName("mUuid")
	private String uuid;

	@SerializedName("mIdType")
	private String idType;

	@SerializedName("mSpuuid")
	private String spuuid;

	@SerializedName("mTitleEN")
	private String titleEN;

	@SerializedName("mTitleAR")
	private String titleAR;

	public String getFirstnameAR() {
		return firstnameAR;
	}

	public void setFirstnameAR(String firstnameAR) {
		this.firstnameAR = firstnameAR;
	}

	public String getFullnameAR() {
		return fullnameAR;
	}

	public void setFullnameAR(String fullnameAR) {
		this.fullnameAR = fullnameAR;
	}

	public String getHomeAddressMobilePhoneNumber() {
		return homeAddressMobilePhoneNumber;
	}

	public void setHomeAddressMobilePhoneNumber(String homeAddressMobilePhoneNumber) {
		this.homeAddressMobilePhoneNumber = homeAddressMobilePhoneNumber;
	}

	public String getLastnameEN() {
		return lastnameEN;
	}

	public void setLastnameEN(String lastnameEN) {
		this.lastnameEN = lastnameEN;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getHomeAddressEmirateDescriptionAR() {
		return homeAddressEmirateDescriptionAR;
	}

	public void setHomeAddressEmirateDescriptionAR(String homeAddressEmirateDescriptionAR) {
		this.homeAddressEmirateDescriptionAR = homeAddressEmirateDescriptionAR;
	}

	public String getFirstnameEN() {
		return firstnameEN;
	}

	public void setFirstnameEN(String firstnameEN) {
		this.firstnameEN = firstnameEN;
	}

	public String getFullnameEN() {
		return fullnameEN;
	}

	public void setFullnameEN(String fullnameEN) {
		this.fullnameEN = fullnameEN;
	}

	public String getLastnameAR() {
		return lastnameAR;
	}

	public void setLastnameAR(String lastnameAR) {
		this.lastnameAR = lastnameAR;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getHomeAddressEmirateDescriptionEN() {
		return homeAddressEmirateDescriptionEN;
	}

	public void setHomeAddressEmirateDescriptionEN(String homeAddressEmirateDescriptionEN) {
		this.homeAddressEmirateDescriptionEN = homeAddressEmirateDescriptionEN;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAcr() {
		return acr;
	}

	public void setAcr(String acr) {
		this.acr = acr;
	}

	public String getHomeAddressAreaDescriptionAR() {
		return homeAddressAreaDescriptionAR;
	}

	public void setHomeAddressAreaDescriptionAR(String homeAddressAreaDescriptionAR) {
		this.homeAddressAreaDescriptionAR = homeAddressAreaDescriptionAR;
	}

	public String getCardHolderSignatureImage() {
		return cardHolderSignatureImage;
	}

	public void setCardHolderSignatureImage(String cardHolderSignatureImage) {
		this.cardHolderSignatureImage = cardHolderSignatureImage;
	}

	public String getHomeAddressCityDescriptionEN() {
		return homeAddressCityDescriptionEN;
	}

	public void setHomeAddressCityDescriptionEN(String homeAddressCityDescriptionEN) {
		this.homeAddressCityDescriptionEN = homeAddressCityDescriptionEN;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHomeAddressAreaDescriptionEN() {
		return homeAddressAreaDescriptionEN;
	}

	public void setHomeAddressAreaDescriptionEN(String homeAddressAreaDescriptionEN) {
		this.homeAddressAreaDescriptionEN = homeAddressAreaDescriptionEN;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getNationalityAR() {
		return nationalityAR;
	}

	public void setNationalityAR(String nationalityAR) {
		this.nationalityAR = nationalityAR;
	}

	public String getHomeAddressCityDescriptionAR() {
		return homeAddressCityDescriptionAR;
	}

	public void setHomeAddressCityDescriptionAR(String homeAddressCityDescriptionAR) {
		this.homeAddressCityDescriptionAR = homeAddressCityDescriptionAR;
	}

	public String getHomeAddressTypeCode() {
		return homeAddressTypeCode;
	}

	public void setHomeAddressTypeCode(String homeAddressTypeCode) {
		this.homeAddressTypeCode = homeAddressTypeCode;
	}

	public String getHomeAddressPOBox() {
		return homeAddressPOBox;
	}

	public void setHomeAddressPOBox(String homeAddressPOBox) {
		this.homeAddressPOBox = homeAddressPOBox;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getHomeAddressCityCode() {
		return homeAddressCityCode;
	}

	public void setHomeAddressCityCode(String homeAddressCityCode) {
		this.homeAddressCityCode = homeAddressCityCode;
	}

	public String getNationalityEN() {
		return nationalityEN;
	}

	public void setNationalityEN(String nationalityEN) {
		this.nationalityEN = nationalityEN;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<String> getAmr() {
		return amr;
	}

	public void setAmr(List<String> amr) {
		this.amr = amr;
	}

	public String getHomeAddressEmirateCode() {
		return homeAddressEmirateCode;
	}

	public void setHomeAddressEmirateCode(String homeAddressEmirateCode) {
		this.homeAddressEmirateCode = homeAddressEmirateCode;
	}

	public String getHomeAddressAreaCode() {
		return homeAddressAreaCode;
	}

	public void setHomeAddressAreaCode(String homeAddressAreaCode) {
		this.homeAddressAreaCode = homeAddressAreaCode;
	}

	public String getIdn() {
		return idn;
	}

	public void setIdn(String idn) {
		this.idn = idn;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getSpuuid() {
		return spuuid;
	}

	public void setSpuuid(String spuuid) {
		this.spuuid = spuuid;
	}

	public String getTitleEN() {
		return titleEN;
	}

	public void setTitleEN(String titleEN) {
		this.titleEN = titleEN;
	}

	public String getTitleAR() {
		return titleAR;
	}

	public void setTitleAR(String titleAR) {
		this.titleAR = titleAR;
	}
}