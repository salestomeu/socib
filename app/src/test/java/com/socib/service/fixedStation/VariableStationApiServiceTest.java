package com.socib.service.fixedStation;

import com.socib.integrationSocib.IntegrationOperationFactoryMock;
import com.socib.integrationSocib.model.Variable;
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

public class VariableStationApiServiceTest {

    private static final String DATA_FILE_NAME = "data.json";
    private static final String DATA_NAN_VALUE_FILE_NAME = "dataNanValue.json";
    private static final Integer VARIABLE_STATION_SIZE_LIST = 14;
    private static final Integer VARIABLE_STATION_NAN_SIZE_LIST = 11;

    private UtilTest utilTest;

    @Before
    public void setUp(){
        utilTest = new UtilTest();
    }


    private VariableStationApiServie getVariableStationApiService(final String data){
        Retrofit retrofitTest = IntegrationOperationFactoryMock
                .getMockAdapter(request ->
                        utilTest.getReponseByRequest(request, data));
        return new VariableStationApiServie(retrofitTest, new TestSchedulerProvider());
    }

    private Variable getFirstFixedStation(final Observable<List<Variable>> variableStations){
        return Objects.requireNonNull(
                Objects.requireNonNull(
                        variableStations.singleOrError().blockingGet()
                                .stream()
                                .findFirst()
                                .orElse(null)));
    }

    @Test
    public void it_should_be_subscribed_complete_and_no_errors() {
        VariableStationApiServie variableStationApiServie = getVariableStationApiService(DATA_FILE_NAME);

        Observable<List<Variable>> variablesStations = variableStationApiServie.getVariables("2");

        Observable.just(variablesStations).test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors();
    }

    @Test
    public void it_should_be_equal_variable_station_list_size(){
        VariableStationApiServie variableStationApiServie = getVariableStationApiService(DATA_FILE_NAME);

        Observable<List<Variable>> variablesStations = variableStationApiServie.getVariables("2");

        Single.just(variablesStations.singleOrError().blockingGet().size())
                .test()
                .assertResult(VARIABLE_STATION_SIZE_LIST);
    }

    @Test
    public void it_should_be_equal_variable_station_list_nan_size(){
        VariableStationApiServie variableStationApiServie = getVariableStationApiService(DATA_NAN_VALUE_FILE_NAME);

        Observable<List<Variable>> variablesStations = variableStationApiServie.getVariables("2");

        Single.just(variablesStations.singleOrError().blockingGet().size())
                .test()
                .assertResult(VARIABLE_STATION_NAN_SIZE_LIST);
    }
}
