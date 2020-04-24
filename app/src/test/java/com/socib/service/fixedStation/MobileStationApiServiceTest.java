package com.socib.service.fixedStation;

import com.google.gson.Gson;
import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.GetDataSourceResponse;
import com.socib.integrationSocib.model.GetProductsResponse;
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

    private static final String DATA_SOURCES_FILE_NAME = "dataSources.json";
    private static final String COASTAL_PRODUCT_DATA_FILE_NAME = "CoastalProduct.json";

    private UtilTest utilTest;
    private Gson gson;

    private static final Double LONGITUDE = 3.383145;

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
        MobileStationApiService mobileStationService = getFixedStationService(COASTAL_PRODUCT_DATA_FILE_NAME, DATA_SOURCES_FILE_NAME);

        Observable<List<MobileStation>> fixedStations = mobileStationService.getMobileStations(StationType.GLIDER);

        fixedStations.test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors();
    }

    private MobileStationApiService getFixedStationService(final String product, final String dataSource) {
        GetProductsResponse productResponse = gson.fromJson(utilTest.getMockResponse(product), GetProductsResponse.class);
        GetDataSourceResponse dataSourceResponse = gson.fromJson(utilTest.getMockResponse(dataSource), GetDataSourceResponse.class);
        when(getApiOperation.getProducts(anyString(), anyString(), anyString())).thenReturn(Observable.just(productResponse));
        when(getApiOperation.getDataSource(anyString(), anyString(), anyString())).thenReturn(Observable.just(dataSourceResponse));
        return new MobileStationApiService(getApiOperation);
    }
}
