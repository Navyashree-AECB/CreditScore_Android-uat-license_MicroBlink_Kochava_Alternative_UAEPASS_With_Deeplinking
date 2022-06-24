package com.aecb;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.aecb.di.components.AppComponent;
import com.aecb.di.components.DaggerAppComponent;
import com.aecb.microblink.result.extract.BaseResultExtractorFactory;
import com.aecb.microblink.result.extract.ResultExtractorFactoryProvider;
import com.aecb.microblink.result.extract.blinkid.BlinkIdResultExtractorFactory;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.util.AppSignatureHelper;
import com.aecb.util.NotificationUtils;
import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kochava.base.Tracker;
import com.microblink.MicroblinkSDK;
import com.microblink.intent.IntentDataTransferMode;
import com.microblink.util.RecognizerCompatibility;
import com.microblink.util.RecognizerCompatibilityStatus;
import com.tumblr.remember.Remember;

import java.io.File;
import java.io.IOException;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

import static com.aecb.services.MyFirebaseInstanceIDService.onTokenRefresh;

//import net.danlew.android.joda.JodaTimeAndroid;

public class App extends DaggerApplication {

    public static AppComponent appComponent;
    public static FirebaseAnalytics mFirebaseAnalytics;
    public static Context mContext;
    private static GoogleAnalytics sAnalytics;
    private static boolean isRecognitionSupported = true;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static boolean isRecognitionSupported() {
        return isRecognitionSupported;
    }

    public static FirebaseAnalytics getFirebaseAnalytics() {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        }
        return mFirebaseAnalytics;
    }

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        setDefaultFont();
        initRemember();
        initBuildTypeData();
//        initJodaTime();
        initKochava();
        initFirebase();
        initGoogleAnalytics();
        initNotificationUtils();
//        if (!BuildConfig.BUILD_TYPE.equalsIgnoreCase("dev")) initMicroBlink();
        onTokenRefresh();
        SmartAsyncPolicyHolder.INSTANCE.init(appComponent.appContext());
        AppSignatureHelper appSignature = new AppSignatureHelper(appComponent.appContext());
        try {
            String token = appSignature.getAppSignatures().get(0);
            Timber.d("smstoken : " + token);
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }
        //saveLogs();
    }

    /*private void initMicroBlink() {
        RecognizerCompatibilityStatus supportStatus = RecognizerCompatibility.getRecognizerCompatibilityStatus(this);
        if (supportStatus == RecognizerCompatibilityStatus.RECOGNIZER_SUPPORTED) {
            Timber.i("Recognition is supported!");
        } else if (supportStatus == RecognizerCompatibilityStatus.NO_CAMERA) {
            Toast.makeText(this, getString(R.string.recognition_not_supported), Toast.LENGTH_SHORT).show();
            Timber.w("Recognition is supported only in DirectAPI mode!");
        } else {
            isRecognitionSupported = false;
            Toast.makeText(this, getString(R.string.recognition_not_supported_reason) + supportStatus.name(), Toast.LENGTH_LONG).show();
            Timber.e("Recognition is not supported! Reason: {}", supportStatus.name());
        }

        if (isRecognitionSupported) {
            MicroblinkSDK.setLicenseFile(getLicenceFilePath(), this);
            MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED);
        }

        ResultExtractorFactoryProvider.set(createResultExtractorFactory());
    }
*/
    private void initKochava() {
        // Start the Kochava Tracker

        if (BuildConfig.BUILD_TYPE.equals("release") || BuildConfig.BUILD_TYPE.equals("uat") ||
                BuildConfig.BUILD_TYPE.equals("dev")) {
            Tracker.configure(new Tracker.Configuration(getApplicationContext())
                    .setAppGuid("kocreditreport-android-uat-ijk5hdp6")
            );
        } else {
            Tracker.configure(new Tracker.Configuration(getApplicationContext())
                    .setAppGuid("kocreditreport-android-prod-offnna")
            );
        }
    }

    /*protected BaseResultExtractorFactory createResultExtractorFactory() {
        return new BlinkIdResultExtractorFactory();
    }*/

   /* protected String getLicenceFilePath() {
        String license = "";
        MicroblinkSDK.setShowTrialLicenseWarning(false);
        switch (BuildConfig.BUILD_TYPE) {
            case AppConstants.ApkBuildType.UAT:
                license = "MB_com.aecb.uat_BlinkID-Android-2022-04-20.key";
                break;
            case AppConstants.ApkBuildType.DEV: {
                license = "MB_com.aecb.dev_BlinkID_Android_2020-12-26.mblic";
                break;
            }
            case AppConstants.ApkBuildType.PROD:
                license = "MB_com.aecb.app_BlinkID_Android_2021-04-09.mblic";
            default:
        }
        return license;
    }*/

    private void initNotificationUtils() {
        new NotificationUtils(appComponent.appContext());
    }

//    private void initJodaTime() {
//        JodaTimeAndroid.init(appComponent.appContext());
//    }

    private void initBuildTypeData() {
        switch (BuildConfig.BUILD_TYPE) {
            case AppConstants.ApkBuildType.UAT:
            case AppConstants.ApkBuildType.PROD: {
                //plantTimber();
                break;
            }
            case AppConstants.ApkBuildType.DEV: {
                plantTimber();
                initStetho();
                break;
            }
            default:
        }
    }

    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    private void initRemember() {
        Remember.init(appComponent.appContext(), this.getString(R.string.app_name));
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void plantTimber() {
        Timber.uprootAll();
        Timber.plant((new DebugTree()));
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
    }

    private void initGoogleAnalytics() {
        sAnalytics = GoogleAnalytics.getInstance(this);
        sAnalytics.setDryRun(true);
        sAnalytics.setAppOptOut(true);
    }

    private void setDefaultFont() {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath(getString(R.string.font_helvetica_ce_regular))
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

    }

    private void saveLogs() {
        if (isExternalStorageWritable()) {

            File appDirectory = new File(Environment.getExternalStorageDirectory() + "/AECBLogs");
            File logDirectory = new File(appDirectory + "/log");
            File logFile = new File(logDirectory, "logcat" + System.currentTimeMillis() + ".txt");

            // create app folder
            if (!appDirectory.exists()) {
                appDirectory.mkdir();
            }

            // create log folder
            if (!logDirectory.exists()) {
                logDirectory.mkdir();
            }

            // clear the previous logcat and then write the new one to the file
            try {
                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -f " + logFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (isExternalStorageReadable()) {
            // only readable
        } else {
            // not accessible
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}