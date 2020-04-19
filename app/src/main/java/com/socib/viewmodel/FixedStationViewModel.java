package com.socib.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProviderImpl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class FixedStationViewModel extends ViewModel {
    private CompositeDisposable disposable;
    private MutableLiveData<List<FixedStation>> fixedStations;
    private final FixedStationApiService fixedStationApiService;

    @Inject
    public FixedStationViewModel(){
        this.fixedStationApiService = new FixedStationApiService(IntegrationOperationFactory.getAdapter(), new SchedulerProviderImpl());
        disposable = new CompositeDisposable();
    }

    public LiveData<List<FixedStation>> getFixedStation() {
        return fixedStations;
    }

    public void fetchFixedStation(StationType stationType) {
        if (fixedStations == null){
            fixedStations = new MutableLiveData<>();
            //fixedStationApiService = new FixedStationApiService(IntegrationOperationFactory.getAdapter(), new SchedulerProviderImpl());
        }
        disposable.add(fixedStationApiService.getFixedStations(stationType)
                .subscribe(this::onSuccess, this::onError));
    }

    private void onSuccess(List<FixedStation> fixedStationList){
        fixedStations.postValue(fixedStationList);
    }

    private void onError(Throwable error){
        fixedStations.postValue(Collections.emptyList());
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
