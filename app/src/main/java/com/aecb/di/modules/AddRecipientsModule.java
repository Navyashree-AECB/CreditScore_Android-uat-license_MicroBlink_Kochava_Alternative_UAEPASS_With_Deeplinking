package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.addrecipients.presenter.AddRecipientsContract;
import com.aecb.ui.addrecipients.presenter.AddRecipientsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class AddRecipientsModule {

    @Provides
    @ActivityScope
    public AddRecipientsContract.Presenter addRecipientsPresenter(AddRecipientsPresenterImpl addRecipientsPresenter) {
        return addRecipientsPresenter;
    }
}