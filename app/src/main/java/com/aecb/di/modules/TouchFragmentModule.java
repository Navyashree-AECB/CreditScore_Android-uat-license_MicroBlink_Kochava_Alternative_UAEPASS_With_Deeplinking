package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.touchdialog.presenter.TouchContract;
import com.aecb.ui.touchdialog.presenter.TouchPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class TouchFragmentModule {

    @Provides
    @FragmentScope
    public TouchContract.Presenter touchPresenterImpl(TouchPresenterImpl touchPresenter) {
        return touchPresenter;
    }
}