package com.socib.service.product;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.service.product.converter.FixedStationConverter;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FixedStationApiServiceTest {



    @Test
    public void aaaa() throws InterruptedException {
        FixedStationConverter fixedStationConverter = new FixedStationConverter();
        FixedStationApiService fixedStationApiService = new FixedStationApiService(IntegrationOperationFactory.getMockAdapter(mockResponse()),fixedStationConverter);
        LiveData<List<FixedStation>> products = fixedStationApiService.getDataProducts("coastal station");

       //assertEquals("", 6, getValue(products).size());
    }

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

    public String mockResponse()  {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Product.json");
        String response = null;
        try {
            response = readTextStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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



}
