package com.socib.integrationSocib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.function.Function;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntegrationOperationFactoryMock {
    public static Retrofit getMockAdapter(Function<Request, String> responseByRequest) {
        return IntegrationOperationFactoryMock.getMockAdapter(new MockInterceptor(responseByRequest));
    }

    private static Retrofit getMockAdapter(MockInterceptor mockInterceptor) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mockInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://www.mock.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
