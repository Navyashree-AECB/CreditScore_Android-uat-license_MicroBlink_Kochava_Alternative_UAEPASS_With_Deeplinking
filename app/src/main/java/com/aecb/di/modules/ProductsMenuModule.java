package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.productsmenu.presenter.ProductsMenuContract;
import com.aecb.ui.productsmenu.presenter.ProductsMenuPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ProductsMenuModule {

    @Provides
    @FragmentScope
    public ProductsMenuContract.Presenter productsMenuPresenter(ProductsMenuPresenterImpl productsMenuPresenter) {
        return productsMenuPresenter;
    }
}