package com.aecb.di.modules.app;

import android.app.Application;
import android.content.Context;

import com.aecb.data.DataManager;
import com.aecb.data.DataManagerImpl;
import com.aecb.data.api.ApiHelper;
import com.aecb.data.api.ApiHelperImpl;
import com.aecb.data.db.DbHelper;
import com.aecb.data.db.DbHelperImpl;
import com.aecb.data.preference.PrefHelper;
import com.aecb.data.preference.PrefHelperImpl;
import com.aecb.di.scopes.AppScope;

import dagger.Binds;
import dagger.Module;

@Module(includes = {AppContextModule.class})
abstract public class AppModule {

    @Binds
    @AppScope
    abstract Context provideContext(Application application);

    @Binds
    @AppScope
    public abstract DataManager dataManager(DataManagerImpl dataManagerImpl);

    @Binds
    @AppScope
    public abstract PrefHelper preferenceHelper(PrefHelperImpl prefHelperImpl);

    @Binds
    @AppScope
    public abstract ApiHelper apiHelper(ApiHelperImpl apiHelperImpl);

    @Binds
    @AppScope
    public abstract DbHelper dbHelper(DbHelperImpl dbHelperImpl);
}
