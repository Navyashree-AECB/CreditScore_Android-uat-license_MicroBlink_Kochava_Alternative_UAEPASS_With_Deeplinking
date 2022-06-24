package com.aecb.ui.loginflow.uaepasspin.uaepass;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.aecb.R;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;

import java.util.Random;
import java.util.Set;

public class UaePassUtil {

    // For UAEPass
    public static final boolean IS_PRODUCTION = false;
    private String state = "";
//    public static final String UAEPASS_AUTHENTICATION_URL = IS_PRODUCTION ? "https://id.uaepass.ae/idshub/authorize" : "https://stg-id.uaepass.ae/idshub/authorize";
    public static final String UAEPASS_AUTHENTICATION_URL = IS_PRODUCTION ? "https://id.uaepass.ae/idshub/authorize" : "https://stg-id.uaepass.ae/idshub/authorize";
//    public static final String UAEPASS_TOKEN_URL = IS_PRODUCTION ? "https://id.uaepass.ae/trustedx-authserver/oauth/main-as/token" : "https://stg-id.uaepass.ae/idshub/token";
    public static final String UAEPASS_TOKEN_URL = IS_PRODUCTION ? "https://id.uaepass.ae/idshub/token" : "https://stg-id.uaepass.ae/idshub/token";
//    public static final String UAEPASS_PROFILE_URL = IS_PRODUCTION ? "https://id.uaepass.ae/trustedx-resources/openid/v1/users/me" : "https://stg-id.uaepass.ae/idshub/userinfo";
    public static final String UAEPASS_PROFILE_URL = IS_PRODUCTION ? "https://id.uaepass.ae/idshub/userinfo" : "https://stg-id.uaepass.ae/idshub/userinfo";
   /* public static final String UAE_PASS_CLIENT_ID = IS_PRODUCTION ?  "prod_client_id" : "aecb_mob_stage";
    public static final String UAE_PASS_CLIENT_SECRET = IS_PRODUCTION ? "prod_secret" : "QmUnvjrYOF9keAZX" ;*/
    public static final String UAE_PASS_CLIENT_ID = IS_PRODUCTION ?  "aecb_mob_prod" : "sandbox_stage";
    public static final String UAE_PASS_CLIENT_SECRET = IS_PRODUCTION ?  "Pn40ECUd8j3NDhQD" : "sandbox_stage" ;
    public static final String REDIRECT_URL = IS_PRODUCTION ?  "https://online.aecb.gov.ae/sso/logout?utm_source=uaepass" : "https://onlineuat.aecb.gov.ae/sso/logout?utm_source=uaepass";
    public static final String DOCUMENT_SIGNING_SCOPE = "urn:safelayer:eidas:sign:process:document";
    public static final String RESPONSE_TYPE = "code";
    public static final String SCOPE = "urn:uae:digitalid:profile:general";
    public static final String ACR_VALUES_MOBILE = "urn:digitalid:authentication:flow:mobileondevice";
    public static final String ACR_VALUES_WEB = "urn:safelayer:tws:policies:authentication:level:low";
    public static final String UAE_PASS_PACKAGE_ID = IS_PRODUCTION ? "ae.uaepass.mainapp" : "ae.uaepass.mainapp.stg";
    public static final String UAE_PASS_LOGOUT_URL = IS_PRODUCTION ? "https://id.uaepass.ae/trustedx-authserver/digitalid-idp/logout?redirect_uri="+REDIRECT_URL : "https://stg-id.uaepass.ae/trustedx-authserver/digitalid-idp/logout?redirect_uri="+REDIRECT_URL;

    private static final String SCHEME = "aecb";
    public static final int UAEPASS_AUTH_CALLBACK = 111;
    public static final int UAEPASS_PROFILE_CALLBACK = 222;

    public static Uri replaceUriParameter(Uri uri, String key, String newValue) {
        final Set<String> params = uri.getQueryParameterNames();
        final Uri.Builder newUri = uri.buildUpon().clearQuery();
        for (String param : params) {
            newUri.appendQueryParameter(param,
                    param.equals(key) ? newValue : uri.getQueryParameter(param));
        }

        return newUri.build();
    }

    public static Uri removeUriParameter(Uri uri, String key) {
        final Set<String> params = uri.getQueryParameterNames();
        final Uri.Builder newUri = uri.buildUpon().clearQuery();
        for (String param : params) {
            if (!param.equals(key)) {
                newUri.appendQueryParameter(param, uri.getQueryParameter(param));
            }
        }
        return newUri.build();
    }

    public static void openUAEPassAppInPlayStore(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=ae.uaepass.mainapp")));
        } catch (ActivityNotFoundException var3) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=ae.uaepass.mainapp")));
        }
    }

    public static String generateRandomString(int length) {
        StringBuilder salt = new StringBuilder();
        //Todo use SecureRandom
        Random random = new Random();

        while(salt.length() < length) {
            int index = (int)(random.nextFloat() * (float)"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".length());
            salt.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".charAt(index));
        }

        return salt.toString();
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void callUAEPassAccessToken(Context context, String code, UaePassCallback callback){

        String basicAuthCredentials = UAE_PASS_CLIENT_ID + ":" + UAE_PASS_CLIENT_SECRET;
        basicAuthCredentials =  "Basic " + Base64.encodeToString(basicAuthCredentials.getBytes(), 2);

        Ion.with(context)
                .load(UAEPASS_TOKEN_URL)
                .addHeader("Authorization",basicAuthCredentials)
                .setBodyParameter("grant_type", "authorization_code")
                .setBodyParameter("code", code)
                .setBodyParameter("redirect_uri",REDIRECT_URL+"")
                .asString()
                .withResponse()
                .setCallback((e, result) -> {
                    //hideProgress();
                    if(result != null && result.getHeaders().code() == 200){
                        String s = result.getResult();
                        if(s.contains("error")){
                            UaePassErrorModel errorModel = new Gson().fromJson(s, UaePassErrorModel.class);
                            if(errorModel != null){
                                Log.e("oidc_error", errorModel.getError()+"\n"+errorModel.getError_description());
                            }
                            callback.uaePassError(UAEPASS_AUTH_CALLBACK);
                        }else{
                            callback.uaePassResponse(UAEPASS_AUTH_CALLBACK, result.getResult());
                        }
                    }else{
                        callback.uaePassError(UAEPASS_AUTH_CALLBACK);
                    }

                });
    }

    public static void callUserProfile(Context context, String accessToken, UaePassCallback callback){
        //showProgress();
        Ion.with(context)
                .load(UAEPASS_PROFILE_URL)
                .addHeader("Authorization","Bearer "+accessToken)
                .asString()
                .withResponse()
                .setCallback((e, result) -> {
                    //hideProgress();
                    if(result != null && result.getHeaders().code() == 200){
                        String s = result.getResult();
                        if(s.contains("error")){
                            UaePassErrorModel errorModel = new Gson().fromJson(s, UaePassErrorModel.class);
                            if(errorModel != null){
                                Log.e("oidc_error", errorModel.getError()+"\n"+errorModel.getError_description());
                            }
                            //showErrorDialog(context, context.getString(R.string.smartpass_claims_error_msg), context.getString(R.string.error));
                            callback.uaePassError(UAEPASS_PROFILE_CALLBACK);
                        }else{
                            callback.uaePassResponse(UAEPASS_PROFILE_CALLBACK, result.getResult());
                        }
                    }else{
                        callback.uaePassError(UAEPASS_PROFILE_CALLBACK);
                    }

                });
    }

    public static void showErrorDialog(Context context, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(
                        context.getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.setView(null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String getQueryParameterValue(String url, String queryParameter) {
        Uri uri = Uri.parse(url);
        String value = uri.getQueryParameter(queryParameter);
        return value;
    }
}
