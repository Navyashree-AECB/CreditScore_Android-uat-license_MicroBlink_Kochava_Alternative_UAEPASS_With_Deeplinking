package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.pdf.presenter.PdfContract;
import com.aecb.ui.pdf.presenter.PdfPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class PdfViewModule {

    @Provides
    @ActivityScope
    public PdfContract.Presenter pdfViewPresenter(PdfPresenterImpl pdfPresenter) {
        return pdfPresenter;
    }
}