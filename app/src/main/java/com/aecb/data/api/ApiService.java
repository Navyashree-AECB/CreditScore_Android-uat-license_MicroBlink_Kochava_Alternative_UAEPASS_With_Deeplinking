package com.aecb.data.api;

import com.aecb.data.api.models.edirham.CheckoutStatusRequest;
import com.aecb.data.api.models.edirham.CheckoutStatusResponse;
import com.aecb.data.api.models.edirham.PurchaseCheckoutRequest;
import com.aecb.data.api.models.edirham.PurchaseCheckoutResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("checkout/purchase")
    Observable<PurchaseCheckoutResponse> callPurchaseCheckout(@Body PurchaseCheckoutRequest purchaseCheckoutRequest);

    @POST("checkout/status")
    Observable<CheckoutStatusResponse> callCheckoutStatus(@Body CheckoutStatusRequest checkoutStatusRequest);
}
