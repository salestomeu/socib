package com.socib.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class VariableStationViewModelFactory implements ViewModelProvider.Factory{

    private final VariableStationApiService variableStationApiService;
    private final SchedulerProvider schedulerProvider;

    public VariableStationViewModelFactory(VariableStationApiService variableStationApiService, SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
        this.variableStationApiService = variableStationApiService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new VariableStationViewModel(this.variableStationApiService, this.schedulerProvider);
    }
}
