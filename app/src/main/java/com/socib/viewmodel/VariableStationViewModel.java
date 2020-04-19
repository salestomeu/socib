package com.socib.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.integrationSocib.model.Variable;
import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;
import com.socib.service.provider.SchedulerProviderImpl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class VariableStationViewModel extends ViewModel {

    private CompositeDisposable disposable;
    private MutableLiveData<List<Variable>> variablesStation = new MutableLiveData<>();
    private final VariableStationApiService variableStationApiService;
    private final SchedulerProvider schedulerProvider;

    @Inject
    public VariableStationViewModel(VariableStationApiService variableStationApiService, SchedulerProvider schedulerProvider) {
        this.variableStationApiService = variableStationApiService;
        this.schedulerProvider = schedulerProvider;
        disposable = new CompositeDisposable();
    }


    public void fetchVariablesStation(String dataSourceId){
        disposable.add(variableStationApiService.getVariables(dataSourceId)
                .compose(schedulerProvider.applySchedulers())
                .subscribe(this::onSuccess,this::onError));
    }

    public LiveData<List<Variable>> getVariablesStation(){
        return variablesStation;
    }

    private void onSuccess(List<Variable> variableList) {
        variablesStation.postValue(variableList);
    }

    private void onError(Throwable error) {
        variablesStation.postValue(Collections.emptyList());
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
