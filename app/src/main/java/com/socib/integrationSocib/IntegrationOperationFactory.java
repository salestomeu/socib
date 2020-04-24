package com.socib.integrationSocib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.socib.SocibApplication;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntegrationOperationFactory {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://api.socib.es/";
    private static final long cacheSize = (5 * 1024 * 1024);

    public static Retrofit getAdapter() {
       if (retrofit == null) {
           File httpCacheDir = new File(SocibApplication.getContext().getCacheDir(), "response");
           Cache myCache = new Cache(httpCacheDir, cacheSize);
           CacheInterceptor cacheInterceptor = new CacheInterceptor();
           Gson gson = new GsonBuilder()
                   .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                   .setLenient()
                   .create();

           OkHttpClient okHttpClient = new OkHttpClient.Builder()
                   .cache(myCache)
                   .addInterceptor(cacheInterceptor)
                   .build();

           retrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create(gson))
                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                   .client(okHttpClient)
                   .build();
       }
       return retrofit;
    }


}
