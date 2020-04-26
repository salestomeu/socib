package com.socib.service.fixedStation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.GetDataResponse;
import com.socib.model.VariableStation;
import com.socib.util.UtilTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class VariableStationApiServiceTest {

    private static final String DATA_FILE_NAME = "data.json";
    private static final String DATA_NAN_VALUE_FILE_NAME = "dataNanValue.json";
    private static final Integer VARIABLE_STATION_SIZE_LIST = 14;
    private static final Integer VARIABLE_STATION_NAN_SIZE_LIST = 11;

    private UtilTest utilTest;
    private Gson gson;
    private Set<String> dataSources;

    @Mock
    private GetApiOperation getApiOperation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gson = new Gson();
        utilTest = new UtilTest();
        dataSources = new HashSet<>();
        dataSources.add("aaa");
    }

    @Test
    public void it_should_be_subscribed_complete_and_no_errors() {
        VariableStationApiService variableStationApiService = getVariableStationApiService(DATA_FILE_NAME);

        Observable<Set<VariableStation>> variablesStations = variableStationApiService.getVariables(dataSources);

        Observable.just(variablesStations).test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors();
    }

    @Test
    public void it_should_be_equal_variable_station_list_size() {
        VariableStationApiService variableStationApiService = getVariableStationApiService(DATA_FILE_NAME);

        Observable<Set<VariableStation>> variablesStations = variableStationApiService.getVariables(dataSources);

        variablesStations
                .test()
                .assertValue(variableList -> variableList.size() == VARIABLE_STATION_SIZE_LIST);
    }

    @Test
    public void it_should_be_equal_variable_station_list_nan_size() {
        VariableStationApiService variableStationApiService = getVariableStationApiService(DATA_NAN_VALUE_FILE_NAME);

        Observable<Set<VariableStation>> variablesStations = variableStationApiService.getVariables(dataSources);

        variablesStations
                .test()
                .assertValue(variableList -> variableList.size() == VARIABLE_STATION_NAN_SIZE_LIST);
    }

    private VariableStationApiService getVariableStationApiService(final String data) {
        Type listType = new TypeToken<ArrayList<GetDataResponse>>() {
        }.getType();
        List<GetDataResponse> dataResponse = gson.fromJson(utilTest.getMockResponse(data), listType);
        when(getApiOperation.getData(anyString(), anyString(), anyInt(), anyString(), anyString())).thenReturn(Observable.just(dataResponse));

        return new VariableStationApiService(getApiOperation, "Test");
    }
}
