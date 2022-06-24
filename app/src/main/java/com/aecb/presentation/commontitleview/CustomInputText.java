package com.aecb.presentation.commontitleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.aecb.R;
import com.aecb.util.ValidationUtil;
import com.aecb.util.inflatelayout.InflateUtils;
import com.google.android.material.textfield.TextInputEditText;

public class CustomInputText extends FrameLayout {

    public TextInputEditText editText;
    TextView tvTitle;
    View viewLine;

    public CustomInputText(@NonNull Context context) {
        super(context);
    }

    public CustomInputText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, context);
    }

    public CustomInputText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomInputText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs, context);
    }

    private void initAttrs(AttributeSet attrs, Context context) {
        View view = InflateUtils.inflate(context, R.layout.view_custom_input_text, null, false);
        addView(view);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CommonTextField);
        int editInputType = arr.getInt(R.styleable.CommonTextField_android_inputType, 0);
        String textHint = arr.getString(R.styleable.CommonTextField_hint);
        String titleText = arr.getString(R.styleable.CommonTextField_titleText);
        String maxLength = arr.getString(R.styleable.CommonTextField_maxLength);
        String digits = arr.getString(R.styleable.CommonTextField_myDigits);
        boolean enabled = arr.getBoolean(R.styleable.CommonTextField_enabled, true);
        boolean focusable = arr.getBoolean(R.styleable.CommonTextField_focusable, true);
        boolean hideUnderline = arr.getBoolean(R.styleable.CommonTextField_hideUnderLine, true);
        tvTitle = view.findViewById(R.id.tvTitle);
        editText = view.findViewById(R.id.editText);
        viewLine = view.findViewById(R.id.view);
        editText.setEnabled(enabled);
        editText.setFocusable(focusable);
        tvTitle.setText(titleText);
        if (hideUnderline) {
            viewLine.setVisibility(VISIBLE);
        } else {
            viewLine.setVisibility(GONE);
        }
        if (digits != null) {
            editText.setKeyListener(DigitsKeyListener.getInstance(digits));
        }
        if (editInputType != 0)
            editText.setInputType(editInputType);
        if (!ValidationUtil.isNullOrEmpty(maxLength)) {
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(Integer.parseInt(maxLength));
            editText.setFilters(fArray);
        }
        editText.setHint(textHint);
        arr.recycle();
    }

    public void setHint(String string) {
        if (editText != null)
            editText.setHint(string);
    }

    public void setTitleText(String string) {
        tvTitle.setText(string);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String name) {
        editText.setText(name);
    }

    public void setErrorText(String errorText) {
        editText.setError(errorText);
    }

    public void requestFocusEditText() {
        editText.requestFocus();
    }
}