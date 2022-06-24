package com.aecb.data.api.models.nationalities;

import android.os.Parcel;
import android.os.Parcelable;

import com.aecb.AppConstants;
import com.google.gson.annotations.SerializedName;
import com.tumblr.remember.Remember;

import javax.annotation.Generated;

import static com.aecb.data.preference.PrefHelperImpl.KEY_CURRENT_LANGUAGE;

@Generated("com.robohorse.robopojogenerator")
public class NationalitiesItem implements Parcelable {

    @SerializedName("name_ar")
    private String nameAr;

    @SerializedName("nationalityId")
    private String nationalityId;

    @SerializedName("threeDigitIso")
    private String threeDigitIso;

    @SerializedName("name_en")
    private String nameEn;

    public String getCountryName() {
        String mAppLang = Remember.getString(KEY_CURRENT_LANGUAGE, AppConstants.AppLanguage.ISO_CODE_ENG);
        if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
            return getNameAr();
        } else
            return getNameEn();
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(String nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getThreeDigitIso() {
        return threeDigitIso;
    }

    public void setThreeDigitIso(String threeDigitIso) {
        this.threeDigitIso = threeDigitIso;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public String toString() {
        return
                "NationalitiesItem{" +
                        "name_ar = '" + nameAr + '\'' +
                        ",nationalityId = '" + nationalityId + '\'' +
                        ",threeDigitIso = '" + threeDigitIso + '\'' +
                        ",name_en = '" + nameEn + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}