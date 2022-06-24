package com.aecb.ui.downloadpdf.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;

import javax.inject.Inject;

public class DownloadPdfPresenterImpl extends MvpBasePresenterImpl<DownloadPdfContract.View>
        implements DownloadPdfContract.Presenter {

    @Inject
    public DownloadPdfPresenterImpl() {
    }

    @Override
    public String getCurrentLanguage() {
        return dataManager.getCurrentLanguage();
    }
}