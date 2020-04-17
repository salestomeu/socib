package com.socib.service.fixedStation;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.socib.util.UtilTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CoastalStationApiServiceTest {

    private static final String PRODUCT_DATA_FILE_NAME = "CoastalProduct.json";
    private static final String ONE_PRODUCT_DATA_FILE_NAME = "OneProduct.json";
    private static final String DATA_SOURCES_FILE_NAME = "dataSources.json";
    private static final String DATA_SOURCES_NONE_INSTRUMENT_FILE_NAME = "dataSourcesNoneInstrument.json";
    private static final String DATA_FILE_NAME = "data.json";
    private static final String DATA_NAN_VALUE_FILE_NAME = "dataNanValue.json";

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private UtilTest utilTest;

    @Before
    public void setUp(){
        utilTest = new UtilTest();
    }

  /*  @Test
    public void whenAllProductsHaveDataSourceWithInstrument() throws InterruptedException {
        //given
        Retrofit retrofitTest = IntegrationOperationFactoryMock
                .getMockAdapter(request ->
                        utilTest.getReponseByRequest(request, PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME, DATA_FILE_NAME));
        FixedStationApiService coastalStationApiService = new FixedStationApiService(retrofitTest, new TestSchedulerProvider());

        //when
        Observable<List<FixedStation>> fixedStations = coastalStationApiService.getFixedStationsLiveData(StationType.COASTALSTATION);
        List<FixedStation> fixedStations = UtilTest.getValue();
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
                        utilTest.getReponseByRequest(request, ONE_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME, DATA_FILE_NAME));
        FixedStationApiService coastalStationApiService = new FixedStationApiService(retrofitTest, new TestSchedulerProvider());

        //when
        coastalStationApiService.getFixedStationsLiveData(StationType.COASTALSTATION).subscribe();
        List<FixedStation> fixedStations = UtilTest.getValue();
        FixedStation fixedStation = fixedStations.get(0);

        //then
        assertNotNull("Must be not null:",  fixedStation.getLatitude());
        assertNotNull("Must be not null:",  fixedStation.getLongitude());
        assertNotNull("Must be not null:",  fixedStation.getName());
        assertNotNull("Must be not null:",  fixedStation.getType());
        assertNotNull("Must be not null:",  fixedStation.getLastUpdateDate());
        assertEquals("Must be equals:", R.drawable.ic_map_station, fixedStation.getIcon());
        //assertEquals("Must be equals:", 14, fixedStation.getVariables().size());
    }

    @Test
    public void whenProducstHaveAnyDataSourceWithInstrument() throws InterruptedException {
        //given
        Retrofit retrofitTest = IntegrationOperationFactoryMock
                .getMockAdapter(request ->
                        utilTest.getReponseByRequest(request, PRODUCT_DATA_FILE_NAME, DATA_SOURCES_NONE_INSTRUMENT_FILE_NAME, DATA_FILE_NAME));
        CoastalStationApiService coastalStationApiService = new CoastalStationApiService(retrofitTest, new TestSchedulerProvider());

        //when
        List<FixedStation> fixedStations = UtilTest.getValue(coastalStationApiService.getFixedStationsLiveData(StationType.COASTALSTATION.stationType()));

        //then
       // assertEquals("Must be equals:", 0, fixedStations.size());
        assertNull("Must be null", fixedStations);
    }

    @Test
    @Ignore
    public void whenSomeDataHaveNanValue() throws InterruptedException {
        //given
        Retrofit retrofitTest = IntegrationOperationFactoryMock
                .getMockAdapter(request ->
                        utilTest.getReponseByRequest(request, PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME, DATA_NAN_VALUE_FILE_NAME));
        CoastalStationApiService coastalStationApiService = new CoastalStationApiService(retrofitTest, new TestSchedulerProvider());

        //when
        List<FixedStation> fixedStations = UtilTest.getValue(coastalStationApiService.getFixedStationsLiveData(StationType.COASTALSTATION.stationType()));
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
    }*/
}
