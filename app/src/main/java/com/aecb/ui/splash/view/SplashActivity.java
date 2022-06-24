package com.aecb.ui.splash.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.databinding.ActivitySplashBinding;
import com.aecb.ui.introscreen.intromain.view.IntroActivity;
import com.aecb.ui.splash.presenter.SplashContract;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.util.LocaleHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kochava.base.Deeplink;
import com.kochava.base.DeeplinkProcessedListener;
import com.kochava.base.Tracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public final class SplashActivity extends BaseActivity<SplashContract.View, SplashContract.Presenter>
        implements SplashContract.View, DeeplinkProcessedListener {

    @Inject
    public SplashContract.Presenter mPresenter;

    private ActivitySplashBinding mSplashBinding;
    private boolean isFirstTime = false;
    final Handler handler = new Handler();

    public SplashContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    public int getLayoutRes() {
        return R.layout.activity_splash;
    }

    public void initToolbar() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
        mPresenter.saveFCMToken();
        mPresenter.getLastLoginUser();
        isFirstTime = mPresenter.isFirstTime();
        //Tracker.sendEvent(new Tracker.Event("Test_Splash_Screen"));
    }

    private void enableBinding() {
        try {
            mSplashBinding = DataBindingUtil.setContentView(this, getLayoutRes());
            Glide.with(this).asGif().load(R.drawable.aecb_splash).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    openStartupActivity();
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    resource.setLoopCount(1);
                    resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            openStartupActivity();
                        }
                    });
                    return false;
                }
            }).into(mSplashBinding.ivGIF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openStartupActivity() {
        if (isFirstTime) {
            moveActivity(SplashActivity.this, IntroActivity.class, true, false);
        } else {
            moveActivity(SplashActivity.this, StartupActivity.class, true, false);
        }
    }

    @Override
    public void setLocale(String locale) {
        LocaleHelper.setLocale(getApplicationContext(), locale);
    }

    @Override
    public void insertNotificationIntoDb() {
        if (getIntent().getExtras() != null) {
            String title = getIntent().getExtras().getString("title");
            String body = getIntent().getExtras().getString("body");
            Date currentDate = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(currentDate);
            String formattedTime = dateFormat.format(currentDate);
            DbNotification dbNotification = new DbNotification(title, body, formattedDate, formattedTime,
                    mPresenter.getUserName(), false, false);
            mPresenter.insertNotification(dbNotification);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onDeeplinkProcessed(@NonNull Deeplink deeplink) {
        if(!deeplink.destination.isEmpty()) {
            // deeplink exists, parse the destination as you see fit and route the user
            final Uri uri = Uri.parse(deeplink.destination);
            //if(uri.toString().contains("smart.link")) {
            Tracker.sendEvent(new Tracker.Event(Tracker.EVENT_TYPE_DEEP_LINK).setUri(uri));
            //}
            // … route the user to the destination accordingly
        } else {
            // no deeplink to act upon, route to a default destination or take no action
        }
    }

    // Deeplink routing should be performed in a dedicated routing activity that launches the destination activity.
    public void onStart() {
        super.onStart();

        // acquire a deeplink whenever one is available and pass it to the SDK with a desired timeout.
        final Intent intent = getIntent();
        final String deeplink = intent != null ? intent.getDataString() : null;
        if(deeplink != null && !deeplink.isEmpty()) {
            // a standard deeplink exists, so we'll pass it to the SDK to be processed
            Tracker.processDeeplink(deeplink, 10, this);
        } else {
            // no deeplink exists, so we'll pass an empty string to retrieve any deferred deeplink with a longer timeout
            Tracker.processDeeplink("", 15, this);
        }
    }

}