package com.aecb.base.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatDelegate;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.BuildConfig;
import com.aecb.R;
import com.aecb.base.network.ConnectivityUtils;
import com.aecb.data.api.models.commonmessageresponse.MessageItem;
import com.aecb.data.api.models.commonmessageresponse.MessageResponse;
import com.aecb.listeners.DialogButtonClickListener;
import com.aecb.ui.exit.view.ExitFragment;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.maintanance.view.MaintenanceActivity;
import com.aecb.ui.splash.view.SplashActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.util.LocaleHelper;
import com.aecb.util.Utilities;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.tumblr.remember.Remember;

import java.util.List;

import dagger.android.AndroidInjection;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import timber.log.Timber;

import static com.aecb.AppConstants.ActivityIntentCode.API_FAILURE_SUCCESS;
import static com.aecb.AppConstants.ApkBuildType.DEV;
import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.IntentKey.DESC_CLICK;
import static com.aecb.data.preference.PrefHelperImpl.KEY_CURRENT_LANGUAGE;
import static com.aecb.util.ToastUtil.longToast;
import static com.aecb.util.ToastUtil.toast;
import static com.aecb.util.ValidationUtil.isNullOrEmpty;

public abstract class BaseActivity<V extends BaseView, P extends BasePresenter<V>> extends MvpActivity<V, P>
        implements BaseView {

    private Dialog progressDialog;
    private Handler handler = new Handler();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)));
    }

    public void showLoading(String message) {
        progressDialog = DialogUtils.showLoading(this, message);
    }

    public void hideLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) progressDialog.cancel();
            progressDialog = null;
        }
    }

    public void clientError(String errorMessage) {
        if (errorMessage != null)
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }


    @SuppressLint("NewApi")
    public void connectionError(String errorMessage) {
        Timber.e(errorMessage);
        if (!this.isNetworkConnected()) {
            showApiFailureError(getString(R.string.networkError), "", "");
        } else {
            showApiFailureError(getString(R.string.connectionError), "", "");
        }

    }

    public void gotoAppUnderMaintain() {
        moveActivity(this, MaintenanceActivity.class, true, true);
    }


    public boolean isNetworkConnected() {
        return ConnectivityUtils.isConnectedToInternet(this);
    }

    public void onTimeout() {
        longToast(this, R.string.timeoutError);
    }

    public void serverError(String errorMessage) {
        Timber.e(errorMessage);
        longToast(this, R.string.serverError);
    }

    public void unauthenticated() {
        longToast(this, R.string.unauthenticatedError);
        this.finishAffinity();
        moveActivity(this, SplashActivity.class, true, true);
    }

    public void unexpectedError(String errorMessage) {
        if (!(this instanceof SplashActivity))
            toast(this, errorMessage);
    }

    public void unexpectedError(RuntimeException exception) {
        if (exception.getMessage() != null) {
            toast(this, exception.getMessage());
        }
    }

    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        if (!BuildConfig.BUILD_TYPE.equals(DEV)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        adjustFontScale(getResources().getConfiguration());
        setStatusBarColor();
        setScreenDirection();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (this.getLayoutRes() != 0) {
            this.setContentView(this.getLayoutRes());
        }
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            Utilities.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19 && getWindow() != null && getWindow().getDecorView() != null) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Utilities.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            if (getWindow() != null) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

    }

    public void setScreenDirection() {
        if (getWindow() != null) {
            String mAppLang = Remember.getString(KEY_CURRENT_LANGUAGE, AppConstants.AppLanguage.ISO_CODE_ENG);
            if (!isNullOrEmpty(mAppLang)) {
                if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                    getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                } else if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                    getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                }
            } else {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.initToolbar();
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void initToolbar();

    public void moveActivity(Context context, Class<?> c, boolean finish, boolean clearStack, Bundle bundle) {
        Intent intent = new Intent(context, c);
        if (bundle != null) intent.putExtras(bundle);
        if (clearStack)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (finish) ((Activity) context).finish();
    }

    public void moveActivity(Context context, Class<?> c, int flags, Bundle bundle) {
        Intent intent = new Intent(context, c);
        if (bundle != null)
            intent.putExtras(bundle);
        intent.setFlags(flags);
        context.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void moveActivity(Context context, Class<?> c, boolean finish, int clearStack, Bundle bundle) {
        Intent intent = new Intent(context, c);
        if (bundle != null) intent.putExtras(bundle);
        intent.setFlags(clearStack);
        context.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (finish) ((Activity) context).finish();
    }

    public void moveActivity(Context context, Class<?> c, boolean finish, boolean clearStack, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, c);
        if (bundle != null) intent.putExtras(bundle);
        if (clearStack)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (finish) ((Activity) context).finish();
    }

    public void moveActivity(Context context, Class<?> c, boolean finish, boolean clearStack) {
        Intent intent = new Intent(context, c);
        if (clearStack)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (finish) ((Activity) context).finish();
    }

    public void moveActivity(Context context, Class<?> c, boolean finish, boolean clearStack, Intent intent) {
        if (clearStack)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (finish) ((Activity) context).finish();
    }

    public void showInternetNotAvailableDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY, AppConstants.ActivityIntentCode.INTERNET_CONNECTION);
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.networkError));
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.txt_ok));
    }

    @Override
    public void showErrorMsgFromApi(String title, String description, String yesButton, String noButton,
                                    DialogButtonClickListener dialogButtonClickListener) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dailog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_main_bg);
        dialog.setCancelable(false);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvDesc = dialog.findViewById(R.id.tvDesc);
        TextView tvYes = dialog.findViewById(R.id.tvYes);
        TextView tvNo = dialog.findViewById(R.id.tvNo);
        tvTitle.setText(title);
        tvDesc.setText(description);
        tvYes.setText(yesButton);
        tvNo.setText(noButton);
        if (noButton == null) {
            tvNo.setVisibility(View.GONE);
            if (dialogButtonClickListener == null) {
                tvYes.setOnClickListener(v -> dialog.dismiss());
            }
        }
        if (title == null) tvTitle.setVisibility(View.GONE);
        if (dialogButtonClickListener != null) {
            tvYes.setOnClickListener(v -> {
                dialog.dismiss();
                dialogButtonClickListener.onPositiveButtonClicked();
            });
            tvNo.setOnClickListener(v -> {
                dialog.dismiss();
                dialogButtonClickListener.onNegativeButtonClicked();
            });
        }
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void sessionTokenExpired() {
        showErrorMsgFromApi(getString(R.string.session_expired), getString(R.string.please_login_again),
                getString(R.string.ok), null, new DialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        moveActivity(BaseActivity.this, LoginActivity.class, true, true);
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                    }
                });
    }

    @Override
    public void localValidationError(String title, String desc) {
        showErrorMsgFromApi(title, desc,
                getString(R.string.ok), null, null);
    }


    public void showApiFailureError(String message, String status, String useCase) {
        String jsonResponse = Utilities.loadJSONFromAsset();
        if (!isNullOrEmpty(status)) {
            MessageResponse messageResponse = new Gson().fromJson(jsonResponse, MessageResponse.class);
            MessageItem messageItem = getMessageItem(messageResponse.getMessage(), status, useCase);
            if (messageItem != null) {
                if (App.getAppComponent().getDataManager().getCurrentLanguage().equals(ISO_CODE_ARABIC)) {
                    showErrorDialog(messageItem.getMessageAr(), status);
                } else {
                    showErrorDialog(messageItem.getMessageEn(), status);
                }
            } else {
                showErrorDialog(message, status);
            }
        } else {
            showErrorDialog(message, status);
        }
    }


    public MessageItem getMessageItem(List<MessageItem> message, String status, String useCase) {
        final MessageItem[] messageItem = {null};
        for (MessageItem item : message) {
            try {
                if (!isNullOrEmpty(status) && status.equals(item.getStatus()) && !isNullOrEmpty(useCase) && useCase.equals(item.getUseCase())) {
                    messageItem[0] = item;
                } else if (status.equals("ss_09") && item.getStatus().equals("ss_09")) {
                    messageItem[0] = item;
                }
            } catch (NumberFormatException efe) {
                Timber.d("Exception : " + efe.toString());
            }
        }

        if (messageItem[0] != null)
            return messageItem[0];
        else return null;

    }

    @Override
    public void showApiSuccessMessage(String message, String status, String useCase) {
        String jsonResponse = Utilities.loadJSONFromAsset();
        if (!isNullOrEmpty(status)) {
            MessageResponse messageResponse = new Gson().fromJson(jsonResponse, MessageResponse.class);
            MessageItem messageItem = getMessageItem(messageResponse.getMessage(), status, useCase);
            if (messageItem != null) {
                if (App.getAppComponent().getDataManager().getCurrentLanguage().equals(ISO_CODE_ARABIC)) {
                    showApiSuccessMessage(messageItem.getMessageAr());
                } else {
                    showApiSuccessMessage(messageItem.getMessageEn());
                }
            } else {
                showApiSuccessMessage(message);
            }
        } else {
            showApiSuccessMessage(message);
        }
    }

    public void showApiSuccessMessage(String message) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY,
                API_FAILURE_SUCCESS);
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.success));
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, message);
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.txt_ok));
        BlurDialogBaseFragment paymentStatusDialog = ExitFragment.newInstance(bundle);
        paymentStatusDialog.setCancelable(false);
        paymentStatusDialog.show(getSupportFragmentManager(), paymentStatusDialog.getTag());
        handler.postDelayed(paymentStatusDialog::dismiss, 3000);
    }

    public void showErrorDialog(String message, String status) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY,
                API_FAILURE_SUCCESS);
        if (status.equalsIgnoreCase("E01-0006") || status.equalsIgnoreCase("202")) {
            bundle.putBoolean(DESC_CLICK, true);
        }
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.error));
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, message);
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.txt_ok));
        BlurDialogBaseFragment paymentStatusDialog = ExitFragment.newInstance(bundle);
        paymentStatusDialog.setCancelable(false);
        paymentStatusDialog.show(getSupportFragmentManager(), paymentStatusDialog.getTag());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void adjustFontScale(Configuration configuration) {
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

}