package com.socib.viewmodel.variableStation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socib.model.FixedStation;
import com.socib.model.VariableStation;
import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.disposables.CompositeDisposable;

public abstract class AbstractVariableStationViewModel extends ViewModel {

    private CompositeDisposable disposable;
    private MutableLiveData<Set<VariableStation>> variablesStation = new MutableLiveData<>();
    private final VariableStationApiService variableStationApiService;
    private final SchedulerProvider schedulerProvider;

    public AbstractVariableStationViewModel(VariableStationApiService variableStationApiService, SchedulerProvider schedulerProvider) {
        this.variableStationApiService = variableStationApiService;
        this.schedulerProvider = schedulerProvider;
        disposable = new CompositeDisposable();
    }


    public void fetchVariablesStation(List<FixedStation> fixedStations){
        Set<String> dataSourceIds = fixedStations.stream()
                .map(FixedStation::getDataSourceId)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        disposable.add(variableStationApiService.getVariables(dataSourceIds)
                .compose(schedulerProvider.applySchedulers())
                .subscribe(this::onSuccess,this::onError));
    }

    public LiveData<Set<VariableStation>> getVariablesStation(){
        return variablesStation;
    }

    private void onSuccess(Set<VariableStation> variableList) {
        variablesStation.postValue(variableList);
    }

    private void onError(Throwable error) {
        variablesStation.postValue(Collections.emptySet());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }
}
