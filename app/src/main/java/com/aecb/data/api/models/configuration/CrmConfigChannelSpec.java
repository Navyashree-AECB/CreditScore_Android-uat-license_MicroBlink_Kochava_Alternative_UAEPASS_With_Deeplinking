package com.aecb.data.api.models.configuration;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class CrmConfigChannelSpec {

    @SerializedName("renewalReminder")
    private Object renewalReminder;

    @SerializedName("deleteWindow")
    private Object deleteWindow;

    @SerializedName("upcomingPayments")
    private Object upcomingPayments;

    @SerializedName("chequeScanDuration")
    private Object chequeScanDuration;

    @SerializedName("chequeDueDuration")
    private Object chequeDueDuration;

    @SerializedName("paymentReminder")
    private Object paymentReminder;

    public Object getRenewalReminder() {
        return renewalReminder;
    }

    public void setRenewalReminder(Object renewalReminder) {
        this.renewalReminder = renewalReminder;
    }

    public Object getDeleteWindow() {
        return deleteWindow;
    }

    public void setDeleteWindow(Object deleteWindow) {
        this.deleteWindow = deleteWindow;
    }

    public Object getUpcomingPayments() {
        return upcomingPayments;
    }

    public void setUpcomingPayments(Object upcomingPayments) {
        this.upcomingPayments = upcomingPayments;
    }

    public Object getChequeScanDuration() {
        return chequeScanDuration;
    }

    public void setChequeScanDuration(Object chequeScanDuration) {
        this.chequeScanDuration = chequeScanDuration;
    }

    public Object getChequeDueDuration() {
        return chequeDueDuration;
    }

    public void setChequeDueDuration(Object chequeDueDuration) {
        this.chequeDueDuration = chequeDueDuration;
    }

    public Object getPaymentReminder() {
        return paymentReminder;
    }

    public void setPaymentReminder(Object paymentReminder) {
        this.paymentReminder = paymentReminder;
    }

    @Override
    public String toString() {
        return
                "CrmConfigChannelSpec{" +
                        "renewalReminder = '" + renewalReminder + '\'' +
                        ",deleteWindow = '" + deleteWindow + '\'' +
                        ",upcomingPayments = '" + upcomingPayments + '\'' +
                        ",chequeScanDuration = '" + chequeScanDuration + '\'' +
                        ",chequeDueDuration = '" + chequeDueDuration + '\'' +
                        ",paymentReminder = '" + paymentReminder + '\'' +
                        "}";
    }
}