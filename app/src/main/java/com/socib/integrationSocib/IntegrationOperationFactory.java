package com.socib.integrationSocib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.function.Function;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntegrationOperationFactory {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://api.socib.es/";

    public static Retrofit getAdapter() {
       if (retrofit == null) {
           Gson gson = new GsonBuilder()
                   .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                   .setLenient()
                   .create();

           retrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create(gson))
                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                   .build();
       }
       return retrofit;
    }

    public static Retrofit getMockAdapter(Function<Request, String> responseByRequest) {
        return IntegrationOperationFactory.getMockAdapter(new MockInterceptor(responseByRequest));
    }

    public static Retrofit getMockAdapter(String response) {
        return IntegrationOperationFactory.getMockAdapter(new MockInterceptor(response));
    }

    private static Retrofit getMockAdapter(MockInterceptor mockInterceptor) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mockInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://www.mock.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
