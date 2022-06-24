package com.aecb.ui.pdf.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface PdfContract {
    interface View extends BaseView {
        void displayPDF(byte[] theByteArray);
    }

    interface Presenter extends BasePresenter<View> {

        void getFile(String reportID);
    }
}