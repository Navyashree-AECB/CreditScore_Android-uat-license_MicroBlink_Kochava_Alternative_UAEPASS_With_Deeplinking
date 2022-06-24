package com.aecb.ui.touchdialog.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface TouchContract {

    interface View extends BaseView {
        void showEmptyPasswordError();

        void callLoginApi(String password);

        void showInvalidPassword();

        void showPasswordLengthError();
    }

    interface Presenter extends BasePresenter<View> {
        void checkPasswordValidation(String password);
    }
}