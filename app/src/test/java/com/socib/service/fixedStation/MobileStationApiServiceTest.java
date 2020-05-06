package com.socib.service.fixedStation;

import com.google.gson.Gson;
import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.GetDataSourceResponse;
import com.socib.model.MobileStation;
import com.socib.model.StationType;
import com.socib.service.mobileStation.MobileStationApiService;
import com.socib.util.UtilTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class MobileStationApiServiceTest {

    private static final String DATA_SOURCES_FILE_NAME = "mobileStation.json";
    private static final int SIZE_LIST = 7;

    private UtilTest utilTest;
    private Gson gson;

    @Mock
    private GetApiOperation getApiOperation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        utilTest = new UtilTest();
        gson = new Gson();
    }

    @Test
    public void it_should_be_subscribed_complete_and_no_errors() {
        MobileStationApiService mobileStationService = getMobileStationService(DATA_SOURCES_FILE_NAME);

        Observable<List<MobileStation>> fixedStations = mobileStationService.getMobileStations(StationType.GLIDER);

        fixedStations.test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors();
    }

    @Test
    public void it_should_be_equal_coastal_fixed_station_list_size() {
        MobileStationApiService mobileStationService = getMobileStationService(DATA_SOURCES_FILE_NAME);

        Observable<List<MobileStation>> fixedStations = mobileStationService.getMobileStations(StationType.GLIDER);

        fixedStations
                .test()
                .assertValue(fixedStationList -> fixedStationList.size() == SIZE_LIST);
    }

    private MobileStationApiService getMobileStationService(final String dataSource) {
        GetDataSourceResponse dataSourceResponse = gson.fromJson(utilTest.getMockResponse(dataSource), GetDataSourceResponse.class);
        when(getApiOperation.getMobileStation(anyString(), anyString(), anyString())).thenReturn(Observable.just(dataSourceResponse));
        return new MobileStationApiService(getApiOperation, "Test");
    }
}
