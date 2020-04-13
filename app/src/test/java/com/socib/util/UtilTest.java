package com.socib.util;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.socib.integrationSocib.GetApiOperation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;

public class UtilTest {

    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }

    public static String readTextStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    public String getReponseByRequest(Request request, String responseProduct, String responseDataSource, String responseData){
        if(request.url().uri().getPath().startsWith("/" + GetApiOperation.DATA_PRODUCTS_PATH)){
            return getMockResponse(responseProduct);
        } else if (request.url().uri().getPath().contains("/data/")){
            return getMockResponse(responseData);
        } else if(request.url().uri().getPath().startsWith("/" + GetApiOperation.DATA_SOURCES_PATH)){
            return  getMockResponse(responseDataSource);
        }
        return null;
    }

    private  String getMockResponse(String fileName)  {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        String response = null;
        try {
            response = readTextStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
