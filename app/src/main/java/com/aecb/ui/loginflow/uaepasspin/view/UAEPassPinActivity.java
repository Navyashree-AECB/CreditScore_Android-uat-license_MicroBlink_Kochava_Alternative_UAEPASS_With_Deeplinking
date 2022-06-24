package com.aecb.ui.loginflow.uaepasspin.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.BuildConfig;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityUaepassPinBinding;
import com.aecb.listeners.DialogButtonClickListener;
import com.aecb.ui.loginflow.uaepasspin.presenter.UAEPassPinContract;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ENG;
import static com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil.REDIRECT_URL;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UAEPassPinActivity extends BaseActivity<UAEPassPinContract.View, UAEPassPinContract.Presenter>
        implements UAEPassPinContract.View {

    @Inject
    public UAEPassPinContract.Presenter mPresenter;

    ActivityUaepassPinBinding uaepassPinBinding;
    private String state = UaePassUtil.IS_PRODUCTION ? "ShNP22hyl1jUU2RGjTRkpg==" : "HnlHOJTkTb66Y5H";
    public String lang = ISO_CODE_ENG;
    private boolean dialogCancelled = false;
    private String mSuccessURLUAEPass = "";
    private String mFailureURLUAEPass = "";
    private WebView webView;
    private androidx.appcompat.app.AlertDialog webViewAlertDialog;
    private boolean isCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
    }

    private void enableBinding() {
        uaepassPinBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        uaepassPinBinding.btnBack.setOnClickListener(v -> onBackPressed());
        lang = mPresenter.getCurrentLanguage();
//         state = "ShNP22hyl1jUU2RGjTRkpg==";
//        state = "HnlHOJTkTb66Y5H";
        //state = UaePassUtil.generateRandomString(24);
        String url;
        if (UaePassUtil.appInstalledOrNot(this, UaePassUtil.UAE_PASS_PACKAGE_ID)) {
            url = UaePassUtil.UAEPASS_AUTHENTICATION_URL + "?redirect_uri=" + REDIRECT_URL + "&client_id=" + UaePassUtil.UAE_PASS_CLIENT_ID + "&response_type=" + UaePassUtil.RESPONSE_TYPE + "&state=" + state + "&scope=" + UaePassUtil.SCOPE + "&acr_values=" + UaePassUtil.ACR_VALUES_MOBILE + "&ui_locales=" + lang;
        } else {
            url = UaePassUtil.UAEPASS_AUTHENTICATION_URL + "?redirect_uri=" + REDIRECT_URL + "&client_id=" + UaePassUtil.UAE_PASS_CLIENT_ID + "&response_type=" + UaePassUtil.RESPONSE_TYPE + "&state=" + state + "&scope=" + UaePassUtil.SCOPE + "&acr_values=" + UaePassUtil.ACR_VALUES_WEB + "&ui_locales=" + lang;
        }
        Log.e("Error URL", url);

        login(url);
    }

    @NonNull
    @Override
    public UAEPassPinContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_uaepass_pin;
    }

    @Override
    public void initToolbar() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void login(String url) {
        CookieManager.getInstance().removeAllCookie();

        webViewAlertDialog = new androidx.appcompat.app.AlertDialog.Builder(this).create();
        webViewAlertDialog.setTitle(null);

        RelativeLayout dialogView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.uaepass_login, null);
        webViewAlertDialog.setView(dialogView);
        webViewAlertDialog.setCancelable(true);
        webView = dialogView.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(false);
        webView.clearCache(true);
        webView.clearHistory();
        webViewAlertDialog.setOnKeyListener((arg0, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialogCancelled = true;
                webViewAlertDialog.dismiss();
            }
            return true;
        });
        webViewAlertDialog.setOnDismissListener(dialog -> {
            webView.clearCache(true);
            webView.clearHistory();
            webView.loadUrl("about:blank");
            webView.onPause();
            webView.removeAllViews();
            webView.destroyDrawingCache();
            webView.pauseTimers();
            webView.destroy();
            webView = null;
            if (dialogCancelled) {
                dialogCancelled = false;
                showError();
            }
        });
        if (Build.VERSION.SDK_INT >= 26) {
            settings.setSafeBrowsingEnabled(false);
        }

        Log.e("Error URL----", url);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.e("Changed Error URL----", url);

                if (url.contains("uaepass://digitalid")) {
                    Timber.e("UAEPass Webview URL ==" + url);

                    if (!UaePassUtil.IS_PRODUCTION) {
                        url = url.replace("uaepass://", "uaepassstg://");
                    }

                    mSuccessURLUAEPass = UaePassUtil.getQueryParameterValue(url, "successurl");
                    mFailureURLUAEPass = UaePassUtil.getQueryParameterValue(url, "failureurl");

                    // success url
                    if (url.contains("successurl")) {
//                        url = UaePassUtil.replaceUriParameter(Uri.parse(url), "successurl", REDIRECT_URL).toString();
                          url = UaePassUtil.replaceUriParameter(Uri.parse(url), "successurl", BuildConfig.APPLICATION_ID).toString()+"://uaePassSuccess";
                    }

                    // failure url
                    if (url.contains("failureurl")) {
//                        url = UaePassUtil.replaceUriParameter(Uri.parse(url), "failureurl", REDIRECT_URL).toString();
                          url = UaePassUtil.replaceUriParameter(Uri.parse(url), "failureurl", BuildConfig.APPLICATION_ID).toString()+"://uaePassFail&closeondone=true";

                    }

                    if (url.contains("browserpackage")) {
                        url = UaePassUtil.replaceUriParameter(Uri.parse(url), "browserpackage", BuildConfig.APPLICATION_ID).toString();
                        url = url + "&closeondone=false";
                    }

                    String filename =  Uri.parse(url).toString();
                    int iend = filename.indexOf("?");
                    String subString = "";
                    if (iend != -1) {
                        subString= filename.substring(0 , iend);
                    }

                    System.out.println("==== " + Uri.parse(subString + "?successurl="+BuildConfig.APPLICATION_ID+"://uaePassSuccess&failureurl="+BuildConfig.APPLICATION_ID+"://uaePassFail&closeondone=true"));

                    Intent launchIntent = new Intent("android.intent.action.VIEW", Uri.parse(subString + "?successurl="+BuildConfig.APPLICATION_ID+"://uaePassSuccess&failureurl="+BuildConfig.APPLICATION_ID+"://uaePassFail&closeondone=true"));
                    //Intent launchIntent = new Intent("android.intent.action.VIEW", Uri.parse(subString + "?successurl=com.aecb.app://uaePassSuccess&failureurl=com.aecb.app://uaePassFail&closeondone=true"));
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PackageManager packageManager = getPackageManager();
                    if (launchIntent.resolveActivity(packageManager) != null) {
                        startActivity(launchIntent);
                        isCalled = true;
                    } else {
                        UaePassUtil.openUAEPassAppInPlayStore(UAEPassPinActivity.this);
                        webViewAlertDialog.dismiss();
                    }

                    return true;
                } else {
                    String redirectUrl = REDIRECT_URL;
                    if (url.startsWith(redirectUrl)) {
                        String code = UaePassUtil.getQueryParameterValue(url, "code");
                        String state_ = UaePassUtil.getQueryParameterValue(url, "state");
                        String error = UaePassUtil.getQueryParameterValue(url, "error");
                        if (error != null) {
                            webViewAlertDialog.dismiss();
                            if (error.contains("access_denied")) {
                                showError();
                                hideLoading();
                            } else {
                                showError();
                                hideLoading();
                            }
                            return false;
                        }

                        if (!state.equals(state_)) {
                            code = null;
                        }
                        if (code != null) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(AppConstants.IntentKey.AUTHENTICATION_CODE, code);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                            //callAccessTokenUaePass(code);
                        }
                        webViewAlertDialog.dismiss();
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                finish();
            }
        });
        EditText edit = (EditText) dialogView.findViewById(R.id.editText);
        edit.setFocusable(true);
        edit.requestFocus();
        webView.loadUrl(url);
        webViewAlertDialog.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        /*if (intent.getDataString().equals(REDIRECT_URL)) {
            Timber.e("Back to our app" + intent.getDataString());
            webView.loadUrl(mSuccessURLUAEPass);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        if(isCalled) {
            //if (intent.getDataString().equals(REDIRECT_URL)) {
            Timber.e("Back to our app" + intent.getDataString());
            webView.loadUrl(mSuccessURLUAEPass);
            // }
        }
    }

    private void showError() {
        showErrorMsgFromApi(getString(R.string.error), getString(R.string.user_cancelled_authentication),
                getString(R.string.ok), null, new DialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        finish();
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                    }
                });
    }
}
