package com.aecb.ui.adcbwebview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityAdcbCardWebviewBinding;
import com.aecb.ui.adcbwebview.presenter.AdcbWebviewContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import static com.aecb.AppConstants.ApiParameter.TERM_URL;
import static com.aecb.AppConstants.IntentKey.ADCB_WEBVIEW_HTML;
import static com.aecb.util.Utilities.showSSLDialog;

public class AdcbCardWebviewActivity extends BaseActivity<AdcbWebviewContract.View, AdcbWebviewContract.Presenter>
        implements AdcbWebviewContract.View {

    @Inject
    AdcbWebviewContract.Presenter mPresenter;
    ActivityAdcbCardWebviewBinding adcbCardWebviewBinding;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        enableBinding();
    }

    private void enableBinding() {
        adcbCardWebviewBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String adcbHtml = bundle.getString(ADCB_WEBVIEW_HTML);
            getUrlAndLoadUrl(adcbHtml);
        }
    }

    private void getUrlAndLoadUrl(String html) {
        adcbCardWebviewBinding.webViewAdcb.getSettings().setJavaScriptEnabled(true);
        adcbCardWebviewBinding.webViewAdcb.getSettings().setBuiltInZoomControls(true);
        adcbCardWebviewBinding.webViewAdcb.getSettings().setDisplayZoomControls(false);
        adcbCardWebviewBinding.webViewAdcb.setWebViewClient(new WvClient());
        adcbCardWebviewBinding.webViewAdcb.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    @NonNull
    @Override
    public AdcbWebviewContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_adcb_card_webview;
    }

    @Override
    public void initToolbar() {

    }

    private class WvClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
            showSSLDialog(mContext, handler, er);
            // Ignore SSL certificate errors
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            adcbCardWebviewBinding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            if (url.toLowerCase().contains(TERM_URL)) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            adcbCardWebviewBinding.progressBar.setVisibility(View.GONE);
            // if (url.contains("https://onlineuat.aecb.gov.ae:8000/api/v1/payment/mobileEdirham")) {
            //Intent intent = new Intent();
               /* setResult(Activity.RESULT_OK);
                finish();*/
               /* adcbCardWebviewBinding.webViewAdcb.evaluateJavascript(
                        "(function() {return document.getElementsByTagName('html')[0].outerHTML;})();",
                        html -> {
                            String response = String.valueOf(Html.fromHtml(String.valueOf(removeUTFCharacters(html))));
                            String semiFinalResponse = "";
                            String finalResponse = "";
                            try {
                                semiFinalResponse = response.replaceAll("(\\\\n|\\\\r)", "").replaceAll("\\\\", "");
                                finalResponse = semiFinalResponse.substring(1, semiFinalResponse.length() - 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            intent.putExtra(ADCB_WEBVIEW_HTML_CONTENT, finalResponse);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        });*/
            // }
        }
    }

    public static StringBuffer removeUTFCharacters(String data) {
        Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        Matcher m = p.matcher(data);
        StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        m.appendTail(buf);
        return buf;
    }
}