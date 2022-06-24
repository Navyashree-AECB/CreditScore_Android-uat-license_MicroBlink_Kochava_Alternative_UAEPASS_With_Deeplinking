package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.downloadpdf.presenter.DownloadPdfContract;
import com.aecb.ui.downloadpdf.presenter.DownloadPdfPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class DownloadPdfModule {

    @Provides
    @FragmentScope
    public DownloadPdfContract.Presenter downloadPdfPresenter(DownloadPdfPresenterImpl downloadPdfPresenter) {
        return downloadPdfPresenter;
    }
}