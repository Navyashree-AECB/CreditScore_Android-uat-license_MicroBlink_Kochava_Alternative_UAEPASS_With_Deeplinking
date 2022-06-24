package com.aecb.base.mvp;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface BasePresenter<T extends MvpView> extends MvpPresenter<T> {
}
