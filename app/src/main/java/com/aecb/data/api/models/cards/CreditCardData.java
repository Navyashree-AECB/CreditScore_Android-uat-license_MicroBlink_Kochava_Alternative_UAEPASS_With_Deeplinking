package com.aecb.data.api.models.cards;

public class CreditCardData {

    private String cardholdername;
    private String cardnumber;
    private String expiryDate;
    private String bankName;
    private String pymtCardIsDefault;
    private String cardType;
    private String cardStatus;
    private String GatewayCode;

    public CreditCardData(String cardholdername, String cardnumber, String expiryDate, String bankName,
                          String pymtCardIsDefault, String cardType, String cardStatus, String GatewayCode) {
        this.cardholdername = cardholdername;
        this.cardnumber = cardnumber;
        this.expiryDate = expiryDate;
        this.bankName = bankName;
        this.pymtCardIsDefault = pymtCardIsDefault;
        this.cardType = cardType;
        this.cardStatus = cardStatus;
        this.GatewayCode = GatewayCode;
    }

    public String getGatewayCode() {
        return GatewayCode;
    }

    public void setGatewayCode(String gatewayCode) {
        GatewayCode = gatewayCode;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardholdername() {
        return cardholdername;
    }

    public void setCardholdername(String cardholdername) {
        this.cardholdername = cardholdername;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPymtCardIsDefault() {
        return pymtCardIsDefault;
    }

    public void setPymtCardIsDefault(String pymtCardIsDefault) {
        this.pymtCardIsDefault = pymtCardIsDefault;
    }
}