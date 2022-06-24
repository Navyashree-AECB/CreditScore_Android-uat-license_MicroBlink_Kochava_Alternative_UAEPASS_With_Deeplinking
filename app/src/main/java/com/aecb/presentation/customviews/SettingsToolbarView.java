package com.aecb.presentation.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.aecb.R;
import com.aecb.listeners.ToolbarCancelListener;
import com.aecb.listeners.ToolbarListener;
import com.aecb.util.inflatelayout.InflateUtils;

public class SettingsToolbarView extends FrameLayout {

    ImageView backButton, cancelButton;
    TextView textView;
    RelativeLayout parentView;

    public SettingsToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public SettingsToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public SettingsToolbarView(Context context) {
        super(context);
        initViews(context);
    }

    public final void setBackBtnListener(ToolbarListener backBtnListener) {
        if (backButton != null)
            backButton.setOnClickListener(v -> backBtnListener.backInvoke());
    }

    public final void setCancelBtnListener(ToolbarCancelListener cancelBtnListener) {
        cancelButton.setOnClickListener(v -> cancelBtnListener.cancelInvoke());
    }

    public final void setToolbarTitle(@StringRes int textRes) {
        textView.setText(this.getContext().getString(textRes));
    }

    public final void setToolbarTitle(String text) {
        textView.setText(text);
    }

    public final void setBtnVisibility(boolean btnBack, boolean btnCancel) {
        InflateUtils.setVisibility(backButton, btnBack);
        InflateUtils.setVisibility(cancelButton, btnCancel);
    }

    public void initViews(Context context) {
        View view = InflateUtils.inflate(context, R.layout.view_settings_toolbar, null, false);
        addView(view);
        backButton = view.findViewById(R.id.btnBack);
        cancelButton = view.findViewById(R.id.btnCancel);
        textView = view.findViewById(R.id.tvText);
        parentView = view.findViewById(R.id.parentView);
    }

}