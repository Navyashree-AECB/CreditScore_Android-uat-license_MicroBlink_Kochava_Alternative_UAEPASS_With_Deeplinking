package com.aecb.ui.pdf.presenter;

import android.util.Base64;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.api.models.getfile.GetFileResponse;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PdfPresenterImpl extends MvpBasePresenterImpl<PdfContract.View> implements PdfContract.Presenter {

    @Inject
    public PdfPresenterImpl() {
    }

    @Override
    public void getFile(String reportID) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<GetFileResponse> observer = new MyAppDisposableObserver<GetFileResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    GetFileResponse getFileResponse = (GetFileResponse) response;
                    byte[] theByteArray = getFileResponse.getData().getFile().getDocumentbody().getBytes();
                    theByteArray = Base64.decode(theByteArray, Base64.DEFAULT);
                    view.hideLoading();
                    view.displayPDF(theByteArray);
                }
            };
            MyAppDisposableObserver<GetFileResponse> disposableObserver =
                    dataManager.getFile(reportID, 2)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);
        });
    }

}