package com.aecb.ui.introscreen.intromain.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityIntroBinding;
import com.aecb.ui.introscreen.intromain.IntroPagerAdapter;
import com.aecb.ui.introscreen.intromain.presenter.IntroMainContract;
import com.aecb.ui.startsup.view.StartupActivity;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.ads.sdk.serverside.CustomData;
import com.facebook.appevents.AppEventsLogger;
import com.kochava.base.Tracker;


import javax.inject.Inject;

import static com.aecb.App.getFirebaseAnalytics;
import static com.aecb.util.ConversionApi.callConversionApi;

public class IntroActivity extends BaseActivity<IntroMainContract.View, IntroMainContract.Presenter>
        implements IntroMainContract.View {

    @Inject
    public IntroMainContract.Presenter mPresenter;
    ActivityIntroBinding introBinding;
    IntroPagerAdapter introPagerAdapter;
    private ImageView[] ivArrayDotsPager;
    final Handler handler = new Handler();
    AppEventsLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        enableBinding();
        logger = AppEventsLogger.newLogger(this);
    }

    private void enableBinding() {
        introBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        introPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());
        introBinding.vpIntroItem.setAdapter(introPagerAdapter);
        introBinding.vpIntroItem.setOffscreenPageLimit(1);
        handler.postDelayed(() -> {
            introBinding.vpIntroItem.startAutoScroll();
            introBinding.vpIntroItem.setInterval(4000);
            introBinding.vpIntroItem.setCycle(true);
            introBinding.vpIntroItem.setStopScrollWhenTouch(true);
        }, 3000);
        setUpDotsAndTexts();
        introBinding.tvSkip.setOnClickListener(v -> {
            callConversionApi(new CustomData(), AppConstants.Events.APP_TUTORIAL_COMPLETION);
            logger.logEvent(AppConstants.Events.APP_TUTORIAL_COMPLETION);
            //logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_TUTORIAL);
            getFirebaseAnalytics().logEvent(AppConstants.Events.APP_TUTORIAL_COMPLETION, null);
            Tracker.sendEvent(new Tracker.Event(AppConstants.Events.APP_TUTORIAL_COMPLETION));
            mPresenter.setIsFirstTime(false);
            moveActivity(IntroActivity.this, StartupActivity.class, true, true);
        });
    }

    private void setUpDotsAndTexts() {
        setupPagerIndicatorDots();
        ivArrayDotsPager[0].setImageResource(R.drawable.intro_selected_dot);
        final Handler handler = new Handler();
        handler.postDelayed(this::showAnimation, 300);
        introBinding.tvMainTitle.setText(getString(R.string.intro_one_title));
        introBinding.tvMainDesc.setText(getString(R.string.intro_one_desc));
        introBinding.vpIntroItem.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setUpText(position);
                for (int i = 0; i < ivArrayDotsPager.length; i++) {
                    ivArrayDotsPager[i].setImageResource(R.drawable.intro_unselected_dot);
                }
                ivArrayDotsPager[position].setImageResource(R.drawable.intro_selected_dot);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void slideToTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight() - 50, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public void showAnimation() {
        slideToTop(introBinding.tvMainTitle);
        slideToTop(introBinding.tvMainDesc);
    }

    private void setUpText(int position) {
        switch (position) {
            case 0:
                showAnimation();
                introBinding.tvMainTitle.setText(getString(R.string.intro_one_title));
                introBinding.tvMainDesc.setText(getString(R.string.intro_one_desc));
                break;
            case 1:
                showAnimation();
                introBinding.tvMainTitle.setText(getString(R.string.intro_two_title));
                introBinding.tvMainDesc.setText(getString(R.string.intro_two_desc));
                break;
            case 2:
                showAnimation();
                introBinding.tvMainTitle.setText(getString(R.string.intro_three_title));
                introBinding.tvMainDesc.setText(getString(R.string.intro_three_desc));
                break;
            case 3:
                showAnimation();
                introBinding.tvMainTitle.setText(getString(R.string.intro_four_title));
                introBinding.tvMainDesc.setText(getString(R.string.intro_four_desc));
                break;

        }
    }

    @NonNull
    @Override
    public IntroMainContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_intro;
    }

    @Override
    public void initToolbar() {

    }

    private void setupPagerIndicatorDots() {
        ivArrayDotsPager = new ImageView[4];
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 50,
                    Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
            params.setMargins(5, 0, 5, 0);
            ivArrayDotsPager[i].setLayoutParams(params);
            ivArrayDotsPager[i].setImageResource(R.drawable.intro_unselected_dot);
            ivArrayDotsPager[i].setOnClickListener(view -> view.setAlpha(1));
            introBinding.layoutDots.addView(ivArrayDotsPager[i]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
