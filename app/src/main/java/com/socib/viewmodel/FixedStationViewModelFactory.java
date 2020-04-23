package com.socib.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class FixedStationViewModelFactory implements ViewModelProvider.Factory {

    private final FixedStationApiService fixedStationApiService;
    private final SchedulerProvider schedulerProvider;

    public FixedStationViewModelFactory(FixedStationApiService fixedStationApiService, SchedulerProvider schedulerProvider) {
        this.fixedStationApiService = fixedStationApiService;
        this.schedulerProvider = schedulerProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FixedStationViewModel(this.fixedStationApiService, this.schedulerProvider);
    }
}
