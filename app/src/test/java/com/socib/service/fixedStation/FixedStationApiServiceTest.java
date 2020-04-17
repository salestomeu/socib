package com.socib.service.fixedStation;

import com.socib.R;
import com.socib.integrationSocib.IntegrationOperationFactoryMock;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.provider.TestSchedulerProvider;
import com.socib.util.UtilTest;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Retrofit;

public class FixedStationApiServiceTest {

    private static final String COASTAL_PRODUCT_DATA_FILE_NAME = "CoastalProduct.json";
    private static final String SEA_LEVEL_PRODUCT_DATA_FILE_NAME = "SeaLevelProduct.json";
    private static final String WEATHER_PRODUCT_DATA_FILE_NAME = "WeatherProduct.json";
    private static final String DATA_SOURCES_FILE_NAME = "dataSources.json";
    private static final String DATA_SOURCES_NONE_INSTRUMENT_FILE_NAME = "dataSourcesNoneInstrument.json";
    private static final int COASTAL_STATION_SIZE_LIST = 6;
    private static final int SEA_LEVEL_STATION_SIZE_LIST = 6;
    private static final int WEATHER_STATION_SIZE_LIST = 5;
    private static final int EMPTY_LIST = 0;
    private static final Double LATITUDE = 39.596277;
    private static final Double LONGITUDE = 3.383145;


    private UtilTest utilTest;

    @Before
    public void setUp(){
        utilTest = new UtilTest();
    }

    private FixedStationApiService getFixedStationService(final String product, final String dataSource){
        Retrofit retrofitTest = IntegrationOperationFactoryMock
                .getMockAdapter(request ->
                        utilTest.getReponseByRequest(request, product, dataSource, null));
        return new FixedStationApiService(retrofitTest, new TestSchedulerProvider());
    }

    private FixedStation getFirstFixedStation(final Observable<List<FixedStation>> fixedStations){
        return Objects.requireNonNull(
                Objects.requireNonNull(
                        fixedStations.singleOrError().blockingGet()
                                .stream()
                                .findFirst()
                                .orElse(null)));
    }

    @Test
    public void it_should_be_subscribed_complete_and_no_errors() {
        FixedStationApiService fixedStationService = getFixedStationService(COASTAL_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME);

        Observable<List<FixedStation>> fixedStations = fixedStationService.getFixedStationsLiveData(StationType.COASTALSTATION);

        Observable.just(fixedStations).test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors();
    }

    @Test
    public void it_should_be_equal_coastal_fixed_station_list_size() {
        FixedStationApiService fixedStationService = getFixedStationService(COASTAL_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME);

        Observable<List<FixedStation>> fixedStations = fixedStationService.getFixedStationsLiveData(StationType.COASTALSTATION);

        Single.just(fixedStations.singleOrError().blockingGet().size())
                .test()
                .assertResult(COASTAL_STATION_SIZE_LIST);
    }

    @Test
    public void it_should_be_equal_sea_level_fixed_station_list_size() {
        FixedStationApiService fixedStationService = getFixedStationService(SEA_LEVEL_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME);

        Observable<List<FixedStation>> fixedStations = fixedStationService.getFixedStationsLiveData(StationType.SEALEVEL);

        Single.just(fixedStations.singleOrError().blockingGet().size())
                .test()
                .assertResult(SEA_LEVEL_STATION_SIZE_LIST);
    }

    @Test
    public void it_should_be_equal_weather_fixed_station_list_size() {
        FixedStationApiService fixedStationService = getFixedStationService(WEATHER_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME);

        Observable<List<FixedStation>> fixedStations = fixedStationService.getFixedStationsLiveData(StationType.WEATHERSTATION);

        Single.just(fixedStations.singleOrError().blockingGet().size())
                .test()
                .assertResult(WEATHER_STATION_SIZE_LIST);
    }

    @Test
    public void it_should_be_equal_0_when_all_data_source_none_instrument() {
        FixedStationApiService fixedStationService = getFixedStationService(WEATHER_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_NONE_INSTRUMENT_FILE_NAME);

        Observable<List<FixedStation>> fixedStations = fixedStationService.getFixedStationsLiveData(StationType.WEATHERSTATION);

        Single.just(fixedStations.singleOrError().blockingGet().size())
                .test()
                .assertResult(EMPTY_LIST);
    }

    @Test
    public void it_should_be_latitude_and_longitude() {
        FixedStationApiService fixedStationService = getFixedStationService(COASTAL_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME);

        Observable<List<FixedStation>> fixedStations = fixedStationService.getFixedStationsLiveData(StationType.COASTALSTATION);

        Single.just(getFirstFixedStation(fixedStations).getLatitude())
                .test()
                .assertResult(LATITUDE);
        Single.just(getFirstFixedStation(fixedStations).getLongitude())
                .test()
                .assertResult(LONGITUDE);
    }

    @Test
    public void it_should_be_equal_coastal_fixed_station_icon() {
        FixedStationApiService fixedStationService = getFixedStationService(COASTAL_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME);

        Observable<List<FixedStation>> fixedStations = fixedStationService.getFixedStationsLiveData(StationType.COASTALSTATION);

        Single.just(getFirstFixedStation(fixedStations).getIcon())
                .test()
                .assertResult(R.drawable.ic_map_station);
    }

    @Test
    public void it_should_be_equal_sea_level_fixed_station_icon() {
        FixedStationApiService fixedStationService = getFixedStationService(SEA_LEVEL_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME);

        Observable<List<FixedStation>> fixedStations = fixedStationService.getFixedStationsLiveData(StationType.SEALEVEL);

        Single.just(getFirstFixedStation(fixedStations).getIcon())
                .test()
                .assertResult(R.drawable.ic_map_sea_level);
    }

    @Test
    public void it_should_be_equal_weather_fixed_station_icon() {
        FixedStationApiService fixedStationService = getFixedStationService(WEATHER_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME);

        Observable<List<FixedStation>> fixedStations = fixedStationService.getFixedStationsLiveData(StationType.WEATHERSTATION);

        Single.just(getFirstFixedStation(fixedStations).getIcon())
                .test()
                .assertResult(R.drawable.ic_map_meteo);
    }
}
