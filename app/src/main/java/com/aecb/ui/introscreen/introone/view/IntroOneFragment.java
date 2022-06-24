package com.aecb.ui.introscreen.introone.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.databinding.FragmentIntroOneBinding;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.introscreen.introone.presenter.IntroOneContract;

import javax.inject.Inject;


public class IntroOneFragment extends BaseFragment<IntroOneContract.View, IntroOneContract.Presenter> implements
        IntroOneContract.View {

    @Inject
    public IntroOneContract.Presenter mPresenter;
    FragmentIntroOneBinding introOneBinding;

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        introOneBinding = DataBindingUtil.inflate(
                inflater, getLayoutRes(), container, false);
        View view = introOneBinding.getRoot();
        return view;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_intro_one;
    }

    @Override
    public IntroOneContract.Presenter createPresenter() {
        return mPresenter;
    }
}
