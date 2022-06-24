package com.aecb.presentation.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.aecb.AppConstants;

import timber.log.Timber;

public class MeasuringViewPager extends ViewPager {

    private View mCurrentView;
    int selectedTab=0;

    public MeasuringViewPager(Context context) {
        super(context);

    }

    public MeasuringViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mCurrentView != null) {
            mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            if(selectedTab==1){

                heightMeasureSpec = MeasureSpec.makeMeasureSpec(AppConstants.ScreenHeight, MeasureSpec.EXACTLY);
            }else{

                heightMeasureSpec = MeasureSpec.makeMeasureSpec(AppConstants.BottomBarHeight, MeasureSpec.EXACTLY);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    public void measureCurrentView(View currentView, int _selectedTab) {
        mCurrentView = currentView;
        selectedTab=_selectedTab;
        Timber.d("Height Issue:: "+ " measureCurrentView Called");
        requestLayout();
    }

}