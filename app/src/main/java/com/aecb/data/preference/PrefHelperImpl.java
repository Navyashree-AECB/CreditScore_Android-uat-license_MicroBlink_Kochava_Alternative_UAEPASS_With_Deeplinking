package com.aecb.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.data.api.models.configuration.ConfigurationData;
import com.google.gson.Gson;
import com.tumblr.remember.Remember;

import javax.inject.Inject;

public final class PrefHelperImpl implements PrefHelper {

    public static final String KEY_CURRENT_LANGUAGE = "KEY_CURRENT_LANGUAGE";
    public static final String KEY_PRODUCTS_LIST = "KEY_PRODUCTS_LIST";
    public static final String KEY_CONFIGURATION_DATA = "KEY_CONFIGURATION_DATA";
    public static final String KEY_PURCHASE_HISTORY = "KEY_PURCHASE_HISTORY";
    public static String KEY_SESSION_TOKEN = "KEY_SESSION_TOKEN";
    private static String KEY_APP_LANGUAGE = "APP_LANGUAGE";
    private static String KEY_TOUCH_ID = "TOUCH_ID";
    private static String KEY_USER_SESSION = "USER_SESSTION";
    private static String KEY_PREVIOUS_RANDOM_ID = "KEY_PREVIOUS_RANDOM_ID";
    private final String KEY_IS_LOGGEDIN = "KEY_IS_LOGGEDIN";
    private final String KEY_FCM_ID = "KEY_FCM_ID";
    private final String IS_APP_FIRST_TIME = "IS_APP_FIRST_TIME";
    private final String KEY_OTP_TIME = "KEY_OTP_TIME";
    private final String KEY_TC_VERSION_NUMBER = "KEY_TC_VERSION_NUMBER";
    private final String KEY_INSTALL_SOURCE = "KEY_INSTALL_SOURCE";
    private final String KEY_NORMAL_INSTALL = "KEY_NORMAL_INSTALL";
    private final String KEY_INSTALL_CAMPAIGN_ID = "KEY_INSTALL_CAMPAIGN_ID";

    @Inject
    PrefHelperImpl() {
    }

    public static String getAppLanguage() {
        return Remember.getString(KEY_APP_LANGUAGE, "");
    }

    public static void setAppLanguage(String value) {
        Remember.putString(KEY_APP_LANGUAGE, value);
    }

    public void clearAll() {
        Remember.clear();
    }

    public boolean isLoggedIn() {
        return Remember.getBoolean(this.KEY_IS_LOGGEDIN, false);
    }

    public void setLoggedIn(boolean value) {
        Remember.putBoolean(this.KEY_IS_LOGGEDIN, value);
    }

    public String getFcmId() {
        return Remember.getString(this.KEY_FCM_ID, "");
    }

    public void setFcmId(String value) {
        Remember.putString(this.KEY_FCM_ID, value);
    }

    @Override
    public String getSessionToken() {
        return Remember.getString(KEY_SESSION_TOKEN, "");
    }

    @Override
    public void setSessionToken(String sessionToken) {
        Remember.putString(KEY_SESSION_TOKEN, sessionToken);
    }

    public boolean isFirstTime() {
        return Remember.getBoolean(this.IS_APP_FIRST_TIME, true);
    }

    public void isFirstTime(boolean isAppFirstTime) {
        Remember.putBoolean(this.IS_APP_FIRST_TIME, isAppFirstTime);
    }

    @Override
    public String getCurrentLanguage() {
        return Remember.getString(KEY_CURRENT_LANGUAGE, AppConstants.AppLanguage.ISO_CODE_ENG);
    }

    @Override
    public void setCurrentLanguage(String currentLanguage) {
        Remember.putString(KEY_CURRENT_LANGUAGE, currentLanguage);
    }

    @Override
    public Long getOtpTime() {
        return Remember.getLong(KEY_OTP_TIME, 0);
    }

    @Override
    public void setOtpTime(Long otpTime) {
        Remember.putLong(KEY_OTP_TIME, otpTime);
    }

    @Override
    public int getTcVersionNumber() {
        return Remember.getInt(KEY_TC_VERSION_NUMBER, 0);
    }

    @Override
    public void setTcVersionNumber(int tcVersionNumber) {
        Remember.putInt(KEY_TC_VERSION_NUMBER, tcVersionNumber);
    }

    @Override
    public ConfigurationData getConfigurationData() {
        String value = Remember.getString(KEY_CONFIGURATION_DATA, "");
        Gson gson = new Gson();
        return gson.fromJson(value, ConfigurationData.class);
    }

    @Override
    public void setConfigurationData(String configurationData) {
        Remember.putString(KEY_CONFIGURATION_DATA, configurationData);
    }

    public String getUserSession() {
        return Remember.getString(KEY_USER_SESSION, "");
    }

    @Override
    public void setUserSession(String userSession) {
        Remember.putString(KEY_USER_SESSION, userSession);
    }

    @Override
    public String getPreviousRandomDeviceID() {
        return Remember.getString(KEY_PREVIOUS_RANDOM_ID, "");
    }

    @Override
    public void setPreviousRandomDeviceID(String randomDeviceID) {
        Remember.putString(KEY_PREVIOUS_RANDOM_ID, randomDeviceID);
    }

    @Override
    public boolean getTouchLoggedIn() {
        return Remember.getBoolean(KEY_TOUCH_ID, false);
    }

    @Override
    public void setTouchLoggedIn(boolean touchLoggedIn) {
        Remember.putBoolean(KEY_TOUCH_ID, touchLoggedIn);
    }

    @Override
    public String getProductList() {
        return Remember.getString(KEY_PRODUCTS_LIST, "");
    }

    @Override
    public void setProductList(String productList) {
        Remember.putString(KEY_PRODUCTS_LIST, productList);
    }

    @Override
    public SharedPreferences prefReference() {
        String prefName = App.getAppComponent().appContext().getString(R.string.app_name);
        return App.getAppComponent().appContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    @Override
    public String getPurchaseHistory() {
        return Remember.getString(KEY_PURCHASE_HISTORY, "");
    }

    @Override
    public void setPurchaseHistory(String purchaseHistory) {
        Remember.putString(KEY_PURCHASE_HISTORY, purchaseHistory);
    }

    @Override
    public void removeSessionToken() {
        Remember.remove(KEY_SESSION_TOKEN);
    }

    @Override
    public String getInstallSource() {
        return Remember.getString(KEY_INSTALL_SOURCE, "");
    }

    @Override
    public void setInstallSource(String installSource) {
        Remember.putString(KEY_INSTALL_SOURCE, installSource);
    }

    @Override
    public String getInstalledCampaignId() {
        return Remember.getString(KEY_INSTALL_CAMPAIGN_ID, "");
    }

    @Override
    public void setInstalledCampaignId(String installedCampaignId) {
        Remember.putString(KEY_INSTALL_CAMPAIGN_ID, installedCampaignId);
    }

    @Override
    public boolean isNormalInstall() {
        return Remember.getBoolean(KEY_NORMAL_INSTALL, false);
    }

    @Override
    public void setNormalInstall(boolean normalInstall) {
        Remember.putBoolean(KEY_NORMAL_INSTALL, normalInstall);
    }
}
