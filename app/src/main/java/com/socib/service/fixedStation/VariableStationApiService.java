package com.socib.service.fixedStation;

import android.annotation.SuppressLint;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.GetDataResponse;
import com.socib.model.VariableStation;
import com.socib.service.fixedStation.converter.VariableStationConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;

public class VariableStationApiService {

    private static final String PROCESSING_LEVEL = "L1";
    private static final Integer MAX_QC_VALUE = 2;
    private static final String NAN = "NaN";

    private final GetApiOperation getApiOperation;
    private final VariableStationConverter variableStationConverter;

    @Inject
    public VariableStationApiService(GetApiOperation getApiOperation) {
        this.getApiOperation = getApiOperation;
        this.variableStationConverter = new VariableStationConverter();

    }

    @SuppressLint("CheckResult")
    public Observable<Set<VariableStation>> getVariables(Set<String> dataSourceIds) {
        List<Observable<Set<VariableStation>>> result = new ArrayList<>();
        for (String dataSourceId : dataSourceIds) {
            result.add(getApiOperation.getData(dataSourceId, PROCESSING_LEVEL, MAX_QC_VALUE, FixedStationApiService.TRUE, FixedStationApiService.apiKey)
                    .doOnError(error -> System.err.println("The error message is: " + error.getMessage()))
                    .map(this::converterListVariable)
                    .onErrorReturnItem(Collections.emptySet()));
        }
        return Observable.zip(result, this::getVariableStationSet);
    }

    private Set<VariableStation> getVariableStationSet(Object[] objects) {
        Set<VariableStation> result2 = new HashSet<>();
        for (Object object : objects) {
            result2.addAll((Collection<? extends VariableStation>) object);
        }
        return result2;
    }

    private Set<VariableStation> converterListVariable(List<GetDataResponse> responseData) {
        Set<VariableStation> result = responseData
                .stream()
                .map(GetDataResponse::getVariables)
                .flatMap(Collection::stream)
                .filter(variable -> !NAN.equals(variable.getData()))
                .map(variable -> variableStationConverter.toDomainModel(variable, VariableStation.class))
                .collect(Collectors.toSet());
        return result;
    }
}
