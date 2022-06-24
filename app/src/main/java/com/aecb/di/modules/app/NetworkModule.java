package com.aecb.di.modules.app;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.BuildConfig;
import com.aecb.R;
import com.aecb.data.api.MyAppService;
import com.aecb.data.preference.PrefHelperImpl;
import com.aecb.di.qualifiers.AppContext;
import com.aecb.di.qualifiers.MyAppInterceptor;
import com.aecb.di.qualifiers.MyAppRetrofit;
import com.aecb.di.scopes.AppScope;
import com.aecb.util.Tls12SocketFactory;
import com.aecb.util.ValidationUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module(includes = {AppContextModule.class, AppModule.class})
public class
NetworkModule {

    private static SSLContext createCertificate(InputStream trustedCertificateIS) throws CertificateException, IOException, KeyStoreException, KeyManagementException, NoSuchAlgorithmException {

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        try {
            ca = cf.generateCertificate(trustedCertificateIS);
        } finally {
            trustedCertificateIS.close();
        }

        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

    private static X509TrustManager systemDefaultTrustManager() {

        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }

    }

    @Provides
    @AppScope
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        if (BuildConfig.BUILD_TYPE.equals("release") || BuildConfig.BUILD_TYPE.equals("uat") ||
                BuildConfig.BUILD_TYPE.equals("prod")) {
            return (new HttpLoggingInterceptor()).setLevel(Level.NONE);
        } else {
            return (new HttpLoggingInterceptor()).setLevel(Level.BODY);
        }
    }

    @Provides
    @AppScope
    public StethoInterceptor stethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @AppScope
    @MyAppInterceptor
    public Interceptor myAppInterceptor(PrefHelperImpl prefHelper) {
        return chain -> {
            String sessionDetails = App.getAppComponent().getDataManager().getSessionToken();
            String sessionToken = "";
            Request request = chain.request();
            if (!ValidationUtil.isNullOrEmpty(request.header("refId"))
                    && request.header("refId").equalsIgnoreCase("true")) {
                request = request.newBuilder()
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("Accept", "application/json")
                        .addHeader("x-api-key", "ca54c0c5-6878-4244-aa31-c87e9ab4d9e7")
                        .addHeader("x-app-key", "000")
                        .build();
            } else {
                sessionToken = sessionDetails;
                request = request.newBuilder()
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("Accept", "application/json")
                        .addHeader("X-Session-Token", sessionToken)
                        .addHeader("x-api-key", "ca54c0c5-6878-4244-aa31-c87e9ab4d9e7")
                        .addHeader("x-app-key", "000")
                        .build();
            }
            return chain.proceed(request);
        };
    }

    @Provides
    @AppScope
    public Cache httpCache(@AppContext Context context) {
        return new Cache(context.getCacheDir(), AppConstants.CACHE_SIZE);
    }

    @Provides
    @AppScope
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        return gsonBuilder.create();
    }

    @Provides
    @AppScope
    public OkHttpClient okhttpClient(Cache cache, HttpLoggingInterceptor httpLoggingInterceptor,
                                     StethoInterceptor stethoInterceptor, @MyAppInterceptor Interceptor myAppInterceptor) {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        SSLSocketFactory sslSocketFactory = null;
        // Install the all-trusting trust manager
        try {
            final SSLContext sslContext = createCertificate(App.getAppComponent().appContext().getResources().openRawResource(R.raw.outaecb));
          /*  final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());*/
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException | CertificateException | KeyStoreException | IOException e) {
            Timber.d("Exception : " + e.toString());
        }


        Builder builder = (new Builder())
                .readTimeout(AppConstants.API_TIMEOUT_TIME, TimeUnit.SECONDS)
                .connectTimeout(AppConstants.API_TIMEOUT_TIME, TimeUnit.SECONDS)
                .addInterceptor(myAppInterceptor);

        // builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.sslSocketFactory(sslSocketFactory, systemDefaultTrustManager());
        builder.hostnameVerifier((hostname, session) -> true);
        builder = enableTls12OnPreLollipop(builder);
        builder.addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(stethoInterceptor);
        return builder.cache(cache).build();
    }

    @Provides
    @AppScope
    @MyAppRetrofit
    public Retrofit myAppRetrofit(OkHttpClient okHttpClient) {
        return (new Retrofit.Builder())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).baseUrl(BuildConfig.API_BASE_URL)
                .build();
    }

    @Provides
    @AppScope
    public MyAppService myAppApiInterface(@MyAppRetrofit Retrofit retrofit) {
        return retrofit.create(MyAppService.class);
    }

    private OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 18 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2).build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }
        return client;

    }
}