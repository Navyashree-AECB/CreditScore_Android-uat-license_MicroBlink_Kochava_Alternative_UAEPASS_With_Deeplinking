package com.aecb.base.mvp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.RequiresApi;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.BuildConfig;
import com.aecb.R;
import com.aecb.base.network.ConnectivityUtils;
import com.aecb.data.api.models.commonmessageresponse.MessageItem;
import com.aecb.data.api.models.commonmessageresponse.MessageResponse;
import com.aecb.di.components.AppComponent;
import com.aecb.listeners.DialogButtonClickListener;
import com.aecb.ui.exit.view.ExitFragment;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.maintanance.view.MaintenanceActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.util.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.List;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static com.aecb.AppConstants.ActivityIntentCode.API_FAILURE_SUCCESS;
import static com.aecb.AppConstants.ApkBuildType.DEV;
import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.IntentKey.DESC_CLICK;
import static com.aecb.util.ValidationUtil.isNullOrEmpty;

public abstract class BaseBottomSheetFragment<V extends BaseView, P extends BasePresenter<V>> extends
        BottomSheetDialogFragment implements BaseView {

    public AppComponent appComponent = App.getAppComponent();
    private Handler handler = new Handler();

    private Dialog progressDialog;

    public void showLoading(String message) {
        if (getContext() != null) {
            progressDialog = DialogUtils.showLoading(getContext(), message);

        }
    }

    public void hideLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) progressDialog.cancel();
            progressDialog = null;
        }
    }

    public void clientError(String errorMessage) {
        if (getContext() != null) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    public void networkError(String errorMessage) {
        if (getContext() != null) {
            Timber.e(errorMessage);
            if (!this.isNetworkConnected()) {
                Toast.makeText(getContext(), getString(R.string.networkError), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void connectionError(String errorMessage) {
        if (getContext() != null) {
            Timber.e(errorMessage);
            if (!this.isNetworkConnected()) {
                showApiFailureError(getString(R.string.networkError), "", "");
            } else {
                showApiFailureError(getString(R.string.connectionError), "", "");
            }
        }
    }

    private boolean isNetworkConnected() {
        return ConnectivityUtils.isConnectedToInternet(getContext());
    }

    public void onTimeout() {
        Toast.makeText(getContext(), getString(R.string.timeoutError), Toast.LENGTH_LONG).show();
    }

    public void serverError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getContext(), getString(R.string.serverError), Toast.LENGTH_LONG).show();
    }

    public void unauthenticated() {
        if (getContext() != null) {
            Toast.makeText(getContext(), getString(R.string.unauthenticatedError), Toast.LENGTH_LONG).show();
            getActivity().finishAffinity();
        }

    }

    public void unexpectedError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void gotoAppUnderMaintain() {
        ((BaseActivity) getActivity()).moveActivity(getActivity(), MaintenanceActivity.class, true, true);
    }

    @Override
    public void sessionTokenExpired() {
        showErrorMsgFromApi(getString(R.string.session_expired), getString(R.string.please_login_again),
                getString(R.string.ok), null, new DialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        ((BaseActivity) getActivity()).moveActivity(getActivity(), LoginActivity.class,
                                true, true);
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                    }
                });
    }

    public void unexpectedError(RuntimeException exception) {
        if (exception.getMessage() != null) {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showErrorMsgFromApi(String title, String description, String yesButton, String noButton,
                                    DialogButtonClickListener dialogButtonClickListener) {
        Dialog dialog = new Dialog(getActivity());
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
            tvYes.setOnClickListener(v -> dialog.dismiss());
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

    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        if (!BuildConfig.BUILD_TYPE.equals(DEV)) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void localValidationError(String title, String desc) {
        showErrorMsgFromApi(title, desc,
                getString(R.string.ok), null, null);
    }

    @LayoutRes
    public abstract int getLayoutRes();

    @RequiresApi(api = Build.VERSION_CODES.N)
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


    private MessageItem getMessageItem(List<MessageItem> message, String status, String useCase) {
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
        paymentStatusDialog.show(getFragmentManager(), paymentStatusDialog.getTag());
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

    @Override
    public void showApiSuccessMessage(String message) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY,
                API_FAILURE_SUCCESS);
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.success));
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, message);
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.txt_ok));
        BlurDialogBaseFragment paymentStatusDialog = ExitFragment.newInstance(bundle);
        paymentStatusDialog.setCancelable(false);
        paymentStatusDialog.show(getFragmentManager(), paymentStatusDialog.getTag());
        handler.postDelayed(paymentStatusDialog::dismiss, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}