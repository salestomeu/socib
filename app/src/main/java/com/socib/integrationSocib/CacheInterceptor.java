package com.socib.integrationSocib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.socib.SocibApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (hasNetwork()){
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build();
        }
        else{
            request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
        }
        return  chain.proceed(request);
    }

    private boolean hasNetwork(){
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) SocibApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()){
            isConnected = true;
        }
        return  isConnected;
    }
}
