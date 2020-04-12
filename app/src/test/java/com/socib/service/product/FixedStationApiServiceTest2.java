package com.socib.service.product;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.integrationSocib.model.Data;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.provider.TestSchedulerProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;
import retrofit2.Retrofit;

import static org.junit.Assert.assertEquals;

public class FixedStationApiServiceTest2 {

    private static final String PRODUCT_DATA_FILE_NAME = "Product.json";
    private static final String PRODUCT_SOURCES_FILE_NAME = "dataSources.json";
    private static final String DATA_FILE_NAME = "data.json";

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Test
    public void aaaa() throws InterruptedException {

        Retrofit retrofitTest = IntegrationOperationFactory.getMockAdapter(this::getReponseByRequest);

        FixedStationApiService fixedStationApiService = new CoastalStationApiService(retrofitTest, new TestSchedulerProvider());

        LiveData<List<FixedStation>> products = fixedStationApiService.getDataProducts(StationType.COASTALSTATION.stationType());

        assertEquals("Must be equals:", 6, getValue(products).size());
    }

    @Test
    public void bbbb() throws InterruptedException {
        Retrofit retrofitTest = IntegrationOperationFactory.getMockAdapter(this.getMockResponse(DATA_FILE_NAME));
        FixedStationApiService fixedStationApiService = new CoastalStationApiService(retrofitTest, new TestSchedulerProvider());
        LiveData<List<Data>> result = fixedStationApiService.getData();
        List<Data> datos = getValue(result);
        assertEquals("Must be equals:", 2, datos.size());
    }



    private String getReponseByRequest(Request request){
        if(request.url().uri().getPath().startsWith("/" + GetApiOperation.DATA_PRODUCTS_PATH)){
            return this.getMockResponse(PRODUCT_DATA_FILE_NAME);
        } else if (request.url().uri().getPath().contains("processing_level")){
            return this.getMockResponse(DATA_FILE_NAME);
        } else if(request.url().uri().getPath().startsWith("/" + GetApiOperation.DATA_SOURCES_PATH)){
            return  this.getMockResponse(PRODUCT_SOURCES_FILE_NAME);
        }
        return null;
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

    public String getMockResponse(String fileName)  {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
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
