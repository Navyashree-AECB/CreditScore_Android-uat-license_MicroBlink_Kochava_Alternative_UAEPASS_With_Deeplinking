package com.aecb.util;

import com.aecb.BuildConfig;
import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.serverside.CustomData;
import com.facebook.ads.sdk.serverside.Event;
import com.facebook.ads.sdk.serverside.EventRequest;
import com.facebook.ads.sdk.serverside.EventResponse;
import com.facebook.ads.sdk.serverside.UserData;

import static com.aecb.AppConstants.ConversionApi.ACCESS_TOKEN;
import static com.aecb.AppConstants.ConversionApi.PIXEL_ID;

public class ConversionApi {

    public static void callConversionApi(CustomData customData, String eventName) {
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("prod") ||
                BuildConfig.BUILD_TYPE.equalsIgnoreCase("uat")) {
            APIContext context = new APIContext(ACCESS_TOKEN).enableDebug(true);
            context.setLogger(System.out);

            UserData userData = new UserData()
                    .fbc("fb.1.1554763741205.AbCdEfGhIjKlMnOpQrStUvWxYz1234567890")
                    .fbp("fb.1.1558571054389.1098115397")
                    .email("test@test.com");

            Event pageViewEvent = new Event();
            pageViewEvent.eventName(eventName)
                    .eventTime(System.currentTimeMillis() / 1000L)
                    .userData(userData)
                    .customData(customData);

            EventRequest eventRequest = new EventRequest(PIXEL_ID, context);
            eventRequest.addDataItem(pageViewEvent);

            Thread thread = new Thread(() -> {
                try {
                    EventResponse response = eventRequest.execute();
                    System.out.println(String.format("Standard API response : %s ", response));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}
