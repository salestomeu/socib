package com.socib.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socib.model.VariableStation;
import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class VariableStationViewModel extends ViewModel {

    private CompositeDisposable disposable;
    private MutableLiveData<Set<VariableStation>> variablesStation = new MutableLiveData<>();
    private final VariableStationApiService variableStationApiService;
    private final SchedulerProvider schedulerProvider;

    @Inject
    public VariableStationViewModel(VariableStationApiService variableStationApiService, SchedulerProvider schedulerProvider) {
        this.variableStationApiService = variableStationApiService;
        this.schedulerProvider = schedulerProvider;
        disposable = new CompositeDisposable();
    }


    public void fetchVariablesStation(Set<String> dataSourceIds){
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
