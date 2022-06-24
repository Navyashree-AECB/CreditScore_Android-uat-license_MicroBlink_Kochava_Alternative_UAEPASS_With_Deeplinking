package com.aecb.base.mvp;

import com.aecb.listeners.DialogButtonClickListener;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface BaseView extends MvpView {
    void clientError(String errorMessage);

    void hideLoading();

    void connectionError(String errorMessage);

    void onTimeout();

    void serverError(String errorMessage);

    void unauthenticated();

    void unexpectedError(String errorMessage);

    void unexpectedError(RuntimeException exception);

    void showLoading(String message);

    void showErrorMsgFromApi(String title, String description, String yesButton, String noButton,
                             DialogButtonClickListener dialogButtonClickListener);

    void gotoAppUnderMaintain();

    void sessionTokenExpired();

    void localValidationError(String title, String desc);

    void showApiFailureError(String message,String status,String useCase);

    void showApiSuccessMessage(String message);

    void showApiSuccessMessage(String message,String status,String useCase);

}
