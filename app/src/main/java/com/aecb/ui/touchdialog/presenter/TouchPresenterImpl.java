package com.aecb.ui.touchdialog.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.util.ValidationUtil;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

public class TouchPresenterImpl extends MvpBasePresenterImpl<TouchContract.View>
        implements TouchContract.Presenter {
    @Inject
    public TouchPresenterImpl() {
    }

    @Override
    public void checkPasswordValidation(String password) {
        if (validation(password)) {
            ifViewAttached(view -> {
                view.callLoginApi(password);
            });
        }

    }

    private boolean validation(String password) {
        AtomicBoolean valid = new AtomicBoolean(true);
        ifViewAttached(view -> {
            if (ValidationUtil.isNullOrEmpty(password)) {
                view.showEmptyPasswordError();
                valid.set(false);
            }
        });
        return valid.get();
    }
}