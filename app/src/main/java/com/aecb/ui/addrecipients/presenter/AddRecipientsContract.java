package com.aecb.ui.addrecipients.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.cards.CreditCardData;

import java.util.List;

public interface AddRecipientsContract {
    interface View extends BaseView {
        void setCardList(List<CreditCardData> list);
    }

    interface Presenter extends BasePresenter<View> {
        String getCurrentLanguage();
    }
}