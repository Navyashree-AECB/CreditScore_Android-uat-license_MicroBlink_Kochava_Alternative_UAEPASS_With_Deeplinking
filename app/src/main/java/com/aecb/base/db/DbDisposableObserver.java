package com.aecb.base.db;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public abstract class DbDisposableObserver<T> extends DisposableObserver {

    public void onError(Throwable throwable) {
        Timber.e("onError: %s", throwable.toString());
    }

    public void onComplete() {
        Timber.e("onComplete:");
    }

    public void onNext(Object response) {
        Timber.e("onNext:");
        onSuccess(response);
    }

    protected abstract void onSuccess(Object var1);
}