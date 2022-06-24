package com.aecb.ui.introscreen.introthree.view;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.ui.introscreen.introthree.presenter.IntroThreeContract;

import javax.inject.Inject;

public class IntroThreeFragment extends BaseFragment<IntroThreeContract.View, IntroThreeContract.Presenter> implements
        IntroThreeContract.View {

    @Inject
    public IntroThreeContract.Presenter mPresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_intro_three;
    }

    @Override
    public IntroThreeContract.Presenter createPresenter() {
        return mPresenter;
    }
}
