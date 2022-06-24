package com.aecb.data.preference;

import android.content.SharedPreferences;

import com.aecb.data.api.models.configuration.ConfigurationData;

public interface PrefHelper {
    void clearAll();

    boolean isLoggedIn();

    void setLoggedIn(boolean isLoggedIn);

    String getFcmId();

    void setFcmId(String fcmId);

    String getSessionToken();

    void setSessionToken(String sessionToken);

    boolean isFirstTime();

    void isFirstTime(boolean isFirstTime);

    String getCurrentLanguage();

    void setCurrentLanguage(String currentLanguage);

    Long getOtpTime();

    void setOtpTime(Long otpTime);

    int getTcVersionNumber();

    void setTcVersionNumber(int tcVersionNumber);

    ConfigurationData getConfigurationData();

    void setConfigurationData(String configurationData);

    String getUserSession();

    void setUserSession(String userSession);

    String getPreviousRandomDeviceID();

    void setPreviousRandomDeviceID(String randomDeviceID);

    boolean getTouchLoggedIn();

    void setTouchLoggedIn(boolean enable);

    String getProductList();

    void setProductList(String productList);

    SharedPreferences prefReference();

    String getPurchaseHistory();

    void setPurchaseHistory(String purchaseHistory);

    void removeSessionToken();

    String getInstallSource();

    void setInstallSource(String installSource);

    String getInstalledCampaignId();

    void setInstalledCampaignId(String installedCampaignId);

    boolean isNormalInstall();

    void setNormalInstall(boolean normalInstall);
}