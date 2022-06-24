package com.aecb.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

import static com.aecb.util.ToastUtil.longToast;
import static com.aecb.util.ToastUtil.toast;

public class ValidationUtil {

    public static void showToast(Context context, String message) {
        if (!TextUtils.isEmpty(message)) {
            toast(context, message);
        }
    }

    public static void showLongToast(Context context, String message) {
        if (!TextUtils.isEmpty(message)) {
            longToast(context, message);
        }
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static void setErrorMessage(TextView txtError, String message, boolean chkVisiable) {
        txtError.setText(message);
        if (chkVisiable) {
            txtError.setVisibility(View.VISIBLE);
        } else {
            txtError.setVisibility(View.GONE);
        }
    }

    public static boolean isValidAlpha(String text, String regex) {
        return Pattern.matches(regex, text);
    }

    public static boolean checkMinTextValidation(String text, int textLength) {
        if (isNullOrEmpty(text)) {
            return false;
        } else return text.trim().length() >= textLength;
    }

    public static boolean isValidPassword(String password) {
        boolean isValid = false;
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&])[A-Za-z\\\\d@$!%*?&]{8,}$";
        if (password.length() < 8) {
            return isValid;
        } else if (password.length() > 20) {
            return isValid;
        } else if (!(Pattern.matches(regex, password))) {
            return isValid;
        } else {
            isValid = true;
            return isValid;
        }
    }

}