package com.aecb;

import android.annotation.SuppressLint;
import android.content.Context;

import com.aecb.data.api.models.getproducts.ProductsItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class AppConstants {
    public static final String DB_NAME = "my-app-db";
    public static final int DB_VERSION = 3;
    public static final long CACHE_SIZE = 10 * 1024 * 1024L;
    public static final long API_TIMEOUT_TIME = 300L;
    public static final String HASH = "#";
    public static final String dfs = "OWwvfjZeOg==";
    public static final String dls = "OiZ3LiktMlI=";
    public static final int MINIMUM_REQUIRED_AGE = 18;
    public static final String TWO_DIGIT = "%02d";
    public static final String DEFAULT_TIMER = "0";
    public static final int COUNTDOWN_INTERVAL_TIME = 1000;
    public static final String MOBILE_NUMBER_PREFIX = "+971 5";
    public static final int MOBILE_NUMBER_MAX_LENGTH = 14;
    public static final String WEBVIEW_MIME_TYPE = "text/html";
    public static final String WEBVIEW_ENCODING = "UTF-8";
    public static final String CARD_PREFIX = "**** **** **** ";
    public static final String OK = "OK";
    public static final String YES = "YES";
    public static final String ZERO = "0";
    public static final String MINUS = "-";
    public static final String DIVISION = "/";
    public static final String PERCENTAGE = "%";
    public static final byte[] fs = {97, 101, 99, 98, 64, 111, 109, 110, 105, 99, 104, 97, 110, 110, 101, 108};
    public static final String COLON = ":";
    public static final String COMMA = ",";
    public static final String WEBVIEW_USERNAME = "aecb";
    public static final String WEBVIEW_PASSWORD = "DC2018SoftLaunch";
    public static final String FIREBASE_TOPIC = "aecb";
    public static final String PLUS = "+";
    public static int BottomBarHeight = 0;
    public static int ScreenHeight = 0;
    public static SimpleDateFormat DISPLAY_DATE_FORMAT =
            new SimpleDateFormat(DateFormats.DISPLAY_FORMAT, Locale.ENGLISH);
    public static SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat(AppConstants.DateFormats.DD_MM_YY, Locale.ENGLISH);
    public static SimpleDateFormat SIMPLEDATEFORMAT_FOR_DOB = new SimpleDateFormat(AppConstants.DateFormats.DD_MM_YY_DOB, Locale.ENGLISH);
    public static SimpleDateFormat CREDIT_REPORT_DASHBOARD_FORMAT = new SimpleDateFormat(AppConstants.DateFormats.MMM_dd_yyyy, Locale.ENGLISH);
    public static String mobileNumber = "";
    public static String email = "";
    public static String confirmEmail = "";

    public static void setLocalLanguageEng(Context context) {
        Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
        context.getResources().getConfiguration().setLocale(localeStartDate);
        Locale.setDefault(context.getResources().getConfiguration().locale);
    }

    public static String generateRandomID() {
        String alphaNumeric = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            int index = (int) (alphaNumeric.length() * Math.random());
            sb.append(alphaNumeric.charAt(index));
        }
        return sb.toString();
    }

    public static String doubleDecimalValue(int decimals, double number) {
        StringBuilder sb = new StringBuilder(decimals + 2);
        sb.append("#.");
        for (int i = 0; i < decimals; i++) {
            sb.append("0");
        }
        return new DecimalFormat(sb.toString()).format(number);
    }

    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        String s = new String(chars);
        if (s.contains("٫")) {
            s = s.replace("٫", ".");
        }
        return s;
    }

    public static final class ReadMoreUrls {
        public static String readMoreLinkOneEn = "https://aecb.gov.ae/credit-report";
        public static String readMoreLinkTwoEn = "https://aecb.gov.ae/credit-score";
        public static String readMoreLinkThreeEn = "https://aecb.gov.ae/sites/default/files/2018-11/New%20Glossary.pdf";
        public static String readMoreLinkOneAr = "https://aecb.gov.ae/ar/credit-report";
        public static String readMoreLinkTwoEnAr = "https://aecb.gov.ae/ar/credit-score";
        public static String readMoreLinkThreeAr = "https://aecb.gov.ae/sites/default/files/2018-11/New%20Glossary.pdf";
    }

    public static final class ApiParameter {
        public static final String STATUS_200 = "200";
        public static final String STATUS_209 = "209";
        public static final String DEVICE_TYPE = "android";
        public static final String EMAIL = "email";
        public static final String CRED_LOGIN = "credlogin";
        public static final String LOGIN_THUMB_ID = "thumbId";
        public static final String CONST_ADD = "add";
        public static final String FORGOT_PASSWORD = "forgotPassword";
        public static final String CARD_DEFAULT = "default";
        public static final String CARD_DELETE = "delete";
        public static final String CARD_ACTIVATE = "activatecard";
        public static final String TERM_URL = "payment/mobileedirham";
    }

    public static final class OTPFor {
        public static final String OTP_FOR_REGISTRATION = "1";
        public static final String FOR_CHANGE_PASSWORD = "2";
        public static final String OTP_FOR_FORGOT_PASSWORD = "3";
        public static final int OTP_REQ_USER_CONSENT = 200;

    }

    public static final class AppLanguage {
        public static final String ISO_CODE_ENG = "en";
        public static final String ISO_CODE_ARABIC = "ar";
    }

    public static final class Gender {
        public static final String MALE = "Male";
        public static final String FEMALE = "Female";
        public static final String MALE_CODE = "M";
        public static final String FEMALE_CODE = "F";
    }

    public static final class OrientationType {
        public static final int ORIENTATION_PORTRAIT_NORMAL = 1;
        public static final int ORIENTATION_PORTRAIT_INVERTED = 2;
        public static final int ORIENTATION_LANDSCAPE_NORMAL = 3;
        public static final int ORIENTATION_LANDSCAPE_INVERTED = 4;
    }

    public static final class IntentKey {
        public static final String IMAGEURI = "image_uri";
        public static final String MIN_DATE = "MIN_DATE";
        public static final String MAX_DATE = "MAX_DATE";
        public static final String DATE_PICKER_IDENTIFIER = "DATE_PICKER_IDENTIFIER";
        public static final String CURRENT_DATE = "CURRENT_DATE";
        public static final String DATE = "DATE";
        public static final String DATE_FORMAT = "DATE_FORMAT";
        public static final String FROM_ACTIVITY = "FROM_ACTIVITY";
        public static final String OPERATION_TYPE = "OPERATION_TYPE";
        public static final String IMAGE_ORIENTATION = "IMAGE_ORIENTATION";
        public static final String POSITIVE_TEXT = "POSITIVE_TEXT";
        public static final String TITLE_TEXT = "TITLE_TEXT";
        public static final String TRANSACTION_ID = "TRANSACTION_ID";
        public static final String REGISTER_REQUEST = "REGISTER_REQUEST";
        public static final String OTP_GENERATED_FROM_REGISTER = "OTP_GENERATED_FROM_REGISTER";
        public static final String OTP_GENERATED_FROM_FORGOT_PASSWORD = "OTP_GENERATED_FROM_FORGOT_PASSWORD";
        public static final String ROUND_QUESTIONS_FROM_REGISTER = "ROUND_QUESTIONS_FROM_REGISTER";
        public static final String MOBILE = "MOBILE";
        public static final String EMAIL = "EMAIL";
        public static final String REFERENCE_ID = "REFERENCE_ID";
        public static final String SESSION_TOKEN = "SESSION_TOKEN";
        public static final String NATIONALITY_LIST = "NATIONALITY_LIST";
        public static final String USER_NAME = "USER_NAME";
        public static final String OPEN_TC_FOR = "OPEN_TC_FOR";
        public static final String UPDATE_TC_VERSION = "UPDATE_TC_VERSION";
        public static final String UPDATE_DEVICE_ID = "UPDATE_DEVICE_ID";
        public static final String INSERT_NEW_USER = "INSERT_NEW_USER";
        public static final String OTP_TOKEN = "OTP_TOKEN";
        public static final String REF_ID = "REF_ID";
        public static final String CAN_RESEND = "CAN_RESEND";
        public static final String TC_VERSION = "TC_VERSION";
        public static final String BUNDLE_USER_DETAILS = "BUNDLE_USER_DETAILS";
        public static final String SELECTED_PRODUCT_NUMBER = "SELECTED_PRODUCT_NUMBER";
        public static final String PRODUCT_UPGRADABLE_TO = "PRODUCT_UPGRADABLE_TO";
        public static final String REGISTRATION = "registration";
        public static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";
        public static final String NEGATIVE_TEXT = "NEGATIVE_TEXT";
        public static final String SUB_TITLE_TEXT = "SUB_TITLE_TEXT";
        public static final String NOTIFICATION_DAYS = "NOTIFICATION_DAYS";
        public static final String PASSWORD = "PASSWORD";
        public static final String LAST_LOGIN_USER = "LAST_LOGIN_USER";
        public static final String EMAIL_RECIPIENTS = "EMAIL_RECIPIENTS";
        public static final String CREDIT_CARD = "CREDIT_CARD";
        public static final String POSITION = "POSITION";
        public static final String FULL_NAME = "FULL_NAME";
        public static final String PRODUCT_ID = "PRODUCT_ID";
        public static final String PRODUCT_TITLE = "PRODUCT_TITLE";
        public static final String EMAIL_ACTION = "EMAIL_ACTION";
        public static final String CALL_API_TYPE = "CALL_API_TYPE";
        public static final String API_CONFIGURATION = "API_CONFIGURATION";
        public static final String API_PRODUCTS = "API_PRODUCTS";
        public static final String PRODUCT_DETAILS = "PRODUCT_DETAILS";
        public static final String REPORT_DETAILS = "REPORT_DETAILS";
        public static final String REPORT_NAME = "REPORT_NAME";
        public static final String HISTORY_ITEM = "HISTORY_ITEM";
        public static final String FROM_MENU_SCREEN = "FROM_MENU_SCREEN";
        public static final String FROM_ADD_RECIPIENTS = "FROM_ADD_RECIPIENTS";
        public static final String FROM_TO = "FROM_TO";
        public static final String CREDIT_REPORT = "CREDIT_REPORT";
        public static final String EDIT_FIELD_NAME = "EDIT_FIELD_NAME";
        public static final String EDIT_FIELD_DATA = "EDIT_FIELD_DATA";
        public static final String USER_DATA = "USER_DATA";
        public static final String EDIT_NATIONALITY = "EDIT_NATIONALITY";
        public static final String HIDE_I_AGREE_BUTTON = "HIDE_I_AGREE_BUTTON";
        public static final String IS_CARD_DEFAULT = "IS_CARD_DEFAULT";
        public static final String IS_FROM_TOUCH_ID = "IS_FROM_TOUCH_ID";
        public static final String SECURITY_QUESTION = "SECURITY_QUESTION";
        public static final String ADD_CARD_RESPONSE = "ADD_CARD_RESPONSE";
        public static final String BUY_CREDIT_RESPONSE = "BUY_CREDIT_RESPONSE";
        public static final String SCREEN_FROM = "SCREEN_FROM";
        public static final String SCANNED = "SCANNED";
        public static final String SHOW_SCORE_LOADING = "SHOW_SCORE_LOADING";
        public static final String SELECTED_CARD = "SELECTED_CARD";
        public static final String BUNDLE_DB_USER = "BUNDLE_DB_USER";
        public static final String DESC_CLICK = "DESC_CLICK";
        public static final String CHECKOUT_URL = "CHECKOUT_URL";
        public static final String CHECKOUT_ID = "CHECKOUT_ID";
        public static final String IS_EDIRHAM_DEFAULT = "IS_EDIRHAM_DEFAULT";
        public static final String SELECTED_EDIRHAM = "SELECTED_EDIRHAM";
        public static final String ADCB_WEBVIEW_HTML = "ADCB_WEBVIEW_HTML";
        public static final String ADCB_WEBVIEW_HTML_CONTENT = "ADCB_WEBVIEW_HTML_CONTENT";
        public static final String IS_DEFAULT_SELECTED = "IS_DEFAULT_SELECTED";
        public static final String SELECTED_NI_CARD = "SELECTED_NI_CARD";
        public static final String UAE_PASS_REQUEST = "UAE_PASS_REQUEST";
        public static final String TC_FOR_DOB = "TC_FOR_DOB";
        public static final String AUTHENTICATION_CODE = "AUTHENTICATION_CODE";
    }

    public static final class EditField {
        public static final String NAME = "NAME";
        public static final String EMIRATE_ID = "EMIRATE_ID";
        public static final String MOBILE_NO = "MOBILE_NO";
        public static final String EMAIL_ID = "EMAIL_ID";
        public static final String PASSPORT_NO = "PASSPORT_NO";
    }

    public static final class ActivityIntentCode {
        public static final int FOR_EMIRATE_SCAN = 4;
        public static final int FOR_TRADE_SCAN = 5;
        public static final int INTERNET_CONNECTION = 26;
        public static final int FOR_FORGOT_PASSWORD = 36;
        public static final int FOR_CONTACT_DETAILS = 37;
        public static final int FOR_SECURITY_ROUND_TWO = 38;
        public static final int CREDIT_REPORT_SELECTED = 39;
        public static final int CREDIT_SCORE_SELECTED = 40;
        public static final int CREDIT_SCORE_WITH_REPORT_SELECTED = 41;
        public static final int VIEW_ALL_RECIPIENTS_REQUEST = 42;
        public static final int ADD_EMAIL = 43;
        public static final int EDIT_EMAIL = 44;
        public static final int PAYMENT_SUCCESSFULLY_SAVED = 45;
        public static final int PAYMENT_STATUS_FAILED = 46;
        public static final int VIEW_ALL_CARDS_REQUEST = 47;
        public static final int FROM_TO_ADDCARD_SCREEN = 48;
        public static final int ADDCARD_SCREEN_TO_BACK_BUTTON = 49;
        public static final int SAVE_CARD_SUCCESSFULLY = 54;
        public static final int UPDATE_DELETE_CARD = 55;
        public static final int MAKE_DEFAULT_CARD = 56;
        public static final int PAYMENT_METHOD_UPDATED = 57;
        public static final int FOR_CHANGE_PASSWORD = 58;
        public static final int FOR_CHANGE_LANG_SETTINGS = 59;
        public static final int CHANGE_TOUCH_ENABLE = 60;
        public static final int CHANGE_TOUCH_DESABLE = 61;
        public static final int TOUCH_ID = 62;
        public static final int FOR_NO_SCORE_FAQ = 63;
        public static final int FOR_NO_SCORE_CONTACT_US = 64;
        public static final int FOR_LOGOUT = 65;
        public static final int API_FAILURE_SUCCESS = 66;
        public static int MY_SCAN_REQUEST_CODE = 50;
        public static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 51;
        public static int SELECT_IMAGE_GALLERY_REQUEST_CODE = 52;
        public static int SELECT_IMAGE_FILE_REQUEST_CODE = 53;
        public static int ADD_RECIPIENTS_CODE = 67;
        public static int CHANGE_CARD_CODE = 68;
        public static int EDIRHAM_REQUEST = 69;
        public static final int MAKE_EDIRHAM_DEFAULT = 70;
        public static int ADCB_CARD_REQUEST = 71;
        public static final int AUTHORIZATION_FAILED = 72;
        public static int UAE_PASS_REQUEST_CODE = 73;
    }

    public static final class QuestionTypes {
        public static final String A_ONLY = "A";
        public static final String A_PLUS = "A+";
        public static final String A_MINUS = "A-";
        public static final String B_ONLY = "B";
        public static final String B_PLUS = "B+";
        public static final String B_MINUS = "B-";
    }

    public static final class ScoreLimits {
        public static final int VERY_LOW = 540;
        public static final int LOW = 650;
        public static final int MEDIUM = 710;
        public static final int HIGH = 745;
        public static final int VERY_HIGH = 900;
    }

    public static final class ProductIds {
        public static final String REPORT_ID_PRODUCT_NUMBER = "ITM-0199";
//        public static final String SCORE_ID_PRODUCT_NUMBER = "ITM-0201";
//        public static final String SCORE_WITH_REPORT_ID_PRODUCT_NUMBER = "ITM-0200";
//        public static final String D2C_PRODUCT_NUMBER = "ITM-0241";

        public static final String SCORE_ID_PRODUCT_NUMBER = "ITM-0306";
        public static final String SCORE_WITH_REPORT_ID_PRODUCT_NUMBER = "ITM-0304";
        public static final String D2C_PRODUCT_NUMBER = "ITM-0310";

        public static String REPORT_ID = "";
        public static String SCORE_ID = "";
        public static String SCORE_WITH_REPORT_ID = "";
        public static String D2C_PRODUCT_ID = "";
        public static ProductsItem REPORT_PRODUCT;
        public static ProductsItem SCORE_PRODUCT;
        public static ProductsItem SCORE_WITH_REPORT_PRODUCT;
        public static ArrayList<ProductsItem> isDisplayFalseProduct = new ArrayList<>();
    }

    public static final class DateFormats {
        public static final String DD_MM_YYYY_DASH = "dd-MM-yyyy";
        public static final String DD_MM_YYYY_SLASH = "dd/MM/yyyy";
        public static final String YYYY_MM_DD_DASH = "yyyy-MM-dd";
        public static final String API_DATE_FORMATE = "dd/MM/YYYY";
        public static final String API_DATE_FORMAT_LOWER_YEAR = "dd/MM/yyyy";
        public static final String DD_MMM_YYYY_SPACE = "dd MMM yyyy";
        public static final String DD_MM_YY = "dd/MM/yyyy";
        public static final String MMM = "MMM";
        public static final String MMM_dd_yyyy = "MMM d yyyy";
        public static final String DEFAULT_API_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        public static final String DISPLAY_FORMAT = "MMM dd, yyyy";
        public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        public static final String DATE_MONTH_YEAR_FORMAT = "dd-MMM-yyyy";
        public static final String DD_MM_YY_DOB = "dd/MM/yyyy";
    }

    public static final class ApkBuildType {
        public static final String UAT = "uat";
        public static final String DEV = "dev";
        public static final String PROD = "prod";
    }

    public static final class ConversionApi {
        public static final String ACCESS_TOKEN = "EAAN32zWDKG0BALC15ZB23G9pGU1VXnhEpgZBkERamQtCZCppJeHly3fHAIoZA2m6ZBBcVT5jVtNH8GgWM7ojE1aXy6d2MFbf0cuMNCiKZBbHJbjX9oTrZB26wYlKM9FoyMyWs5jkc1RB5ji4x3tTWyHin0bZC7V3Tw6YMRaLFTZAU7T10xREcsRGm";
        public static final String PIXEL_ID = "523106775142321";
    }

    public static final class PaymentMethods {
        public static final String E_DIRHAM = "E-Dirham";
        public static final String PAYMENT_GW = "Payment GW";
        public static final String EDIRHAMS = "edirhams";
    }

    public static final class CardTypes {
        public static final String MASTER_CARD = "MASTERCARD";
        public static final String VISA = "VISA";
        public static final String CIRRUS = "CIRRUS";
    }

    public static final class AppUnderMaintenance {
        public static final String Title = "Title";
        public static final String DES = "DES";
        public static final String MAINTENANCE_DESCRIPTION_MOBILE = "MAINTENANCE.DESCRIPTION.MOBILE";
        public static final String MAINTENANCE_TITLE_MOBILE = "MAINTENANCE.TITLE.MOBILE";
    }

    public static final class AddCardOperationType {
        public static final String PAYMENT_NEW_CARD = "paymentnewcard";
        public static final String PAYMENT_OLD_CARD = "paymentoldcard";
        public static final String PAYMENT_CONFIG_NEW_CARD = "confignewcard";
        public static final String PAYMENT_CONFIG_OLD_CARD_SETTINGS = "configoldcard";
        public static final String PAYMENT_CONFIG_OLD_CARD_PURCHASE = "paymentnewcard6";
    }

    public static final class Events {
        public static final String APP_TUTORIAL_COMPLETION = "completedTutorial";
        public static final String REGISTERED_WITHOUT_SCAN = "registered_without_scan";
        public static final String REGISTERED_WITH_SCAN = "registered_with_scan";

        public static final String CANCEL_FROM_RECIPIENT = "cancel_from_recipient_screen";
        public static final String CANCEL_FROM_CHECKOUT = "cancel_from_checkout_screen";
        public static final String CANCEL_FROM_ADD_CARD = "cancel_from_add_card_screen";
        public static final String CANCEL_FROM_PAYMENT = "cancel_from_payment_screen";
        public static final String CANCEL_FROM_CARD_VERIFICATION = "cancel_from_card_verification_screen";

        public static final String CANCEL_FROM_FORGOT_PWD = "cancel_from_forgot_password_screen";
        public static final String CANCEL_FROM_OTP = "cancel_otp_screen";
        public static final String CANCEL_FROM_CREATE_PASSWORD = "cancel_from_create_password_screen";
        public static final String WRONG_PASSWORD_FROM_LOGIN = "wrong_password_from_login";
        public static final String CANCEL_FROM_CHANGE_PASSWORD = "cancel_from_change_password_screen";
        public static final String CANCEL_FROM_SECURITY_ONE = "cancel_from_security_round_one_screen";
        public static final String CANCEL_FROM_SECURITY_TWO = "cancel_from_security_round_two_screen";
        public static final String CONTACT_US_GUEST = "contact_us_guest";
        public static final String CONTACT_US_REGISTERED_USER = "contact_us_registered_user";

        public static final String UPDATED_FULLNAME = "updated_fullname_from_scan";
        public static final String UPDATED_EMIRATE_ID = "updated_emirate_id_from_scan";
        public static final String UPDATED_GENDER = "updated_gender_from_scan";
        public static final String UPDATED_DOB = "updated_date_of_birth_from_scan";
        public static final String UPDATED_NATIONALITY = "updated_nationality_from_scan";

        public static final String CARD_SCANNED = "add_card_detail_with_scan";
        public static final String CARD_NOT_SCANNED = "add_card_detail_without_scan";

        public static final String VISITED_PROFILE = "visited_profile";
        public static final String VISITED_ABOUT_AECB = "visited_about_aecb";
        public static final String VISITED_T_AND_C = "visited_terms_and_conditions";
        public static final String SWITCHED_LANGUAGE = "switched_language";
        public static final String ENABLE_DISABLE_TOUCHID = "enable_disable_touch_id";
        public static final String VISITED_MANAGE_PAYMENT = "visited_manage_payment";
        public static final String VISITED_CHANGE_PASSWORD = "visited_change_password";
        public static final String USER_LOGOUT = "user_logged_out";

        public static final String ADDED_RECIPIENTS = "added_recipients";
        public static final String ADDED_REMOVED_PRODUCTS = "added_removed_product";
        public static final String ADDED_PAYMENT_DETAILS = "added_payment_details";

        public static final String SELECTED_EIDRHAM_FROM_CHECKOUT = "selected_edirham_from_checkout";
        public static final String ADDED_CARD_FROM_CHECKOUT = "added_new_card_from_checkout";

        public static final String UPDATED_CARD_NUMBER = "updated_card_number_from_scan";
        public static final String UPDATED_CARD_FULLNAME = "updated_card_fullname_from_scan";
        public static final String UPDATED_CARD_EXPIRY = "updated_expiry_from_scan";

        public static final String EDITED_PASSPORT_FROM_SETTING = "edited_passport_from_setting";
        public static final String EDITED_NATIONALITY_FROM_SETTING = "edited_nationality_from_setting";
        public static final String EDITED_MOBILE_FROM_SETTING = "edited_mobile_from_setting";
        public static final String EDITED_EMAIL_FROM_SETTING = "edited_email_from_setting";

        public static final String EDIT_PASSPORT_SAVED = "edit_passport_saved";
        public static final String EDIT_PASSPORT_CANCELLED = "edit_passport_cancelled";
        public static final String EDITED_NATIONALITY_SAVED = "edit_nationality_saved";

        public static final String VISITED_SCORE_HISTORY = "visited_score_history";
        public static final String VISITED_HELP_SUPPORT = "visited_help_and_support";

        public static final String MAKE_PAYMENT = "MakePayment";
        public static final String MAKE_PAYMENT_ANDROID = "MakePaymentAndroid";
        public static final String REGISTRATION = "Registration";
        public static final String APP_UPDATE = "app_update";

    }

    public static final class ValidationRegex {
        public static final String NAME = "^[A-Z a-z]+$";
        public static final String CONTACT_NUMBER = "^[0-9 +\\- ]*$";
        public static final String PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[*([@#$%^&+=])]).{8,20})";
        public static final String ALPHA = "^[a-zA-Z ]*$";
        public static final Pattern CUSTOM_EMAIL_REGEX_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,64}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,255}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        public static final String ALPHANUMERIC = "^[a-zA-Z0-9 ]*$";
    }
}