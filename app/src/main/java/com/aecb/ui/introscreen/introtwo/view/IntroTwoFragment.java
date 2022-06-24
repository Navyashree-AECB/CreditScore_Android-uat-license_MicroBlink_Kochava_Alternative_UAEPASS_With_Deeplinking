package com.aecb.ui.introscreen.introtwo.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.databinding.FragmentIntroTwoBinding;
import com.aecb.ui.introscreen.intromain.view.IntroActivity;
import com.aecb.ui.introscreen.introtwo.presenter.IntroTwoContract;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import timber.log.Timber;

public class IntroTwoFragment extends BaseFragment<IntroTwoContract.View, IntroTwoContract.Presenter> implements
        IntroTwoContract.View {

    @Inject
    public IntroTwoContract.Presenter mPresenter;
    FragmentIntroTwoBinding introTwoBinding;
    private Timer timerForFirstTimeUser;
    private int currentPositionForFirstTimeUser = -1;
    private int DEFAULT_SCORE = 300;
    private int MAXIMUM_SCORE = 900;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(false);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        introTwoBinding = DataBindingUtil.inflate(
                inflater, getLayoutRes(), container, false);
        View view = introTwoBinding.getRoot();
        initialSetup();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            showLoadingAnimationForFirstTimeUser();
            introTwoBinding.tvCreditScoreFirstTimeUser.setVisibility(View.VISIBLE);
            introTwoBinding.tvCreditScoreFirstTimeUser.setCharacterDelay(20);
            introTwoBinding.tvCreditScoreFirstTimeUser.animateText(getString(R.string.get_you_credit_score_now));
        }else {
            if (timerForFirstTimeUser != null) {
                timerForFirstTimeUser.cancel();
                timerForFirstTimeUser.purge();
            }
        }
    }

    private void initialSetup() {
        introTwoBinding.creditScoreArcFirstUser.setEnabled(true);
        introTwoBinding.creditScoreArcFirstUser.setClickable(false);
        introTwoBinding.creditScoreArcFirstUser.setFocusable(false);
        introTwoBinding.creditScoreArcFirstUser.setArcColor(getResources().getColor(R.color.transparent));
        introTwoBinding.creditScoreArcFirstUser.setOnTouchListener((v, event) -> true);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_intro_two;
    }

    @Override
    public IntroTwoContract.Presenter createPresenter() {
        return mPresenter;
    }


    private void displayStarWithAnimationForFirstTimeUser() {
        try {
            Glide.with(this).asGif().load(R.drawable.grey_stars).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    resource.setLoopCount(1);
                    return false;
                }
            }).into(introTwoBinding.starViewFirstTimeUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoadingAnimationForFirstTimeUser() {
        displayStarWithAnimationForFirstTimeUser();
        timerForFirstTimeUser = new Timer();
        timerForFirstTimeUser.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentPositionForFirstTimeUser = currentPositionForFirstTimeUser + 1;
                if (currentPositionForFirstTimeUser != MAXIMUM_SCORE - DEFAULT_SCORE) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        try {
                            introTwoBinding.creditScoreArcFirstUser.setProgress(currentPositionForFirstTimeUser);
                            introTwoBinding.creditScoreArcFirstUser.setProgressColor(getResources().getColor(R.color.star_unfilled_color));
                        } catch (Exception e) {
                            Timber.d("Exception : " + e.toString());
                        }

                    });
                } else {
                    if (timerForFirstTimeUser != null) {
                        timerForFirstTimeUser.cancel();
                        timerForFirstTimeUser.purge();
                        getActivity().runOnUiThread(() -> {
                            try {
                                introTwoBinding.creditScoreArcFirstUser.setmHideThumb(false);
                            } catch (Exception e) {
                                Timber.d("Exception : " + e.toString());
                            }
                        });

                    }
                }
            }
        }, 0, 2);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timerForFirstTimeUser != null) {
            timerForFirstTimeUser.cancel();
            timerForFirstTimeUser.purge();
        }
    }
}
