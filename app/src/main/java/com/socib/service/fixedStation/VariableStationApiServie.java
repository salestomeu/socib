package com.socib.service.fixedStation;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.GetDataResponse;
import com.socib.integrationSocib.model.Variable;
import com.socib.service.provider.SchedulerProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public class VariableStationApiServie {

    private static final String PROCESSING_LEVEL = "L1";
    private static final Integer MAX_QC_VALUE = 2;

    private GetApiOperation getApiOperation;
    private SchedulerProvider schedulerProvider;

    public VariableStationApiServie(Retrofit retrofit,
                                    SchedulerProvider schedulerProvider) {
        this.getApiOperation = retrofit.create(GetApiOperation.class);
        this.schedulerProvider = schedulerProvider;

    }
    public Observable<List<Variable>> getVariables(String dataSourceId){
        return getApiOperation.getData(dataSourceId, PROCESSING_LEVEL, MAX_QC_VALUE, FixedStationApiService.TRUE, FixedStationApiService.apiKey)
                .subscribeOn(this.schedulerProvider.getSchedulerIo())
                .doOnError(error -> System.err.println("The error message is: " + error.getMessage()))
                .map(this::converterListVariable)
                .onErrorReturnItem(Collections.emptyList())
                .observeOn(this.schedulerProvider.getSchedulerUi());
    }

    private List<Variable> converterListVariable(List<GetDataResponse> responseData) {
        return responseData
                .stream()
                .map(GetDataResponse::getVariables)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
     }
}
