package com.aecb.ui.termsandconditions.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.FragmentTermsAndConditionsBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.purchasejourney.addcard.view.AddCardActivity;
import com.aecb.ui.registerflow.personaldetails.view.PersonalDetailsActivity;
import com.aecb.ui.scanemirate.view.ScanEmirateIdActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.ui.termsandconditions.presenter.TermsConditionContract;
import com.aecb.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.IntentKey.BUNDLE_DB_USER;
import static com.aecb.AppConstants.IntentKey.BUNDLE_USER_DETAILS;
import static com.aecb.AppConstants.IntentKey.HIDE_I_AGREE_BUTTON;
import static com.aecb.AppConstants.IntentKey.TC_FOR_DOB;
import static com.aecb.AppConstants.IntentKey.TC_VERSION;
import static com.aecb.AppConstants.IntentKey.UPDATE_TC_VERSION;
import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;
import static com.aecb.AppConstants.WEBVIEW_ENCODING;
import static com.aecb.AppConstants.WEBVIEW_MIME_TYPE;
import static com.aecb.AppConstants.arabicToDecimal;
import static com.aecb.AppConstants.doubleDecimalValue;
import static com.aecb.util.Utilities.showSSLDialog;

public class TermsAndConditionsFragment extends BlurDialogBaseFragment<TermsConditionContract.View,
        TermsConditionContract.Presenter> implements TermsConditionContract.View, View.OnClickListener {

    @Inject
    public TermsConditionContract.Presenter mPresenter;
    GetUserDetailResponse getUserDetailResponse;
    DBUserTC dbUserTC;
    private Context mContext;
    private String userName, tcFor;
    private int tcVersion;
    private int fromActivity;
    private boolean hideAgree = false;
    private boolean tcForDob = false;
    private FragmentTermsAndConditionsBinding termsConditionBinding;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            };

    public static TermsAndConditionsFragment newInstance(Bundle bundle) {
        TermsAndConditionsFragment fragment = new TermsAndConditionsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    public TermsConditionContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    public int getLayoutRes() {
        return R.layout.fragment_terms_and_conditions;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fromActivity = bundle.getInt(AppConstants.IntentKey.FROM_ACTIVITY);
            tcFor = bundle.getString(AppConstants.IntentKey.FROM_ACTIVITY);
            hideAgree = bundle.getBoolean(HIDE_I_AGREE_BUTTON);
            tcForDob = bundle.getBoolean(TC_FOR_DOB);
            if (bundle.containsKey(AppConstants.IntentKey.USER_NAME)) {
                userName = bundle.getString(AppConstants.IntentKey.USER_NAME, "");
            }
            if (bundle.containsKey(BUNDLE_USER_DETAILS)) {
                getUserDetailResponse = new Gson().fromJson(bundle.getString(BUNDLE_USER_DETAILS), GetUserDetailResponse.class);
            }
            if (bundle.containsKey(BUNDLE_DB_USER)) {
                dbUserTC = new Gson().fromJson(bundle.getString(BUNDLE_DB_USER), DBUserTC.class);
            }
            if (bundle.containsKey(AppConstants.IntentKey.OPEN_TC_FOR)) {
                tcFor = bundle.getString(AppConstants.IntentKey.OPEN_TC_FOR, "");
                if (tcFor.equals(UPDATE_TC_VERSION)) {
                    tcVersion = bundle.getInt(TC_VERSION, 0);
                }
            }
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        termsConditionBinding = DataBindingUtil.bind(contentView);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        ScreenUtils screenUtils = new ScreenUtils(getActivity());
        View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        bottomSheet.getLayoutParams().height = screenUtils.getHeight() - 150;
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        termsConditionBinding.btnIAgree.setOnClickListener(this);
        if (hideAgree) {
            termsConditionBinding.btnIAgree.setVisibility(View.GONE);
        }
        contentView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (termsConditionBinding.progressBar.getVisibility() == View.VISIBLE) {
                        termsConditionBinding.progressBar.setVisibility(View.GONE);
                    }
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getTermsConditionUrl();
        mContext = this.getActivity();
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnIAgree) {
            if (getContext() != null && (getContext() instanceof LoginActivity) /*|| getContext() instanceof StartupActivity*/) {
                UpdateUserProfileRequest updateUserProfileRequest = new UpdateUserProfileRequest();
                updateUserProfileRequest.setUpdateType(1);
                updateUserProfileRequest.setTcVersion(Float.parseFloat(arabicToDecimal(doubleDecimalValue(1, mPresenter.getTCVersion()))));
                updateUserProfileRequest.setNewsLetterSubscription(true);
                updateUserProfileRequest.setChannel(2);
                try {
                    String tcVersionString = arabicToDecimal(doubleDecimalValue(1, mPresenter.getTCVersion()));
                    String result = tcVersionString.substring(0, tcVersionString.indexOf("."));
                    dbUserTC.setTcVersion(Integer.parseInt(result));
                } catch (Exception e) {
                    Timber.e(e.getMessage());
                }
                if (getUserDetailResponse.getPreferredPaymentMethod() != null) {
                    dbUserTC.setPreferredPaymentMethod(getUserDetailResponse.getPreferredPaymentMethod());
                } else {
                    dbUserTC.setPreferredPaymentMethod(PAYMENT_GW);
                }
                if (getContext() instanceof LoginActivity) {
                    if (tcForDob) {
                        ((LoginActivity) getContext()).updateUserDetailForUaePass(updateUserProfileRequest, tcFor, dbUserTC, getUserDetailResponse);
                    } else {
                        ((LoginActivity) getContext()).mPresenter.updateUserDetail(updateUserProfileRequest, tcFor, dbUserTC, getUserDetailResponse);
                    }
                }
            }

            if (getContext() != null && (getContext() instanceof ScanEmirateIdActivity) /*|| getContext() instanceof StartupActivity*/) {
                UpdateUserProfileRequest updateUserProfileRequest = new UpdateUserProfileRequest();
                updateUserProfileRequest.setUpdateType(1);
                updateUserProfileRequest.setTcVersion(Float.parseFloat(arabicToDecimal(doubleDecimalValue(1, mPresenter.getTCVersion()))));
                updateUserProfileRequest.setNewsLetterSubscription(true);
                updateUserProfileRequest.setChannel(2);
                try {
                    String tcVersionString = arabicToDecimal(doubleDecimalValue(1, mPresenter.getTCVersion()));
                    String result = tcVersionString.substring(0, tcVersionString.indexOf("."));
                    dbUserTC.setTcVersion(Integer.parseInt(result));
                } catch (Exception e) {
                    Timber.e(e.getMessage());
                }
                if (getUserDetailResponse.getPreferredPaymentMethod() != null) {
                    dbUserTC.setPreferredPaymentMethod(getUserDetailResponse.getPreferredPaymentMethod());
                } else {
                    dbUserTC.setPreferredPaymentMethod(PAYMENT_GW);
                }
                if (getContext() instanceof ScanEmirateIdActivity) {
                    if (tcForDob) {
                        ((ScanEmirateIdActivity) getContext()).updateUserDetailForUaePass(updateUserProfileRequest, tcFor, dbUserTC, getUserDetailResponse);
                    } else {
                        ((ScanEmirateIdActivity) getContext()).mPresenter.updateUserDetail(updateUserProfileRequest, tcFor, dbUserTC, getUserDetailResponse);
                    }
                }
            }

            if (getContext() != null && getContext() instanceof AddCardActivity) {
                dismiss();
                ((AddCardActivity) getContext()).selectTermsConditions(true);
            }
            if (getContext() != null && getContext() instanceof StartupActivity) {
                UpdateUserProfileRequest updateUserProfileRequest = new UpdateUserProfileRequest();
                updateUserProfileRequest.setUpdateType(1);
                updateUserProfileRequest.setTcVersion(Float.parseFloat(arabicToDecimal(doubleDecimalValue(1, mPresenter.getTCVersion()))));
                updateUserProfileRequest.setNewsLetterSubscription(true);
                updateUserProfileRequest.setChannel(2);
                try {
                    String tcVersionString = arabicToDecimal(doubleDecimalValue(1, mPresenter.getTCVersion()));
                    String result = tcVersionString.substring(0, tcVersionString.indexOf("."));
                    dbUserTC.setTcVersion(Integer.parseInt(result));
                } catch (Exception e) {
                    Timber.e(e.getMessage());
                }
                if (getUserDetailResponse.getPreferredPaymentMethod() != null) {
                    dbUserTC.setPreferredPaymentMethod(getUserDetailResponse.getPreferredPaymentMethod());
                } else {
                    dbUserTC.setPreferredPaymentMethod(PAYMENT_GW);
                }
                if (!tcForDob) {
                    ((StartupActivity) getContext()).mPresenter.updateUserDetail(updateUserProfileRequest, tcFor, dbUserTC);
                }
            } else if (getContext() != null && getContext() instanceof PersonalDetailsActivity) {
                dismiss();
                ((PersonalDetailsActivity) getContext()).checkTermsConditions(true);
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void loadTermsUrl(String title, String fieldContentEn) {
        termsConditionBinding.webView.setWebViewClient(new myWebClient());
        termsConditionBinding.webView.getSettings().setJavaScriptEnabled(true);
        termsConditionBinding.webView.getSettings().setBuiltInZoomControls(true);
        termsConditionBinding.webView.getSettings().setDisplayZoomControls(false);
        termsConditionBinding.webView.loadDataWithBaseURL(null, fieldContentEn, WEBVIEW_MIME_TYPE, WEBVIEW_ENCODING, null);
        termsConditionBinding.scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(() -> {
                    if (termsConditionBinding.scrollView.getChildAt(0).getBottom()
                            <= (termsConditionBinding.scrollView.getHeight() + termsConditionBinding.scrollView.getScrollY())) {
                        //scroll view is at bottom
                        termsConditionBinding.btnIAgree.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_round_corner_blue));
                        termsConditionBinding.btnIAgree.setTextColor(getResources().getColor(R.color.border_color));
                        termsConditionBinding.btnIAgree.setEnabled(true);
                    } else {
                        //scroll view is not at bottom
                    }
                });

    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            termsConditionBinding.progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
            showSSLDialog(mContext, handler, er);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            termsConditionBinding.progressBar.setVisibility(View.GONE);
        }
    }

}