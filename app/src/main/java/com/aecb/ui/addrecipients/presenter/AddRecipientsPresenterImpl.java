package com.aecb.ui.addrecipients.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.cards.CreditCardList;
import com.aecb.util.AESEncryption;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

import static com.aecb.util.Utilities.fullString;
import static com.aecb.util.Utilities.getStringFromErrorBody;

public class AddRecipientsPresenterImpl extends MvpBasePresenterImpl<AddRecipientsContract.View>
        implements AddRecipientsContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public AddRecipientsPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public String getCurrentLanguage() {
        return mDataManager.getCurrentLanguage();
    }
}