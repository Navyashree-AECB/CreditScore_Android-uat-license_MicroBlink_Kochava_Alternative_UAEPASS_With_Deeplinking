package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.addcvv.presenter.AddCvvContract;
import com.aecb.ui.addcvv.presenter.AddCvvPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class AddCvvFragmentModule {

    @Provides
    @FragmentScope
    public AddCvvContract.Presenter addCvvPresenterImpl(AddCvvPresenterImpl addCvvPresenter) {
        return addCvvPresenter;
    }
}