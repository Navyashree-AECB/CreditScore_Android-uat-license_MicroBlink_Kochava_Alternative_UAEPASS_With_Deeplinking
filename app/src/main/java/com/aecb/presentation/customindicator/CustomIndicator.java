package com.aecb.presentation.customindicator;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aecb.R;

import java.util.List;

public class CustomIndicator extends RelativeLayout implements ViewPager.OnPageChangeListener,
        View.OnTouchListener {

    private static final String TAG = "CustomIndicator";

    private int lineWidth;
    private int lineWidthSelected;
    private int lineHeight;
    private int lineMargin;

    private int lineWidthWithMargin;
    private int lineWidthSelectedWithMargin;

    private int customWidth;
    private int customHeight;

    private int selectedLineDrawableResource;
    private int unselectedLineDrawableResource;

    private PagerAdapter adapter;

    private LinearLayout linesLayout;
    private RelativeLayout customView;
    private AppCompatImageView customViewImage;
    private int selectedPosition = -1;

    private ValueAnimator expandAnimator;
    private ValueAnimator collapseAnimator;

    private Drawable[] drawableList;

    public CustomIndicator(Context context) {
        this(context, null);
    }

    public CustomIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setClipChildren(false);
        this.setClipToPadding(false);
        this.setClickable(true);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomIndicator);
        try {
            lineWidth = (int) typedArray.getDimension(R.styleable.CustomIndicator_ti_lineWidth, dpToPx(16));
            lineHeight = (int) typedArray.getDimension(R.styleable.CustomIndicator_ti_lineHeight, dpToPx(6));
            lineWidthSelected = (int) typedArray.getDimension(R.styleable.CustomIndicator_ti_lineWidthSelected, dpToPx(32));
            lineMargin = (int) typedArray.getDimension(R.styleable.CustomIndicator_ti_lineMargin, dpToPx(4));
            lineWidthWithMargin = lineWidth + (lineMargin * 2);
            lineWidthSelectedWithMargin = lineWidthSelected + (lineMargin * 2);
            customWidth = (int) typedArray.getDimension(R.styleable.CustomIndicator_ti_tooltipWidth, dpToPx(100));
            customHeight = (int) typedArray.getDimension(R.styleable.CustomIndicator_ti_tooltipHeight, dpToPx(180));
            selectedLineDrawableResource = typedArray.getResourceId(R.styleable.CustomIndicator_ti_selectedLineDrawable, R.drawable.indicator_line_selected);
            unselectedLineDrawableResource = typedArray.getResourceId(R.styleable.CustomIndicator_ti_unselectedLineDrawable, R.drawable.indicator_line_unselected);
        } finally {
            typedArray.recycle();
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        this.adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new NullPointerException("ViewPager's adapter cannot be null.");
        }
        this.removeAllViews();
        initIndicatorLines();
        initCustomView();
        selectPage(0);
        viewPager.removeOnPageChangeListener(this);
        viewPager.addOnPageChangeListener(this);
        this.setOnTouchListener(this);
    }

    public void setDrawables(List<Drawable> drawableList) {
        if (adapter == null) {
            throw new NullPointerException("ViewPager's adapter cannot be null.");
        }
        this.drawableList = drawableList.toArray(new Drawable[adapter.getCount()]);
    }

    private void initCustomView() {
        LayoutParams layoutParams = new LayoutParams(customWidth, customHeight);
        layoutParams.topMargin = -(layoutParams.height + dpToPx(8));
        customView = new RelativeLayout(getContext());
        customView.setLayoutParams(layoutParams);
        customView.setBackgroundResource(R.drawable.indicator_line_selected);
        customView.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        customViewImage = new AppCompatImageView(getContext());
        customViewImage.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        customView.setScaleX(0);
        customView.setScaleY(0);
        customView.setAlpha(0);
        customView.setTranslationY(layoutParams.height / 2);
        customView.addView(customViewImage);
        this.addView(customView);
    }

    private void initIndicatorLines() {
        linesLayout = new LinearLayout(getContext());
        linesLayout.setVerticalGravity(LinearLayout.HORIZONTAL);
        int linesCount = adapter.getCount();
        for (int i = 0; i < linesCount; i++) {
            View lineView = new View(getContext());
            lineView.setBackgroundResource(unselectedLineDrawableResource);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(lineWidth, lineHeight);
            layoutParams.leftMargin = lineMargin;
            layoutParams.rightMargin = lineMargin;
            lineView.setLayoutParams(layoutParams);
            linesLayout.addView(lineView);
        }
        this.addView(linesLayout);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        if (drawableList == null || drawableList.length == 0) {
            return true;
        }
        int toolTipX = Math.max(0, x);
        toolTipX = Math.min(toolTipX, getMeasuredWidth());
        customView.setX(toolTipX - customView.getMeasuredWidth() / 2);
        int pos;
        int totalWidthBefore = selectedPosition * lineWidthWithMargin;
        if (toolTipX >= totalWidthBefore + lineWidthSelectedWithMargin) {
            pos = (selectedPosition + 1) + (toolTipX - (totalWidthBefore + lineWidthSelectedWithMargin))
                    / lineWidthWithMargin;
        } else if (toolTipX > totalWidthBefore) {
            pos = selectedPosition;
        } else {
            pos = toolTipX / lineWidthWithMargin;
        }
        pos = Math.min(pos, drawableList.length - 1);
        if (customViewImage.getBackground() != drawableList[pos]) {
            customViewImage.setBackgroundDrawable(drawableList[pos]);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                customView.animate().alpha(1).scaleX(1).scaleY(1).translationY(1).setInterpolator(new
                        OvershootInterpolator()).start();
                break;
            case MotionEvent.ACTION_UP:
                customView.animate().alpha(0).scaleX(0).scaleY(0).translationY(customView.getMeasuredHeight()
                        / 2).setInterpolator(new LinearInterpolator()).start();
                break;
        }

        return true;
    }

    private void selectPage(int position) {
        selectedPosition = position;
        View childAt = linesLayout.getChildAt(selectedPosition);
        if (childAt == null) return;
        if (selectedPosition != -1) {
            collapseView(childAt);
        }
        expandView(childAt);
    }

    private void expandView(final View selectedView) {
        selectedView.setBackgroundResource(selectedLineDrawableResource);
        if (expandAnimator != null) {
            expandAnimator.end();
        }
        expandAnimator = ValueAnimator.ofObject(new IntEvaluator(), lineWidth, lineWidthSelected);
        expandAnimator.setInterpolator(new DecelerateInterpolator());
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) selectedView.getLayoutParams();
                layoutParams.width = (int) animation.getAnimatedValue();
                selectedView.setLayoutParams(layoutParams);
            }
        });
        expandAnimator.setDuration(200);
        expandAnimator.start();
    }

    private void collapseView(final View selectedView) {
        selectedView.setBackgroundResource(unselectedLineDrawableResource);
        if (collapseAnimator != null) {
            collapseAnimator.end();
        }
        collapseAnimator = ValueAnimator.ofObject(new IntEvaluator(), lineWidthSelected, lineWidth);
        collapseAnimator.setInterpolator(new DecelerateInterpolator());
        collapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) selectedView.getLayoutParams();
                layoutParams.width = (int) animation.getAnimatedValue();
                selectedView.setLayoutParams(layoutParams);
            }
        });
        collapseAnimator.setDuration(500);
        collapseAnimator.start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public int dpToPx(int dp) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

}