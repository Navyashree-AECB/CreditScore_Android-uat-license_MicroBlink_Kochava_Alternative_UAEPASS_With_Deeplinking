package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.edirhamwebview.presenter.EDirhamWebviewContract;
import com.aecb.ui.edirhamwebview.presenter.EDirhamWebviewPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class EDirhamWebviewModule {

    @Provides
    @ActivityScope
    public EDirhamWebviewContract.Presenter eDirhamPresenter(EDirhamWebviewPresenterImpl eDirhamPresenter) {
        return eDirhamPresenter;
    }
}
