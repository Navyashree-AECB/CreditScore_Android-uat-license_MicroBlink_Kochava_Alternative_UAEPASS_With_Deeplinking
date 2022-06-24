package com.aecb.base.mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.aecb.R;

public class DialogUtils {

    public static ProgressDialog showLoading(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.CustomAlertDialogStyleParentTheme);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(message)) {
            progressDialog.setMessage(message);
        }
        progressDialog.show();
        return progressDialog;
    }

}