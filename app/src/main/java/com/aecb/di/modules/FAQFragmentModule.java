package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.creditreport.faq.presenter.FAQContract;
import com.aecb.ui.creditreport.faq.presenter.FAQPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class FAQFragmentModule {

    @Provides
    @FragmentScope
    public FAQContract.Presenter faqPresenterImpl(FAQPresenterImpl faqPresenter) {
        return faqPresenter;
    }
}