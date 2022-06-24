package com.aecb.data.api.models.configuration;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ConfigurationData {

    @SerializedName("infoCentre")
    private List<InfoCentreItem> infoCentre;

    @SerializedName("hearAboutUs")
    private List<HearAboutUsItem> hearAboutUs;

    @SerializedName("otpAliveTime")
    private Long otpAliveTime;

    @SerializedName("configurations")
    private List<ConfigurationsItem> configurations;

    @SerializedName("applicationCodes")
    private List<ApplicationCodesItem> applicationCodes;

    @SerializedName("drupalBaseUrl")
    private String drupalBaseUrl;

    @SerializedName("banners")
    private List<BannersItem> banners;

    @SerializedName("caseTypes")
    private List<CaseTypesItem> caseTypes;

    @SerializedName("termsAndConditions")
    private TermsAndConditions termsAndConditions;

    @SerializedName("products")
    private List<ProductsItem> products;

    @SerializedName("notificationIgnoreDuration")
    private int notificationIgnoreDuration;

    @SerializedName("maintenanceEndDate")
    private MaintenanceEndDate maintenanceEndDate;

    @SerializedName("pages")
    private Pages pages;

    @SerializedName("notificationReminderLaterDuration")
    private int notificationReminderLaterDuration;

    @SerializedName("crmConfigChannelSpec")
    private CrmConfigChannelSpec crmConfigChannelSpec;

    @SerializedName("caseOrigins")
    private List<CaseOriginsItem> caseOrigins;

    @SerializedName("maintenanceStartDate")
    private MaintenanceStartDate maintenanceStartDate;

    @SerializedName("isAppFree")
    private boolean isAppFree;

    @SerializedName("refId")
    private String refId;

    @SerializedName("minIosAppVersion")
    private String minIosAppVersion;

    @SerializedName("marketingContent")
    private List<MarketingContentItem> marketingContent;

    @SerializedName("minAndroidAppVersion")
    private String minAndroidAppVersion;

    public List<InfoCentreItem> getInfoCentre() {
        return infoCentre;
    }

    public void setInfoCentre(List<InfoCentreItem> infoCentre) {
        this.infoCentre = infoCentre;
    }

    public List<HearAboutUsItem> getHearAboutUs() {
        return hearAboutUs;
    }

    public void setHearAboutUs(List<HearAboutUsItem> hearAboutUs) {
        this.hearAboutUs = hearAboutUs;
    }

    public Long getOtpAliveTime() {
        return otpAliveTime;
    }

    public void setOtpAliveTime(Long otpAliveTime) {
        this.otpAliveTime = otpAliveTime;
    }

    public List<ConfigurationsItem> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<ConfigurationsItem> configurations) {
        this.configurations = configurations;
    }

    public List<ApplicationCodesItem> getApplicationCodes() {
        return applicationCodes;
    }

    public void setApplicationCodes(List<ApplicationCodesItem> applicationCodes) {
        this.applicationCodes = applicationCodes;
    }

    public String getDrupalBaseUrl() {
        return drupalBaseUrl;
    }

    public void setDrupalBaseUrl(String drupalBaseUrl) {
        this.drupalBaseUrl = drupalBaseUrl;
    }

    public List<BannersItem> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersItem> banners) {
        this.banners = banners;
    }

    public List<CaseTypesItem> getCaseTypes() {
        return caseTypes;
    }

    public void setCaseTypes(List<CaseTypesItem> caseTypes) {
        this.caseTypes = caseTypes;
    }

    public TermsAndConditions getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(TermsAndConditions termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public List<ProductsItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsItem> products) {
        this.products = products;
    }

    public int getNotificationIgnoreDuration() {
        return notificationIgnoreDuration;
    }

    public void setNotificationIgnoreDuration(int notificationIgnoreDuration) {
        this.notificationIgnoreDuration = notificationIgnoreDuration;
    }

    public MaintenanceEndDate getMaintenanceEndDate() {
        return maintenanceEndDate;
    }

    public void setMaintenanceEndDate(MaintenanceEndDate maintenanceEndDate) {
        this.maintenanceEndDate = maintenanceEndDate;
    }

    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }

    public int getNotificationReminderLaterDuration() {
        return notificationReminderLaterDuration;
    }

    public void setNotificationReminderLaterDuration(int notificationReminderLaterDuration) {
        this.notificationReminderLaterDuration = notificationReminderLaterDuration;
    }

    public CrmConfigChannelSpec getCrmConfigChannelSpec() {
        return crmConfigChannelSpec;
    }

    public void setCrmConfigChannelSpec(CrmConfigChannelSpec crmConfigChannelSpec) {
        this.crmConfigChannelSpec = crmConfigChannelSpec;
    }

    public List<CaseOriginsItem> getCaseOrigins() {
        return caseOrigins;
    }

    public void setCaseOrigins(List<CaseOriginsItem> caseOrigins) {
        this.caseOrigins = caseOrigins;
    }

    public MaintenanceStartDate getMaintenanceStartDate() {
        return maintenanceStartDate;
    }

    public void setMaintenanceStartDate(MaintenanceStartDate maintenanceStartDate) {
        this.maintenanceStartDate = maintenanceStartDate;
    }

    public boolean isIsAppFree() {
        return isAppFree;
    }

    public void setIsAppFree(boolean isAppFree) {
        this.isAppFree = isAppFree;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getMinIosAppVersion() {
        return minIosAppVersion;
    }

    public void setMinIosAppVersion(String minIosAppVersion) {
        this.minIosAppVersion = minIosAppVersion;
    }

    public List<MarketingContentItem> getMarketingContent() {
        return marketingContent;
    }

    public void setMarketingContent(List<MarketingContentItem> marketingContent) {
        this.marketingContent = marketingContent;
    }

    public String getMinAndroidAppVersion() {
        return minAndroidAppVersion;
    }

    public void setMinAndroidAppVersion(String minAndroidAppVersion) {
        this.minAndroidAppVersion = minAndroidAppVersion;
    }

    @Override
    public String toString() {
        return
                "ConfigurationData{" +
                        "infoCentre = '" + infoCentre + '\'' +
                        ",hearAboutUs = '" + hearAboutUs + '\'' +
                        ",otpAliveTime = '" + otpAliveTime + '\'' +
                        ",configurations = '" + configurations + '\'' +
                        ",applicationCodes = '" + applicationCodes + '\'' +
                        ",drupalBaseUrl = '" + drupalBaseUrl + '\'' +
                        ",banners = '" + banners + '\'' +
                        ",caseTypes = '" + caseTypes + '\'' +
                        ",termsAndConditions = '" + termsAndConditions + '\'' +
                        ",products = '" + products + '\'' +
                        ",notificationIgnoreDuration = '" + notificationIgnoreDuration + '\'' +
                        ",maintenanceEndDate = '" + maintenanceEndDate + '\'' +
                        ",pages = '" + pages + '\'' +
                        ",notificationReminderLaterDuration = '" + notificationReminderLaterDuration + '\'' +
                        ",crmConfigChannelSpec = '" + crmConfigChannelSpec + '\'' +
                        ",caseOrigins = '" + caseOrigins + '\'' +
                        ",maintenanceStartDate = '" + maintenanceStartDate + '\'' +
                        ",isAppFree = '" + isAppFree + '\'' +
                        ",refId = '" + refId + '\'' +
                        ",minIosAppVersion = '" + minIosAppVersion + '\'' +
                        ",marketingContent = '" + marketingContent + '\'' +
                        ",minAndroidAppVersion = '" + minAndroidAppVersion + '\'' +
                        "}";
    }
}