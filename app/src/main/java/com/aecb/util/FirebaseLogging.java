package com.aecb.util;

import android.os.Bundle;

import com.facebook.ads.sdk.serverside.CustomData;
import com.facebook.appevents.AppEventsLogger;
import com.kochava.base.Tracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.aecb.App.appComponent;
import static com.aecb.App.getFirebaseAnalytics;
import static com.aecb.AppConstants.Events.ADDED_CARD_FROM_CHECKOUT;
import static com.aecb.AppConstants.Events.ADDED_PAYMENT_DETAILS;
import static com.aecb.AppConstants.Events.ADDED_RECIPIENTS;
import static com.aecb.AppConstants.Events.ADDED_REMOVED_PRODUCTS;
import static com.aecb.AppConstants.Events.CANCEL_FROM_ADD_CARD;
import static com.aecb.AppConstants.Events.CANCEL_FROM_CARD_VERIFICATION;
import static com.aecb.AppConstants.Events.CANCEL_FROM_CHANGE_PASSWORD;
import static com.aecb.AppConstants.Events.CANCEL_FROM_CHECKOUT;
import static com.aecb.AppConstants.Events.CANCEL_FROM_CREATE_PASSWORD;
import static com.aecb.AppConstants.Events.CANCEL_FROM_FORGOT_PWD;
import static com.aecb.AppConstants.Events.CANCEL_FROM_OTP;
import static com.aecb.AppConstants.Events.CANCEL_FROM_PAYMENT;
import static com.aecb.AppConstants.Events.CANCEL_FROM_RECIPIENT;
import static com.aecb.AppConstants.Events.CANCEL_FROM_SECURITY_ONE;
import static com.aecb.AppConstants.Events.CANCEL_FROM_SECURITY_TWO;
import static com.aecb.AppConstants.Events.CARD_NOT_SCANNED;
import static com.aecb.AppConstants.Events.CARD_SCANNED;
import static com.aecb.AppConstants.Events.CONTACT_US_GUEST;
import static com.aecb.AppConstants.Events.CONTACT_US_REGISTERED_USER;
import static com.aecb.AppConstants.Events.EDITED_EMAIL_FROM_SETTING;
import static com.aecb.AppConstants.Events.EDITED_MOBILE_FROM_SETTING;
import static com.aecb.AppConstants.Events.EDITED_NATIONALITY_FROM_SETTING;
import static com.aecb.AppConstants.Events.EDITED_NATIONALITY_SAVED;
import static com.aecb.AppConstants.Events.EDITED_PASSPORT_FROM_SETTING;
import static com.aecb.AppConstants.Events.EDIT_PASSPORT_CANCELLED;
import static com.aecb.AppConstants.Events.EDIT_PASSPORT_SAVED;
import static com.aecb.AppConstants.Events.ENABLE_DISABLE_TOUCHID;
import static com.aecb.AppConstants.Events.REGISTERED_WITHOUT_SCAN;
import static com.aecb.AppConstants.Events.REGISTERED_WITH_SCAN;
import static com.aecb.AppConstants.Events.SELECTED_EIDRHAM_FROM_CHECKOUT;
import static com.aecb.AppConstants.Events.SWITCHED_LANGUAGE;
import static com.aecb.AppConstants.Events.UPDATED_CARD_EXPIRY;
import static com.aecb.AppConstants.Events.UPDATED_CARD_FULLNAME;
import static com.aecb.AppConstants.Events.UPDATED_CARD_NUMBER;
import static com.aecb.AppConstants.Events.UPDATED_DOB;
import static com.aecb.AppConstants.Events.UPDATED_EMIRATE_ID;
import static com.aecb.AppConstants.Events.UPDATED_FULLNAME;
import static com.aecb.AppConstants.Events.UPDATED_GENDER;
import static com.aecb.AppConstants.Events.UPDATED_NATIONALITY;
import static com.aecb.AppConstants.Events.USER_LOGOUT;
import static com.aecb.AppConstants.Events.VISITED_ABOUT_AECB;
import static com.aecb.AppConstants.Events.VISITED_CHANGE_PASSWORD;
import static com.aecb.AppConstants.Events.VISITED_HELP_SUPPORT;
import static com.aecb.AppConstants.Events.VISITED_MANAGE_PAYMENT;
import static com.aecb.AppConstants.Events.VISITED_PROFILE;
import static com.aecb.AppConstants.Events.VISITED_SCORE_HISTORY;
import static com.aecb.AppConstants.Events.VISITED_T_AND_C;
import static com.aecb.AppConstants.Events.WRONG_PASSWORD_FROM_LOGIN;
import static com.aecb.util.ConversionApi.callConversionApi;

public class FirebaseLogging {

    public static boolean isScanned = false;

    public static Bundle bundle = new Bundle();
    public static AppEventsLogger logger = AppEventsLogger.newLogger(appComponent.appContext());
    public static String currentDate = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault()).format(new Date());

    public static void withoutScanRegistration() {
        bundle.putString("Date", currentDate);
        logger.logEvent(REGISTERED_WITHOUT_SCAN, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, REGISTERED_WITHOUT_SCAN);
        getFirebaseAnalytics().logEvent(REGISTERED_WITHOUT_SCAN, bundle);
        Tracker.sendEvent(new Tracker.Event(REGISTERED_WITHOUT_SCAN));
    }

    public static void withScanRegistration() {
        bundle.putString("Date", currentDate);
        logger.logEvent(REGISTERED_WITH_SCAN, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, REGISTERED_WITH_SCAN);
        getFirebaseAnalytics().logEvent(REGISTERED_WITH_SCAN, bundle);
        Tracker.sendEvent(new Tracker.Event(REGISTERED_WITH_SCAN));
    }

    public static void cancelFromRecipient() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_RECIPIENT, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_RECIPIENT);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_RECIPIENT, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_RECIPIENT));
    }

    public static void cancelFromCheckout() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_CHECKOUT, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_CHECKOUT);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_CHECKOUT, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_CHECKOUT));
    }

    public static void cancelFromAddCard() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_ADD_CARD, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_ADD_CARD);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_ADD_CARD, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_ADD_CARD));
    }

    public static void cancelFromPaymentScreen() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_PAYMENT, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_PAYMENT);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_PAYMENT, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_PAYMENT));
    }

    public static void cancelFromCardVerificationScreen() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_CARD_VERIFICATION, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_CARD_VERIFICATION);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_CARD_VERIFICATION, bundle);
    }

    public static void cancelFromForgotPassword() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_FORGOT_PWD, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_FORGOT_PWD);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_FORGOT_PWD, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_FORGOT_PWD));
    }

    public static void cancelFromOTPScreen() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_OTP, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_OTP);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_OTP, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_OTP));
    }

    public static void cancelCreatePassword() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_CREATE_PASSWORD, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_CREATE_PASSWORD);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_CREATE_PASSWORD, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_CREATE_PASSWORD));
    }

    public static void incorrectPasswordLogin() {
        bundle.putString("Date", currentDate);
        logger.logEvent(WRONG_PASSWORD_FROM_LOGIN, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, WRONG_PASSWORD_FROM_LOGIN);
        getFirebaseAnalytics().logEvent(WRONG_PASSWORD_FROM_LOGIN, bundle);
        Tracker.sendEvent(new Tracker.Event(WRONG_PASSWORD_FROM_LOGIN));
    }

    public static void cancelChangePassword() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_CHANGE_PASSWORD, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_CHANGE_PASSWORD);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_CHANGE_PASSWORD, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_CHANGE_PASSWORD));
    }

    public static void cancelSecurityRoundOne() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_SECURITY_ONE, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_SECURITY_ONE);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_SECURITY_ONE, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_SECURITY_ONE));
    }

    public static void cancelSecurityRoundTwo() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CANCEL_FROM_SECURITY_TWO, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CANCEL_FROM_SECURITY_TWO);
        getFirebaseAnalytics().logEvent(CANCEL_FROM_SECURITY_TWO, bundle);
        Tracker.sendEvent(new Tracker.Event(CANCEL_FROM_SECURITY_TWO));
    }

    public static void contactUsGuest() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CONTACT_US_GUEST, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CONTACT_US_GUEST);
        getFirebaseAnalytics().logEvent(CONTACT_US_GUEST, bundle);
        Tracker.sendEvent(new Tracker.Event(CONTACT_US_GUEST));
    }


    public static void contactUsRegisteredUser() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CONTACT_US_REGISTERED_USER, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CONTACT_US_REGISTERED_USER);
        getFirebaseAnalytics().logEvent(CONTACT_US_REGISTERED_USER, bundle);
        Tracker.sendEvent(new Tracker.Event(CONTACT_US_REGISTERED_USER));
    }

    public static void fullNameUpdate() {
        bundle.putString("Date", currentDate);
        logger.logEvent(UPDATED_FULLNAME, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, UPDATED_FULLNAME);
        getFirebaseAnalytics().logEvent(UPDATED_FULLNAME, bundle);
        Tracker.sendEvent(new Tracker.Event(UPDATED_FULLNAME));
    }

    public static void emirateIdUpdate() {
        bundle.putString("Date", currentDate);
        logger.logEvent(UPDATED_EMIRATE_ID, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, UPDATED_EMIRATE_ID);
        getFirebaseAnalytics().logEvent(UPDATED_EMIRATE_ID, bundle);
        Tracker.sendEvent(new Tracker.Event(UPDATED_EMIRATE_ID));
    }

    public static void genderUpdate() {
        bundle.putString("Date", currentDate);
        logger.logEvent(UPDATED_GENDER, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, UPDATED_GENDER);
        getFirebaseAnalytics().logEvent(UPDATED_GENDER, bundle);
        Tracker.sendEvent(new Tracker.Event(UPDATED_GENDER));
    }

    public static void dobUpdate() {
        bundle.putString("Date", currentDate);
        logger.logEvent(UPDATED_DOB, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, UPDATED_DOB);
        getFirebaseAnalytics().logEvent(UPDATED_DOB, bundle);
        Tracker.sendEvent(new Tracker.Event(UPDATED_DOB));
    }

    public static void nationalityUpdate() {
        bundle.putString("Date", currentDate);
        logger.logEvent(UPDATED_NATIONALITY, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, UPDATED_NATIONALITY);
        getFirebaseAnalytics().logEvent(UPDATED_NATIONALITY, bundle);
        Tracker.sendEvent(new Tracker.Event(UPDATED_NATIONALITY));
    }

    public static void cardScanned() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CARD_SCANNED, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CARD_SCANNED);
        getFirebaseAnalytics().logEvent(CARD_SCANNED, bundle);
        Tracker.sendEvent(new Tracker.Event(CARD_SCANNED));
    }

    public static void cardNotScanned() {
        bundle.putString("Date", currentDate);
        logger.logEvent(CARD_NOT_SCANNED, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, CARD_NOT_SCANNED);
        getFirebaseAnalytics().logEvent(CARD_NOT_SCANNED, bundle);
        Tracker.sendEvent(new Tracker.Event(CARD_NOT_SCANNED));
    }

    public static void profileVisited() {
        bundle.putString("Date", currentDate);
        logger.logEvent(VISITED_PROFILE, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, VISITED_PROFILE);
        getFirebaseAnalytics().logEvent(VISITED_PROFILE, bundle);
        Tracker.sendEvent(new Tracker.Event(VISITED_PROFILE));
    }

    public static void visitedAboutAecb() {
        bundle.putString("Date", currentDate);
        logger.logEvent(VISITED_ABOUT_AECB, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, VISITED_ABOUT_AECB);
        getFirebaseAnalytics().logEvent(VISITED_ABOUT_AECB, bundle);
        Tracker.sendEvent(new Tracker.Event(VISITED_ABOUT_AECB));
    }

    public static void visitedTandC() {
        bundle.putString("Date", currentDate);
        logger.logEvent(VISITED_T_AND_C, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, VISITED_T_AND_C);
        getFirebaseAnalytics().logEvent(VISITED_T_AND_C, bundle);
        Tracker.sendEvent(new Tracker.Event(VISITED_T_AND_C));
    }

    public static void languageSwitched() {
        bundle.putString("Date", currentDate);
        logger.logEvent(SWITCHED_LANGUAGE, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, SWITCHED_LANGUAGE);
        getFirebaseAnalytics().logEvent(SWITCHED_LANGUAGE, bundle);
        Tracker.sendEvent(new Tracker.Event(SWITCHED_LANGUAGE));
    }

    public static void enableDisableTouchId() {
        bundle.putString("Date", currentDate);
        logger.logEvent(ENABLE_DISABLE_TOUCHID, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, ENABLE_DISABLE_TOUCHID);
        getFirebaseAnalytics().logEvent(ENABLE_DISABLE_TOUCHID, bundle);
        Tracker.sendEvent(new Tracker.Event(ENABLE_DISABLE_TOUCHID));
    }

    public static void visitedManagePayment() {
        bundle.putString("Date", currentDate);
        logger.logEvent(VISITED_MANAGE_PAYMENT, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, VISITED_MANAGE_PAYMENT);
        getFirebaseAnalytics().logEvent(VISITED_MANAGE_PAYMENT, bundle);
        Tracker.sendEvent(new Tracker.Event(VISITED_MANAGE_PAYMENT));
    }

    public static void visitedChangePassword() {
        bundle.putString("Date", currentDate);
        logger.logEvent(VISITED_CHANGE_PASSWORD, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, VISITED_CHANGE_PASSWORD);
        getFirebaseAnalytics().logEvent(VISITED_CHANGE_PASSWORD, bundle);
        Tracker.sendEvent(new Tracker.Event(VISITED_CHANGE_PASSWORD));
    }

    public static void userLoggedOut() {
        bundle.putString("Date", currentDate);
        logger.logEvent(USER_LOGOUT, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, USER_LOGOUT);
        getFirebaseAnalytics().logEvent(USER_LOGOUT, bundle);
        Tracker.sendEvent(new Tracker.Event(USER_LOGOUT));
    }

    public static void addedRecipients() {
        bundle.putString("Date", currentDate);
        logger.logEvent(ADDED_RECIPIENTS, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, ADDED_RECIPIENTS);
        getFirebaseAnalytics().logEvent(ADDED_RECIPIENTS, bundle);
        Tracker.sendEvent(new Tracker.Event(ADDED_RECIPIENTS));
    }

    public static void addedRemovedProducts() {
        bundle.putString("Date", currentDate);
        logger.logEvent(ADDED_REMOVED_PRODUCTS, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, ADDED_REMOVED_PRODUCTS);
        getFirebaseAnalytics().logEvent(ADDED_REMOVED_PRODUCTS, bundle);
        Tracker.sendEvent(new Tracker.Event(ADDED_REMOVED_PRODUCTS));
    }

    public static void addedPaymentDetails() {
        bundle.putString("Date", currentDate);
        logger.logEvent(ADDED_PAYMENT_DETAILS, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, ADDED_PAYMENT_DETAILS);
        getFirebaseAnalytics().logEvent(ADDED_PAYMENT_DETAILS, bundle);
        Tracker.sendEvent(new Tracker.Event(ADDED_PAYMENT_DETAILS));
    }

    public static void selectedEDirhamFromCheckout() {
        bundle.putString("Date", currentDate);
        logger.logEvent(SELECTED_EIDRHAM_FROM_CHECKOUT, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, SELECTED_EIDRHAM_FROM_CHECKOUT);
        getFirebaseAnalytics().logEvent(SELECTED_EIDRHAM_FROM_CHECKOUT, bundle);
        Tracker.sendEvent(new Tracker.Event(SELECTED_EIDRHAM_FROM_CHECKOUT));
    }

    public static void addedCardFromCheckout() {
        bundle.putString("Date", currentDate);
        logger.logEvent(ADDED_CARD_FROM_CHECKOUT, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, ADDED_CARD_FROM_CHECKOUT);
        getFirebaseAnalytics().logEvent(ADDED_CARD_FROM_CHECKOUT, bundle);
        Tracker.sendEvent(new Tracker.Event(ADDED_CARD_FROM_CHECKOUT));
    }

    public static void updatedCardNumber() {
        bundle.putString("Date", currentDate);
        logger.logEvent(UPDATED_CARD_NUMBER, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, UPDATED_CARD_NUMBER);
        getFirebaseAnalytics().logEvent(UPDATED_CARD_NUMBER, bundle);
        Tracker.sendEvent(new Tracker.Event(UPDATED_CARD_NUMBER));
    }

    public static void updatedCardFullname() {
        bundle.putString("Date", currentDate);
        logger.logEvent(UPDATED_CARD_FULLNAME, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, UPDATED_CARD_FULLNAME);
        getFirebaseAnalytics().logEvent(UPDATED_CARD_FULLNAME, bundle);
    }

    public static void updatedCardExpiry() {
        bundle.putString("Date", currentDate);
        logger.logEvent(UPDATED_CARD_EXPIRY, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, UPDATED_CARD_EXPIRY);
        getFirebaseAnalytics().logEvent(UPDATED_CARD_EXPIRY, bundle);
    }

    public static void editedPassportFromSetting() {
        bundle.putString("Date", currentDate);
        logger.logEvent(EDITED_PASSPORT_FROM_SETTING, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, EDITED_PASSPORT_FROM_SETTING);
        getFirebaseAnalytics().logEvent(EDITED_PASSPORT_FROM_SETTING, bundle);
        Tracker.sendEvent(new Tracker.Event(EDITED_PASSPORT_FROM_SETTING));
    }

    public static void editedNationalityFromSetting() {
        bundle.putString("Date", currentDate);
        logger.logEvent(EDITED_NATIONALITY_FROM_SETTING, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, EDITED_NATIONALITY_FROM_SETTING);
        getFirebaseAnalytics().logEvent(EDITED_NATIONALITY_FROM_SETTING, bundle);
        Tracker.sendEvent(new Tracker.Event(EDITED_NATIONALITY_FROM_SETTING));
    }

    public static void editedMobileFromSetting() {
        bundle.putString("Date", currentDate);
        logger.logEvent(EDITED_MOBILE_FROM_SETTING, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, EDITED_MOBILE_FROM_SETTING);
        getFirebaseAnalytics().logEvent(EDITED_MOBILE_FROM_SETTING, bundle);
        Tracker.sendEvent(new Tracker.Event(EDITED_MOBILE_FROM_SETTING));
    }

    public static void editedEmailFromSetting() {
        bundle.putString("Date", currentDate);
        logger.logEvent(EDITED_EMAIL_FROM_SETTING, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, EDITED_EMAIL_FROM_SETTING);
        getFirebaseAnalytics().logEvent(EDITED_EMAIL_FROM_SETTING, bundle);
        Tracker.sendEvent(new Tracker.Event(EDITED_EMAIL_FROM_SETTING));
    }

    public static void editedPassportSaved() {
        bundle.putString("Date", currentDate);
        logger.logEvent(EDIT_PASSPORT_SAVED, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, EDIT_PASSPORT_SAVED);
        getFirebaseAnalytics().logEvent(EDIT_PASSPORT_SAVED, bundle);
        Tracker.sendEvent(new Tracker.Event(EDIT_PASSPORT_SAVED));
    }

    public static void editedPassportCancelled() {
        bundle.putString("Date", currentDate);
        logger.logEvent(EDIT_PASSPORT_CANCELLED, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, EDIT_PASSPORT_CANCELLED);
        getFirebaseAnalytics().logEvent(EDIT_PASSPORT_CANCELLED, bundle);
        Tracker.sendEvent(new Tracker.Event(EDIT_PASSPORT_CANCELLED));
    }

    public static void editedNationalitySaved() {
        bundle.putString("Date", currentDate);
        logger.logEvent(EDITED_NATIONALITY_SAVED, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, EDITED_NATIONALITY_SAVED);
        getFirebaseAnalytics().logEvent(EDITED_NATIONALITY_SAVED, bundle);
        Tracker.sendEvent(new Tracker.Event(EDITED_NATIONALITY_SAVED));
    }

    public static void visitedScoreHistory() {
        bundle.putString("Date", currentDate);
        logger.logEvent(VISITED_SCORE_HISTORY, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, VISITED_SCORE_HISTORY);
        getFirebaseAnalytics().logEvent(VISITED_SCORE_HISTORY, bundle);
        Tracker.sendEvent(new Tracker.Event(VISITED_SCORE_HISTORY));
    }

    public static void visitedHelpSupport() {
        bundle.putString("Date", currentDate);
        logger.logEvent(VISITED_HELP_SUPPORT, bundle);
        CustomData customData = new CustomData();
        HashMap<String, String> data = new HashMap<>();
        data.put("Date", currentDate);
        customData.setCustomProperties(data);
        callConversionApi(customData, VISITED_HELP_SUPPORT);
        getFirebaseAnalytics().logEvent(VISITED_HELP_SUPPORT, bundle);
        Tracker.sendEvent(new Tracker.Event(VISITED_HELP_SUPPORT));
    }


}