package com.aecb.ui.edirhamwebview.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface EDirhamWebviewContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
        String getCurrentLanguage();
    }
}
