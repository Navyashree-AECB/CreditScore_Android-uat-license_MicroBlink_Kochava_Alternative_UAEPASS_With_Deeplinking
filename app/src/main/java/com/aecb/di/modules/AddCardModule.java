package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.purchasejourney.addcard.presenter.AddCardContract;
import com.aecb.ui.purchasejourney.addcard.presenter.AddCardPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class AddCardModule {
    @Provides
    @ActivityScope
    public AddCardContract.Presenter addCardPresenterImpl(AddCardPresenterImpl addCardPresenter) {
        return addCardPresenter;
    }
}
