package com.aecb.data.api.models.configuration;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ProductsItem {

    @SerializedName("why_upgrade_en")
    private String whyUpgradeEn;

    @SerializedName("button_title_ar")
    private String buttonTitleAr;

    @SerializedName("vat")
    private double vat;

    @SerializedName("why_upgrade_ar")
    private String whyUpgradeAr;

    @SerializedName("productNumber")
    private String productNumber;

    @SerializedName("title_ar")
    private String titleAr;

    @SerializedName("desc_ar")
    private String descAr;

    @SerializedName("scoreDependent")
    private boolean scoreDependent;

    @SerializedName("price")
    private double price;

    @SerializedName("upgrade_with")
    private String upgradeWith;

    @SerializedName("name")
    private String name;

    @SerializedName("title_en")
    private String titleEn;

    @SerializedName("currency")
    private Object currency;

    @SerializedName("upgradeable_to")
    private String upgradeableTo;

    @SerializedName("id")
    private String id;

    @SerializedName("button_title_en")
    private String buttonTitleEn;

    @SerializedName("desc_en")
    private String descEn;

    @SerializedName("order")
    private int order;

    public String getWhyUpgradeEn() {
        return whyUpgradeEn;
    }

    public void setWhyUpgradeEn(String whyUpgradeEn) {
        this.whyUpgradeEn = whyUpgradeEn;
    }

    public String getButtonTitleAr() {
        return buttonTitleAr;
    }

    public void setButtonTitleAr(String buttonTitleAr) {
        this.buttonTitleAr = buttonTitleAr;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public String getWhyUpgradeAr() {
        return whyUpgradeAr;
    }

    public void setWhyUpgradeAr(String whyUpgradeAr) {
        this.whyUpgradeAr = whyUpgradeAr;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getDescAr() {
        return descAr;
    }

    public void setDescAr(String descAr) {
        this.descAr = descAr;
    }

    public boolean isScoreDependent() {
        return scoreDependent;
    }

    public void setScoreDependent(boolean scoreDependent) {
        this.scoreDependent = scoreDependent;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUpgradeWith() {
        return upgradeWith;
    }

    public void setUpgradeWith(String upgradeWith) {
        this.upgradeWith = upgradeWith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public Object getCurrency() {
        return currency;
    }

    public void setCurrency(Object currency) {
        this.currency = currency;
    }

    public String getUpgradeableTo() {
        return upgradeableTo;
    }

    public void setUpgradeableTo(String upgradeableTo) {
        this.upgradeableTo = upgradeableTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getButtonTitleEn() {
        return buttonTitleEn;
    }

    public void setButtonTitleEn(String buttonTitleEn) {
        this.buttonTitleEn = buttonTitleEn;
    }

    public String getDescEn() {
        return descEn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return
                "ProductsItem{" +
                        "why_upgrade_en = '" + whyUpgradeEn + '\'' +
                        ",button_title_ar = '" + buttonTitleAr + '\'' +
                        ",vat = '" + vat + '\'' +
                        ",why_upgrade_ar = '" + whyUpgradeAr + '\'' +
                        ",productNumber = '" + productNumber + '\'' +
                        ",title_ar = '" + titleAr + '\'' +
                        ",desc_ar = '" + descAr + '\'' +
                        ",scoreDependent = '" + scoreDependent + '\'' +
                        ",price = '" + price + '\'' +
                        ",upgrade_with = '" + upgradeWith + '\'' +
                        ",name = '" + name + '\'' +
                        ",title_en = '" + titleEn + '\'' +
                        ",currency = '" + currency + '\'' +
                        ",upgradeable_to = '" + upgradeableTo + '\'' +
                        ",id = '" + id + '\'' +
                        ",button_title_en = '" + buttonTitleEn + '\'' +
                        ",desc_en = '" + descEn + '\'' +
                        ",order = '" + order + '\'' +
                        "}";
    }
}