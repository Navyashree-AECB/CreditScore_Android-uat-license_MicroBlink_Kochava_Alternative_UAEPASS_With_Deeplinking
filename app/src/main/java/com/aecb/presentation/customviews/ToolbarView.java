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
import com.aecb.util.Utilities;
import com.aecb.util.inflatelayout.InflateUtils;

public class ToolbarView extends FrameLayout {

    ImageView backButton, backCancel;
    TextView textView;
    RelativeLayout parentView;

    public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ToolbarView(Context context) {
        super(context);
        initViews(context);
    }

    public final void setBackBtnListener(ToolbarListener backBtnListener) {
        backButton.setOnClickListener(v -> backBtnListener.backInvoke());
    }

    public final void setCancelBtnListener(ToolbarCancelListener cancelBtnListener) {
        backCancel.setOnClickListener(v -> cancelBtnListener.cancelInvoke());
    }

    public final void setToolbarTitle(@StringRes int textRes) {
        textView.setText(this.getContext().getString(textRes));
    }

    public final void setToolbarTitleVisibility(boolean visibility) {
        InflateUtils.setVisibility(textView, visibility);
    }

    public final void setToolbarTitle(String text) {
        textView.setText(text);
    }

    public final void setBtnVisibility(boolean btnBack, boolean btnCancel) {
        InflateUtils.setVisibility(backButton, btnBack);
        InflateUtils.setVisibility(backCancel, btnCancel);
    }

    public void initViews(Context context) {
        View view = InflateUtils.inflate(context, R.layout.view_toolbar, null, false);
        addView(view);
        backButton = view.findViewById(R.id.btnBack);
        backCancel = view.findViewById(R.id.btnCancel);
        textView = view.findViewById(R.id.tvText);
        parentView = view.findViewById(R.id.parentView);
        parentView.setPadding(0, Utilities.getStatusBarHeight(context), 0, 0);
    }

}