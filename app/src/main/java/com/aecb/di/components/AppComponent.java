package com.aecb.di.components;

import android.app.Application;
import android.content.Context;

import com.aecb.App;
import com.aecb.data.DataManager;
import com.aecb.di.modules.app.AppModule;
import com.aecb.di.modules.app.DbModule;
import com.aecb.di.modules.app.NetworkModule;
import com.aecb.di.modules.app.ServiceModule;
import com.aecb.di.modules.contributor.ActivityContributorModule;
import com.aecb.di.modules.contributor.FragmentContributorModule;
import com.aecb.di.qualifiers.AppContext;
import com.aecb.di.scopes.AppScope;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@AppScope
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, NetworkModule.class,
        DbModule.class, ServiceModule.class, ActivityContributorModule.class,
        FragmentContributorModule.class})
public interface AppComponent extends AndroidInjector<App> {
    @AppContext
    Context appContext();

    DataManager getDataManager();

    void inject(App app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
