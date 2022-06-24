package com.aecb.ui.downloadpdf.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface DownloadPdfContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
        String getCurrentLanguage();
    }
}