package com.aecb.ui.menu.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.BuildConfig;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.biometric.UpdateBiometricRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.FragmentMenuBinding;
import com.aecb.presentation.biometric.BiometricUtils;
import com.aecb.ui.aboutus.view.AboutUsActivity;
import com.aecb.ui.changepassword.view.ChangePasswordActivity;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.exit.view.ExitFragment;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.menu.presenter.MenuContract;
import com.aecb.ui.profile.view.ProfileActivity;
import com.aecb.ui.purchasejourney.cardlist.view.CardListActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.ui.termsandcodition.view.TermsAndConditionActivity;
import com.aecb.ui.touchdialog.view.TouchFragment;
import com.aecb.util.LocaleHelper;
import com.aecb.util.ValidationUtil;

import javax.inject.Inject;

import static com.aecb.AppConstants.ActivityIntentCode.TOUCH_ID;
import static com.aecb.AppConstants.IntentKey.FROM_MENU_SCREEN;
import static com.aecb.util.FirebaseLogging.enableDisableTouchId;
import static com.aecb.util.FirebaseLogging.languageSwitched;
import static com.aecb.util.FirebaseLogging.profileVisited;
import static com.aecb.util.FirebaseLogging.userLoggedOut;
import static com.aecb.util.FirebaseLogging.visitedAboutAecb;
import static com.aecb.util.FirebaseLogging.visitedChangePassword;
import static com.aecb.util.FirebaseLogging.visitedManagePayment;
import static com.aecb.util.FirebaseLogging.visitedTandC;
import static com.aecb.util.varibles.StringConstants.SPACE;

public class MenuActivity extends BaseActivity<MenuContract.View, MenuContract.Presenter> implements
        MenuContract.View, View.OnClickListener {

    @Inject
    public MenuContract.Presenter mPresenter;
    FragmentMenuBinding menuBinding;
    private UpdateUserProfileRequest updateUserProfileRequest;
    private BlurDialogBaseFragment changeLangDialogFragment;
    private BlurDialogBaseFragment logoutDialog;
    private BlurDialogBaseFragment touchIdFragment;
    private BlurDialogBaseFragment confirmationDialogFragment;
    private UpdateBiometricRequest updateBiometricRequest;
    private DBUserTC dbUserTC;
    private boolean chkLocalTouch;
    private String mPassword;
    private boolean isArabic = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getUserDetail();
        enableBinding();
        setCurrentLang();
    }

    private void enableBinding() {
        menuBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        menuBinding.llProfile.setOnClickListener(this);
        menuBinding.llManagePayment.setOnClickListener(this);
        menuBinding.ivAecbLogo.setOnClickListener(this);
        menuBinding.btnCancel.setOnClickListener(this);
        menuBinding.llChangePassword.setOnClickListener(this);
        menuBinding.swLanguage.setOnClickListener(this);
        menuBinding.swTouchId.setOnClickListener(this);
        updateUserProfileRequest = new UpdateUserProfileRequest();
        updateBiometricRequest = new UpdateBiometricRequest();
        updateBiometricRequest.setIsEnabledFaceID(false);
        menuBinding.llAbout.setOnClickListener(this);
        menuBinding.tvLogout.setOnClickListener(this);
        menuBinding.llTermsConditions.setOnClickListener(this);
        menuBinding.tvVersionName.setText(getString(R.string.app_version) + SPACE + BuildConfig.VERSION_NAME);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_menu;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public MenuContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llProfile:
                profileVisited();
                moveActivity(MenuActivity.this, ProfileActivity.class, false, false);
                break;
            case R.id.llManagePayment:
                visitedManagePayment();
                Bundle bundle1 = new Bundle();
                bundle1.putString(AppConstants.IntentKey.FROM_ACTIVITY, FROM_MENU_SCREEN);
                moveActivity(this, CardListActivity.class, false, false, bundle1);
                break;
            case R.id.ivAecbLogo:
            case R.id.btnCancel:
                moveActivity(this, DashboardActivity.class, true, true);
                break;
            case R.id.llAbout:
                visitedAboutAecb();
                moveActivity(this, AboutUsActivity.class, false, false);
                break;
            case R.id.tv_logout:
                Bundle blogout = new Bundle();
                blogout.putInt(AppConstants.IntentKey.FROM_ACTIVITY, AppConstants.ActivityIntentCode.FOR_LOGOUT);
                blogout.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.dialog_logout));
                blogout.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.yes));
                blogout.putString(AppConstants.IntentKey.NEGATIVE_TEXT, getString(R.string.no));
                logoutDialog = ExitFragment.newInstance(blogout);
                logoutDialog.show(getSupportFragmentManager(), logoutDialog.getTag());
                break;
            case R.id.llChangePassword:
                visitedChangePassword();
                moveActivity(this, ChangePasswordActivity.class, false, false);
                break;
            case R.id.swLanguage:
                if (isNetworkConnected()) {
                    Bundle b = new Bundle();
                    b.putInt(AppConstants.IntentKey.FROM_ACTIVITY, AppConstants.ActivityIntentCode.FOR_CHANGE_LANG_SETTINGS);
                    b.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.dialog_msg_change_language));
                    b.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.yes));
                    b.putString(AppConstants.IntentKey.NEGATIVE_TEXT, getString(R.string.no));
                    changeLangDialogFragment = ExitFragment.newInstance(b);
                    changeLangDialogFragment.setCancelable(false);
                    changeLangDialogFragment.show(getSupportFragmentManager(), changeLangDialogFragment.getTag());
                } else {
                    showInternetNotAvailableDialog();
                    setCurrentLang();
                }
                break;
            case R.id.swTouchId:
                if (mPresenter != null && dbUserTC.isTouchId()) {
                    mPresenter.updateTouchIdInLocal(false, dbUserTC.getEmail());
                } else {
                    confirmationDialog(getString(R.string.please_enter_your_password),
                            AppConstants.ActivityIntentCode.CHANGE_TOUCH_ENABLE, getString(R.string.enable_touch_id));
                }
                break;
            case R.id.llTermsConditions:
                visitedTandC();
                moveActivity(this, TermsAndConditionActivity.class, false, false);
                break;
        }
    }

    public void confirmationDialog(String alertTitle, int dialogIdentifier, String positiveButtonText) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY, dialogIdentifier);
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, alertTitle);
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, positiveButtonText);
        bundle.putString(AppConstants.IntentKey.NEGATIVE_TEXT, getString(R.string.skip));
        touchIdFragment = TouchFragment.newInstance(bundle);
        touchIdFragment.setCancelable(false);
        touchIdFragment.show(getSupportFragmentManager(), touchIdFragment.getTag());
    }

    public void updateTouchEnable(boolean isStatus, int identifier, String password) {
        mPassword = password;
        if (touchIdFragment != null) {
            touchIdFragment.dismiss();
        }
        if (isStatus) {
            if (identifier == AppConstants.ActivityIntentCode.CHANGE_TOUCH_ENABLE) {
                if (biometricSetUp()) {
                    if (BiometricUtils.isFingerprintAvailable(this)) {
                        updateBiometricRequest.setIsEnabledTouchID(true);
                        updateBiometricReqCall();
                    } else {
                        chkLocalTouch = true;
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), TOUCH_ID);
                    }
                } else {
                    chkLocalTouch = true;
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), TOUCH_ID);
                }

            } else if (identifier == AppConstants.ActivityIntentCode.CHANGE_TOUCH_DESABLE) {
                updateBiometricRequest.setIsEnabledTouchID(false);
                updateBiometricReqCall();
            }
        } else {
            if (identifier == AppConstants.ActivityIntentCode.CHANGE_TOUCH_DESABLE || identifier == AppConstants.ActivityIntentCode.CHANGE_TOUCH_ENABLE) {
                setCheckTouchId();
            }
        }
    }

    public void updateBiometricReqCall() {
        if (mPassword != null && !mPassword.isEmpty()) {
            mPresenter.callLoginApi(updateBiometricRequest.isIsEnabledTouchID(), dbUserTC.getEmail(), mPassword);
        }

    }

    public void setCheckTouchId() {
        if (dbUserTC != null && dbUserTC.isTouchId()) {
            menuBinding.swTouchId.setChecked(true);
        } else {
            menuBinding.swTouchId.setChecked(false);
        }
       /* if (dbUserTC != null && dbUserTC.getIsUAEPassUser() == 1) {
            menuBinding.llChangePassword.setVisibility(View.GONE);
            menuBinding.viewPassword.setVisibility(View.GONE);
            menuBinding.llTouchId.setVisibility(View.GONE);
            menuBinding.viewTouchID.setVisibility(View.GONE);
        } else {
            menuBinding.llChangePassword.setVisibility(View.VISIBLE);
            menuBinding.viewPassword.setVisibility(View.VISIBLE);
            menuBinding.llTouchId.setVisibility(View.VISIBLE);
            menuBinding.viewTouchID.setVisibility(View.VISIBLE);
        }*/
    }

    public boolean biometricSetUp() {
        boolean check = false;
        if (BiometricUtils.isSdkVersionSupported()) {
            if (BiometricUtils.isPermissionGranted(this)) {
                if (BiometricUtils.isHardwareSupported(this)) {
                    if (BiometricUtils.isFingerprintAvailable(this)) {
                        check = true;
                    }
                }
            }
        }
        return check;
    }

    @Override
    public void onBackPressed() {
        moveActivity(this, DashboardActivity.class, true, true);
    }

    /**
     * set language from shared preferences
     */
    public void setCurrentLang() {
        String mAppLang = mPresenter.getAppLanguage();
        if (!ValidationUtil.isNullOrEmpty(mAppLang)) {
            if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                loadSubTitle(false, getString(R.string.txt_switchto));
                isArabic = true;
            } else if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                loadSubTitle(true, getString(R.string.txt_switchto));
                isArabic = false;
            }
        } else {
            isArabic = false;
            loadSubTitle(false, getString(R.string.txt_english));
        }
    }

    private void loadSubTitle(boolean b, String string) {
        menuBinding.swLanguage.setChecked(b);
        menuBinding.txtSwitchTo.setText(string);
    }

    /**
     * change language dialog button(yes and no) click to call this method
     *
     * @param chkChangeLang - boolean (true-yes)
     */
    public void changeLanguage(boolean chkChangeLang) {
        if (changeLangDialogFragment != null) {
            changeLangDialogFragment.dismiss();
        }
        if (chkChangeLang) {
            if (!isArabic) {
                updateUserProfileRequest.setUpdateType(3);
                updateUserProfileRequest.setPreferredlanguage(AppConstants.AppLanguage.ISO_CODE_ENG);
                updateUserProfileRequest.setChannel(2);
                mPresenter.saveUserAdvProfile(updateUserProfileRequest);
            } else if (isArabic) {
                updateUserProfileRequest.setUpdateType(3);
                updateUserProfileRequest.setPreferredlanguage(AppConstants.AppLanguage.ISO_CODE_ARABIC);
                updateUserProfileRequest.setChannel(2);
                mPresenter.saveUserAdvProfile(updateUserProfileRequest);
            }
        } else {
            setCurrentLang();
        }
    }

    @Override
    public void showError(String message) {
        setCheckTouchId();
        localValidationError(getString(R.string.error), message);
    }

    @Override
    public void updateUserAdvProfileResponse() {
        languageSwitched();
        handler.postDelayed(() -> {
            LocaleHelper.setLocale(getApplicationContext(), updateUserProfileRequest.getPreferredlanguage());
            moveActivity(this, MenuActivity.class, true, false);
        }, 3000);
    }

    @Override
    public void updateBiometricResponse(boolean touchEnable) {
        setCheckTouchId();
        enableDisableTouchId();
        showApiSuccessMessage(getString(R.string.touch_login_enable_successfully));
    }

    @Override
    public void setLastUserDetails(DBUserTC dbUserTC) {
        this.dbUserTC = dbUserTC;
        setCheckTouchId();
    }

    @Override
    public void openLoginActivity() {
        userLoggedOut();
        moveActivity(this, LoginActivity.class, true, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPassword != null && !mPassword.isEmpty()) {
            if (chkLocalTouch) {
                if (BiometricUtils.isFingerprintAvailable(this)) {
                    updateBiometricRequest.setIsEnabledTouchID(true);
                    updateBiometricReqCall();
                }
            }
            if (!biometricSetUp() && chkLocalTouch) {
                setCheckTouchId();
                chkLocalTouch = true;
            } else {
                chkLocalTouch = false;
            }
        }

    }

    public void dialogDismiss() {
        if (logoutDialog != null) {
            logoutDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}