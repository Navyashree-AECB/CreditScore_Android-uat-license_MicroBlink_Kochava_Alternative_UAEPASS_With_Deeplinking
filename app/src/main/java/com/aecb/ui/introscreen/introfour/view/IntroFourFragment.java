package com.aecb.ui.introscreen.introfour.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.FragmentIntroFourBinding;
import com.aecb.ui.introscreen.introfour.presenter.IntroFourContract;
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

import static com.aecb.AppConstants.ScoreLimits.HIGH;
import static com.aecb.AppConstants.ScoreLimits.LOW;
import static com.aecb.AppConstants.ScoreLimits.MEDIUM;
import static com.aecb.AppConstants.ScoreLimits.VERY_HIGH;
import static com.aecb.AppConstants.ScoreLimits.VERY_LOW;

public class IntroFourFragment extends BaseFragment<IntroFourContract.View, IntroFourContract.Presenter> implements
        IntroFourContract.View {

    @Inject
    public IntroFourContract.Presenter mPresenter;
    FragmentIntroFourBinding introFourBinding;
    private Timer timer;
    private int currentPosition = -1;
    private int DEFAULT_SCORE = 300;

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        introFourBinding = DataBindingUtil.inflate(
                inflater, getLayoutRes(), container, false);
        View view = introFourBinding.getRoot();
        initialSetup();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
           animateScore(720);
           displayStars(720);
        }
    }

    private void initialSetup() {
        introFourBinding.creditScoreArc.setEnabled(true);
        introFourBinding.creditScoreArc.setClickable(false);
        introFourBinding.creditScoreArc.setFocusable(false);
        introFourBinding.creditScoreArc.setOnTouchListener((v, event) -> true);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_intro_four;
    }

    @Override
    public IntroFourContract.Presenter createPresenter() {
        return mPresenter;
    }

    private void animateScore(int score) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentPosition = currentPosition + 1;
                if (currentPosition <= score - DEFAULT_SCORE) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        try {
                            introFourBinding.creditScoreArc.setProgress(currentPosition);
                            introFourBinding.tvCreditScore.setText(String.valueOf(currentPosition + DEFAULT_SCORE));
                            displayStarAndProgressColor(currentPosition + DEFAULT_SCORE);
                        } catch (Exception e) {
                            Timber.d("Exception : " + e.toString());
                        }

                    });
                } else {
                    timer.cancel();
                    timer.purge();
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        introFourBinding.tvCreditScore.setText(String.valueOf(score));
                        displayStarAndProgressColor(score);
                    });
                }
            }
        }, 0, 3);
    }

    private void displayStarAndProgressColor(int score) {
        if (getActivity() == null) return;
        if (score <= VERY_LOW) {
            introFourBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.red_progress_color));
        } else if (score <= LOW) {
            introFourBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.orange_progress_color));
        } else if (score <= MEDIUM) {
            introFourBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.yellow_progress_color));
        } else if (score <= HIGH) {
            introFourBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.light_green_progress_color));
        } else if (score <= VERY_HIGH) {
            introFourBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.green_progress_color));
        }
    }

    private void displayStars(int score) {
        if (score <= VERY_LOW) {
            displayStarWithAnimation(R.drawable.star_one);
        } else if (score <= LOW) {
            displayStarWithAnimation(R.drawable.star_two);
        } else if (score <= MEDIUM) {
            displayStarWithAnimation(R.drawable.star_three);
        } else if (score <= HIGH) {
            displayStarWithAnimation(R.drawable.star_four_fast);
        } else if (score <= VERY_HIGH) {
            displayStarWithAnimation(R.drawable.star_five);
        }
    }

    private void displayStarWithAnimation(int starGif) {
        try {
            Glide.with(this).asGif().load(starGif).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    resource.setLoopCount(1);
                    return false;
                }
            }).into(introFourBinding.starView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
