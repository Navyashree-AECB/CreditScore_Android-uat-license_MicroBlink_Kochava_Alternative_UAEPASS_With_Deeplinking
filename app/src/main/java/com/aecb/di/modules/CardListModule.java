package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.purchasejourney.addcard.presenter.AddCardContract;
import com.aecb.ui.purchasejourney.addcard.presenter.AddCardPresenterImpl;
import com.aecb.ui.purchasejourney.cardlist.presenter.CardListContract;
import com.aecb.ui.purchasejourney.cardlist.presenter.CardListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class CardListModule {
    @Provides
    @ActivityScope
    public CardListContract.Presenter cardListPresenterImpl(CardListPresenterImpl cardListPresenter) {
        return cardListPresenter;
    }
}
