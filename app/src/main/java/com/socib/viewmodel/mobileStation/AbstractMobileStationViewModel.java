package com.socib.viewmodel.mobileStation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socib.model.MobileStation;
import com.socib.model.StationType;
import com.socib.service.mobileStation.MobileStationApiService;
import com.socib.service.provider.SchedulerProvider;

import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public abstract class AbstractMobileStationViewModel extends ViewModel {
    private CompositeDisposable disposable;
    private MutableLiveData<List<MobileStation>> mobileStation = new MutableLiveData<>();
    private final MobileStationApiService mobileStationApiService;
    private final SchedulerProvider schedulerProvider;

    public AbstractMobileStationViewModel(MobileStationApiService mobileStationApiService, SchedulerProvider schedulerProvider) {
        this.mobileStationApiService = mobileStationApiService;
        this.schedulerProvider = schedulerProvider;
        disposable = new CompositeDisposable();
    }

    protected abstract StationType getStationType();

    public LiveData<List<MobileStation>> getMobileStation() {
        return  mobileStation;
    }

    public void fetchMobileStation() {
        disposable.add(mobileStationApiService.getMobileStations(getStationType())
                .compose(schedulerProvider.applySchedulers())
                .subscribe(this::onSuccess, this::OnError));
    }

    private void onSuccess(List<MobileStation> mobileStations){
        mobileStation.postValue(mobileStations);
    }

    private void OnError(Throwable throwable) {
        mobileStation.postValue(Collections.emptyList());
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
