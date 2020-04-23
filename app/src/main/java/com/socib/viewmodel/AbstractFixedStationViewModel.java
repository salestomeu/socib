package com.socib.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;

import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public abstract class AbstractFixedStationViewModel extends ViewModel {
    private CompositeDisposable disposable;
    private MutableLiveData<List<FixedStation>> fixedStations = new MutableLiveData<>();
    private final FixedStationApiService fixedStationApiService;
    private final SchedulerProvider schedulerProvider;

    public AbstractFixedStationViewModel(FixedStationApiService fixedStationApiService, SchedulerProvider schedulerProvider){
        this.fixedStationApiService = fixedStationApiService;
        this.schedulerProvider = schedulerProvider;
        disposable = new CompositeDisposable();
    }

    public LiveData<List<FixedStation>> getFixedStation() {
        return fixedStations;
    }

    public void fetchFixedStation() {
        disposable.add(fixedStationApiService.getFixedStations(getStationType())
                .compose(schedulerProvider.applySchedulers())
                .subscribe(this::onSuccess, this::onError));
    }

    protected abstract StationType getStationType();

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
