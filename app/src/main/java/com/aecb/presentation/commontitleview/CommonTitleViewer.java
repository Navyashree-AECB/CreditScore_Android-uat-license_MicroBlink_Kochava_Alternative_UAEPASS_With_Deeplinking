package com.aecb.presentation.commontitleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aecb.R;
import com.aecb.util.inflatelayout.InflateUtils;

public class CommonTitleViewer extends FrameLayout {

    public CommonTitleViewer(@NonNull Context context) {
        super(context);
    }

    TextView tvDescription;

    public CommonTitleViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, context);
    }

    public CommonTitleViewer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, context);
    }

    public CommonTitleViewer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs, context);
    }

    private void initAttrs(AttributeSet attrs, Context context) {
        View view = InflateUtils.inflate(context, R.layout.view_common_title_viewer, null, false);
        addView(view);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleViewer);
        int drawableSrc = arr.getResourceId(R.styleable.CommonTitleViewer_imgSrc, -1);
        String title = arr.getString(R.styleable.CommonTitleViewer_myTitle);
        String description = arr.getString(R.styleable.CommonTitleViewer_myDescription);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvTitle.setText(title);
        tvDescription.setText(description);
        imageView.setImageResource(drawableSrc);
        arr.recycle();
    }

    public String getDescription() {
        return tvDescription.getText().toString();
    }

    public void setDescription(String description) {
        if (tvDescription != null)
            tvDescription.setText(description);
    }
}