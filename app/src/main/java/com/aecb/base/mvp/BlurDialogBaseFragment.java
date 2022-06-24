package com.aecb.base.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.BuildConfig;
import com.aecb.R;
import com.aecb.base.network.ConnectivityUtils;
import com.aecb.data.api.models.commonmessageresponse.MessageItem;
import com.aecb.data.api.models.commonmessageresponse.MessageResponse;
import com.aecb.di.components.AppComponent;
import com.aecb.listeners.DialogButtonClickListener;
import com.aecb.presentation.blurcustomview.Blur;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.BlurEngine;
import com.aecb.presentation.blurcustomview.ViewUtilBlur;
import com.aecb.ui.exit.view.ExitFragment;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.maintanance.view.MaintenanceActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.util.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby3.mvp.MvpDialogFragment;

import java.util.List;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static com.aecb.AppConstants.ActivityIntentCode.API_FAILURE_SUCCESS;
import static com.aecb.AppConstants.ApkBuildType.DEV;
import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.IntentKey.DESC_CLICK;
import static com.aecb.util.ValidationUtil.isNullOrEmpty;

public abstract class BlurDialogBaseFragment<V extends BaseView, P extends BasePresenter<V>>
        extends MvpDialogFragment<V, P> implements BaseView {

    public static final int DEFAULT_ANIM_DURATION = 400;
    public static final boolean DEFAULT_BACKGROUND_DIMMING_ENABLED = false;
    private static final String TAG = BlurDialogBaseFragment.class.getSimpleName();
    public AppComponent appComponent = App.getAppComponent();
    private Blur blur;
    private ViewGroup root;
    private ImageView blurImgView;
    private Handler handler = new Handler();
    private final ViewTreeObserver.OnPreDrawListener preDrawListener =
            new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    root.getViewTreeObserver().removeOnPreDrawListener(this);
                    // makes sure to get the complete drawing after the layout pass
                    root.post(new Runnable() {
                        @Override
                        public void run() {
                            setUpBlurringViews();
                            startEnterAnimation();
                        }
                    });
                    return true;
                }
            };

    private Dialog progressDialog;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

        blur = new Blur(context, blurConfig());

        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            root = (ViewGroup) activity.getWindow().getDecorView();
            if (root.isShown()) {
                setUpBlurringViews();
                startEnterAnimation();
            } else {
                root.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            }
        } else {
            Timber.w(TAG, "onAttach(Context context) - context is not type of Activity. Currently Not supported.");
        }
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        onViewCreated(view, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!BuildConfig.BUILD_TYPE.equals(DEV)) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        super.onViewCreated(view, savedInstanceState);

        // will be only called if onCreateView returns non-null view
        // set to dismiss when touched outside content view
        if (view != null) {
            view.setOnTouchListener((v, event) -> {
                v.setOnTouchListener(null);
                dismiss();
                return true;
            });
        }
    }

    @Override
    public void onStart() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            if (!backgroundDimmingEnabled()) {
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
        super.onStart();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        startExitAnimation();
        super.onDismiss(dialog);
    }

    @Override
    public void onDetach() {
        root.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
        blur.destroy();
        super.onDetach();
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return BlurConfig.DEFAULT_CONFIG;
    }

    protected boolean backgroundDimmingEnabled() {
        return DEFAULT_BACKGROUND_DIMMING_ENABLED;
    }

    protected int animDuration() {
        return DEFAULT_ANIM_DURATION;
    }

    private void setUpBlurringViews() {
        Rect visibleFrame = new Rect();
        root.getWindowVisibleDisplayFrame(visibleFrame);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(visibleFrame.right - visibleFrame.left,
                visibleFrame.bottom - visibleFrame.top);
        params.setMargins(visibleFrame.left, visibleFrame.top, 0, 0);

        blurImgView = new ImageView(root.getContext());
        blurImgView.setLayoutParams(params);
        blurImgView.setAlpha(0f);

        root.addView(blurImgView);

        // apply blur effect
        Bitmap bitmapToBlur = ViewUtilBlur.drawViewToBitmap(root, visibleFrame.right,
                visibleFrame.bottom, visibleFrame.left, visibleFrame.top, blurConfig().downScaleFactor(),
                blurConfig().overlayColor());
        blur.execute(bitmapToBlur, true, new BlurEngine.Callback() {
            @Override
            public void onFinished(@Nullable Bitmap blurredBitmap) {
                blurImgView.setImageBitmap(blurredBitmap);
            }
        });
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

    private void startEnterAnimation() {
        if (blurImgView != null) {
            ViewUtilBlur.animateAlpha(blurImgView, 0f, 1f, animDuration(), null);
        }
    }

    private void startExitAnimation() {
        if (blurImgView != null) {
            ViewUtilBlur.animateAlpha(blurImgView, 1f, 0f, animDuration(), new Runnable() {
                @Override
                public void run() {
                    root.removeView(blurImgView);
                }
            });
        }
    }

    public void showLoading(String message) {
        if (getContext() != null) {
            progressDialog = DialogUtils.showLoading(getContext(), message);
        }
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

    @Override
    public void localValidationError(String title, String desc) {
        showErrorMsgFromApi(title, desc,
                getString(R.string.ok), null, null);
    }


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

    public void hideLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) progressDialog.cancel();
            progressDialog = null;
        }
    }

    public void clientError(String errorMessage) {
        if (getContext() != null) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public void networkError(String errorMessage) {
        if (getContext() != null) {
            Timber.e(errorMessage);
            if (!this.isNetworkConnected()) {
                Toast.makeText(getContext(), getString(R.string.networkError), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
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

    public boolean isNetworkConnected() {
        return ConnectivityUtils.isConnectedToInternet(getContext());
    }

    public void onTimeout() {
        Toast.makeText(getContext(), getString(R.string.timeoutError), Toast.LENGTH_SHORT).show();
    }

    public void serverError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getContext(), getString(R.string.serverError), Toast.LENGTH_SHORT).show();
    }

    public void unauthenticated() {
        if (getContext() != null) {
            Toast.makeText(getContext(), getString(R.string.unauthenticatedError), Toast.LENGTH_LONG).show();
            getActivity().finishAffinity();
        }

    }

    public void unexpectedError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void unexpectedError(RuntimeException exception) {
        if (exception.getMessage() != null) {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @CallSuper
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    //  copied from
    //  android.support.design.widget.BottomSheetDialogFragment
    //  because we can't inherit multiple classes, else we would have just inherited BaseBottomSheetFragment
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        return new BottomSheetDialog(this.getContext(), this.getTheme());
    }

    //  copied from
    //  android.support.v7.app.AppCompatDialogFragment
    //  because we can't inherit multiple classes, else we would have just inherited BaseBottomSheetFragment
    @SuppressLint("RestrictedApi")
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void setupDialog(Dialog dialog, int style) {
        if (dialog instanceof AppCompatDialog) {
            AppCompatDialog acd = (AppCompatDialog) dialog;
            switch (style) {
                case 3:
                    dialog.getWindow().addFlags(24);
                case 1:
                case 2:
                    acd.supportRequestWindowFeature(1);
            }
        } else {
            super.setupDialog(dialog, style);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoading();
    }

    @LayoutRes
    public abstract int getLayoutRes();

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}