package com.aecb.ui.changepassword.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface ChangePasswordContract {
    interface View extends BaseView {
        void showEmptyPasswordError();

        void showInvalidPasswordError();

        void showEmptyConfirmPasswordError();

        void showPasswordNotMatchedError();

        void showErrorMSg(String message);

        void showCurrentPasswordError();

        void showInvalidCurrentPasswordError();

        void openLogin(String message, String status, String useCase);

        void showPasswordSizeError();
    }

    interface Presenter extends BasePresenter<View> {
        void changePassword(String toString, String toString1, String toString2);
    }
}