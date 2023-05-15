package com.ominfo.staff_original.network;

import static com.ominfo.staff_original.network.NetworkURLs.BASE_URL;

import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.utility.ApplicationMode;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroNetworkModule {

    private static RetroNetworkModule sInstance = null;
    private final NetworkAPIServices mAPI;

    private RetroNetworkModule() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (LogUtil.isEnableLogs) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(interceptor);
            httpClient.connectTimeout(90, TimeUnit.SECONDS);
            httpClient.readTimeout(90, TimeUnit.SECONDS);
            httpClient.writeTimeout(90, TimeUnit.SECONDS);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        mAPI = retrofit.create(NetworkAPIServices.class);
    }

    public static synchronized RetroNetworkModule getInstance() {
        if (sInstance == null) {
            sInstance = new RetroNetworkModule();
        }
        return sInstance;
    }

    public NetworkAPIServices getAPI() {
        return mAPI;
    }
}
