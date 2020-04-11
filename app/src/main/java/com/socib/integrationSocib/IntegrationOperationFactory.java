package com.socib.integrationSocib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntegrationOperationFactory {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://api.socib.es/";

    public static Retrofit getAdapter() {
       if (retrofit == null) {
           Gson gson = new GsonBuilder()
                   .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                   .create();

           retrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create(gson))
                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                   .build();
       }
       return retrofit;
    }

    public static Retrofit getMockAdapter(String response) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MockInterceptor(response))
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://www.mock.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
