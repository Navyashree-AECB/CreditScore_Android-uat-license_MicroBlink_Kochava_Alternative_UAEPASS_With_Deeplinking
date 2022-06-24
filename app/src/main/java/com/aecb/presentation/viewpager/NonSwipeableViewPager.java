package com.aecb.presentation.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class NonSwipeableViewPager extends ViewPager {

    int selectedTab = 0;
    private View mCurrentView;

    public NonSwipeableViewPager(Context context) {
        super(context);
        //setMyScroller();
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setMyScroller();
    }

    /* @Override
     public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         super.onMeasure(widthMeasureSpec, heightMeasureSpec);

         if (mCurrentView != null) {
             mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
             heightMeasureSpec = MeasureSpec.makeMeasureSpec(AppConstants.BottomBarHeight, MeasureSpec.EXACTLY);
             super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         }
     }


     public void measureCurrentView(View currentView, int _selectedTab) {
         mCurrentView = currentView;
         selectedTab = _selectedTab;
         Timber.d("Height Issue:: " + " measureCurrentView Called");
         requestLayout();
     }
 */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    //down one is added for smooth scrolling

  /*  private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            Timber.d("Exception : "+e.toString());
        }
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 *//*1 secs*//*);
        }
    }*/
}