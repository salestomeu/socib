package com.socib.service.product;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.socib.R;
import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.IntegrationOperationFactoryMock;
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
import static org.junit.Assert.assertNotNull;

public class FixedStationApiServiceTest {

    private static final String PRODUCT_DATA_FILE_NAME = "Product.json";
    private static final String ONE_PRODUCT_DATA_FILE_NAME = "OneProduct.json";
    private static final String DATA_SOURCES_FILE_NAME = "dataSources.json";
    private static final String DATA_SOURCES_NONE_INSTRUMENT_FILE_NAME = "dataSourcesNoneInstrument.json";

    private static final String DATA_FILE_NAME = "data.json";
    private static final String DATA_NAN_VALUE_FILE_NAME = "dataNanValue.json";

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Test
    public void whenAllProductsHaveDataSourceWithInstrument() throws InterruptedException {
        //given
        Retrofit retrofitTest = IntegrationOperationFactoryMock
                .getMockAdapter(request ->
                        getReponseByRequest(request, PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME, DATA_FILE_NAME));
        FixedStationApiService fixedStationApiService = new CoastalStationApiService(retrofitTest, new TestSchedulerProvider());

        //when
        List<FixedStation> fixedStations = getValue(fixedStationApiService.getDataProducts(StationType.COASTALSTATION.stationType()));
        FixedStation fixedStation = fixedStations.get(0);

        //then
        assertEquals("Must be equals:", 6, fixedStations.size());
        assertNotNull("Must be not null:",  fixedStation.getLatitude());
        assertNotNull("Must be not null:",  fixedStation.getLongitude());
        assertNotNull("Must be not null:",  fixedStation.getName());
        assertNotNull("Must be not null:",  fixedStation.getType());
        assertNotNull("Must be not null:",  fixedStation.getLastUpdateDate());
        assertEquals("Must be equals:", R.drawable.ic_map_station, fixedStation.getIcon());
    }


    @Test
    public void whenOneProductsHaveDataValueFromDataSourceWithInstrument() throws InterruptedException {
        //given
        Retrofit retrofitTest = IntegrationOperationFactoryMock
                .getMockAdapter(request ->
                        getReponseByRequest(request, ONE_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME, DATA_FILE_NAME));
        FixedStationApiService fixedStationApiService = new CoastalStationApiService(retrofitTest, new TestSchedulerProvider());

        //when
        List<FixedStation> fixedStations = getValue(fixedStationApiService.getDataProducts(StationType.COASTALSTATION.stationType()));
        FixedStation fixedStation = fixedStations.get(0);

        //then
        assertNotNull("Must be not null:",  fixedStation.getLatitude());
        assertNotNull("Must be not null:",  fixedStation.getLongitude());
        assertNotNull("Must be not null:",  fixedStation.getName());
        assertNotNull("Must be not null:",  fixedStation.getType());
        assertNotNull("Must be not null:",  fixedStation.getLastUpdateDate());
        assertEquals("Must be equals:", R.drawable.ic_map_station, fixedStation.getIcon());
        assertEquals("Must be equals:", 14, fixedStation.getVariables().size());
    }

    @Test
    public void whenProducstHaveAnyDataSourceWithInstrument() throws InterruptedException {
        //given
        Retrofit retrofitTest = IntegrationOperationFactoryMock
                .getMockAdapter(request ->
                        getReponseByRequest(request, PRODUCT_DATA_FILE_NAME, DATA_SOURCES_NONE_INSTRUMENT_FILE_NAME, DATA_FILE_NAME));
        FixedStationApiService fixedStationApiService = new CoastalStationApiService(retrofitTest, new TestSchedulerProvider());

        //when
        List<FixedStation> fixedStations = getValue(fixedStationApiService.getDataProducts(StationType.COASTALSTATION.stationType()));

        //then
        assertEquals("Must be equals:", 0, fixedStations.size());
    }

    @Test
    public void whenSomeDataHaveNanValue() throws InterruptedException {
        //given
        Retrofit retrofitTest = IntegrationOperationFactoryMock
                .getMockAdapter(request ->
                        getReponseByRequest(request, PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME, DATA_NAN_VALUE_FILE_NAME));
        FixedStationApiService fixedStationApiService = new CoastalStationApiService(retrofitTest, new TestSchedulerProvider());

        //when
        List<FixedStation> fixedStations = getValue(fixedStationApiService.getDataProducts(StationType.COASTALSTATION.stationType()));
        FixedStation fixedStation = fixedStations.get(0);

        //then
        assertEquals("Must be equals:", 6, fixedStations.size());
        assertNotNull("Must be not null:",  fixedStation.getLatitude());
        assertNotNull("Must be not null:",  fixedStation.getLongitude());
        assertNotNull("Must be not null:",  fixedStation.getName());
        assertNotNull("Must be not null:",  fixedStation.getType());
        assertNotNull("Must be not null:",  fixedStation.getLastUpdateDate());
        assertEquals("Must be equals:", R.drawable.ic_map_station, fixedStation.getIcon());
        assertEquals("Must be equals:", 11, fixedStation.getVariables().size());
    }

    private String getReponseByRequest(Request request, String responseProduct, String responseDataSource, String responseData){
        if(request.url().uri().getPath().startsWith("/" + GetApiOperation.DATA_PRODUCTS_PATH)){
            return this.getMockResponse(responseProduct);
        } else if (request.url().uri().getPath().contains("10f09dc763")){
            return this.getMockResponse(responseData);
        } else if(request.url().uri().getPath().startsWith("/" + GetApiOperation.DATA_SOURCES_PATH)){
            return  this.getMockResponse(responseDataSource);
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
