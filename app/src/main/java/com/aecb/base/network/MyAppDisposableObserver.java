package com.aecb.base.network;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.MessageBase;
import com.aecb.data.api.models.settings.ApplicationCodesItem;
import com.aecb.data.db.repository.commonresponse.DbResponse;
import com.aecb.util.varibles.StringConstants;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import timber.log.Timber;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.OK;
import static com.aecb.util.Utilities.getStringFromErrorBody;
import static com.aecb.util.varibles.StringConstants.CONNECTION_FAILED;
import static com.aecb.util.varibles.StringConstants.INTERNAL_SERVER_ERROR;
import static com.aecb.util.varibles.StringConstants.INVALID_SESSION_TOKEN;
import static com.aecb.util.varibles.StringConstants.SESSION_TOKEN_EXPIRED;

public abstract class MyAppDisposableObserver<T> extends DisposableObserver {

    private final BaseView view;
    private String userCase;
    private MyAppWrappedError wrappedError;

    public MyAppDisposableObserver(BaseView view) {
        this.view = view;
    }

    public MyAppDisposableObserver(BaseView view, String userCase) {
        this.view = view;
        this.userCase = userCase;
    }

    public void onError(Throwable throwable) {
        if (view != null) {
            view.hideLoading();
        }
        Timber.e("onError: %s", throwable.toString());
        if (throwable instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
            String errorString = null;
            try {
                errorString = getStringFromErrorBody(responseBody.bytes()).toString();
                wrappedError = (new Gson()).fromJson(errorString, MyAppWrappedError.class);
                wrappedError.setError(errorString);
                int code = ((HttpException) throwable).code();
                ApplicationCodesItem applicationCodeItem = getErrorDetails(wrappedError.status);
                if (code == 401) {
                    assert view != null;
                    view.unauthenticated();
                } else if (applicationCodeItem.getCode() != null) {
                    //ToDo remove when error code stored in database
                    if (/*App.appComponent.getDataManager().getAppLanguage()*/"".equals(ISO_CODE_ARABIC)) {
                        assert view != null;
                        view.showErrorMsgFromApi("", applicationCodeItem.getMessageAr(), OK, null, null);
                    } else {
                        assert view != null;
                        view.showErrorMsgFromApi("", applicationCodeItem.getMessageEn(), OK, null, null);
                    }
                } else {
                    if (wrappedError.getError().contains(SESSION_TOKEN_EXPIRED) ||
                            wrappedError.getError().contains(INVALID_SESSION_TOKEN)) {
                        assert view != null;
                        view.sessionTokenExpired();
                        return;
                    } else if (code >= 400 && code <= 499) {
                        assert view != null;
                        String messageToDisplay = "";
                        String messageData = new Gson().toJson(wrappedError.getMessage());
                        String messageString = null;
                        try {
                            messageString = new Gson().fromJson(messageData, String.class);
                        } catch (Exception e) {
                            Timber.d("Exception : " + e.toString());
                        }
                        if (messageString != null) {
                            messageToDisplay = messageString;
                        } else {
                            MessageBase messageBase = new Gson().fromJson(messageData, MessageBase.class);
                            messageToDisplay = messageBase.getMessage();
                        }
                        view.showApiFailureError(messageToDisplay, "", "");
                        return;
                    }
                    if (code == 500 && wrappedError.getError().contains(CONNECTION_FAILED)) {
                        assert view != null;
                        view.gotoAppUnderMaintain();
                        return;
                    }
                    if (code == 500 && wrappedError.getError().contains(INTERNAL_SERVER_ERROR)) {
                        assert view != null;
                        if (App.getAppComponent().getDataManager().getCurrentLanguage().equals(ISO_CODE_ARABIC)) {
                            view.showApiFailureError(StringConstants.INTERNAL_ERROR_MSG_AR, "", "");
                        } else {
                            view.showApiFailureError(StringConstants.INTERNAL_ERROR_MSG_EN, "", "");
                        }
                        return;
                    }
                    if (code == 500 && wrappedError.getError().contains("Payment declined")) {
                        assert view != null;
                        if (App.getAppComponent().getDataManager().getCurrentLanguage().equals(ISO_CODE_ARABIC)) {
                            view.showApiFailureError(StringConstants.PAYMENT_DECLINED_AR, "", "");
                        } else {
                            view.showApiFailureError(StringConstants.PAYMENT_DECLINED_EN, "", "");
                        }
                        return;
                    }
                    if (code == 500) {
                        assert view != null;
                        String messageToDisplay = "";
                        String messageData = new Gson().toJson(wrappedError.getMessage());
                        String status = new Gson().toJson(wrappedError.getStatus());
                        String messageString = null;
                        try {
                            messageString = new Gson().fromJson(messageData, String.class);
                        } catch (Exception e) {
                            Timber.d("Exception : " + e.toString());
                        }
                        if (messageString != null) {
                            messageToDisplay = messageString;
                        } else {
                            MessageBase messageBase = new Gson().fromJson(messageData, MessageBase.class);
                            messageToDisplay = messageBase.getMessage();
                        }
                        view.showApiFailureError(messageToDisplay, status, userCase);
                        return;
                    }
                    if (code >= 501 && code <= 599) {
                        try {
                            errorString = responseBody.string();
                            if (!TextUtils.isEmpty(errorString)) {
                                if (view != null && !TextUtils.isEmpty(wrappedError.getStatus())
                                        && wrappedError.getStatus().equalsIgnoreCase(
                                        AppConstants.ApiParameter.CONST_ADD)
                                        && wrappedError.getStatus().equalsIgnoreCase(
                                        AppConstants.ApiParameter.STATUS_209)
                                        && !wrappedError.isSuccess()) {
                                    view.unauthenticated();
                                } else {
                                    if (view != null) {
                                        view.showApiFailureError(throwable.toString(), "", "");
                                    }
                                }
                            } else {
                                if (view != null) {
                                    view.showApiFailureError(throwable.toString(), "", "");
                                }
                            }
                        } catch (IOException e) {
                            Timber.d("Exception : " + e.toString());
                            if (view != null) {
                                view.showApiFailureError(throwable.toString(), "", "");
                            }
                        }
                        return;
                    }
                    assert view != null;
                    view.showApiFailureError(throwable.toString(), "", "");
                }
            } catch (Exception e) {
                Timber.d("Exception : " + e.toString());
            }

        } else if (throwable instanceof ConnectException) {
            assert view != null;
            view.showApiFailureError(throwable.toString(), "", "");
        } else if (throwable instanceof SocketTimeoutException) {
            assert view != null;
            view.onTimeout();
        } else if (throwable instanceof SSLHandshakeException) {
            assert view != null;
            Timber.d("throwable code== " + throwable.getMessage());
            try {
                view.connectionError(throwable.getMessage());
            } catch (Exception e) {
                Timber.d("Exception : " + e.toString());
            }
        } else if (throwable instanceof IOException) {
            assert view != null;
            Timber.d("throwable code== " + throwable.getMessage());
            try {
                if (throwable.getMessage().contains("Read error")) {
                    view.gotoAppUnderMaintain();
                } else {
                    view.showApiFailureError(throwable.toString(), "", "");
                }
            } catch (Exception e) {
                Timber.d("Exception : " + e.toString());
            }

        } else {
            assert view != null;
            view.showApiFailureError(throwable.toString(), "", "");
        }

    }

    private ApplicationCodesItem getErrorDetails(String code) {
        final ApplicationCodesItem[] applicationCodeItem = {new ApplicationCodesItem()};
        DbDisposableObserver<DbResponse> observer = new DbDisposableObserver<DbResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onSuccess(Object var1) {
                if (var1 instanceof DbResponse) {
                    if (((DbResponse) var1).getConfigurationData() != null) {
                        if (((DbResponse) var1).getConfigurationData().getApplicationCodes() != null) {
                            List<ApplicationCodesItem> list = ((DbResponse) var1).getConfigurationData().getApplicationCodes();
                            for (ApplicationCodesItem item : list) {
                                try {
                                    if (item.getCode().equals(code)) {
                                        applicationCodeItem[0] = item;
                                    }
                                } catch (NumberFormatException efe) {
                                    Timber.d("Exception : " + efe.toString());
                                }
                            }
                        }
                    }
                }
            }
        };
        //ToDo remove when error code stored in database
        /*App.appComponent.getDataManager()
                .getResponseFromTable(TablesIds.CONST_SETTINGS_TABLE_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);*/

        return applicationCodeItem[0];
    }

    public void onComplete() {
        Timber.d("onComplete:");
    }

    public void onNext(Object response) {
        Timber.d("onNext:");
        this.onSuccess(response);
    }

    protected abstract void onSuccess(Object response);
}