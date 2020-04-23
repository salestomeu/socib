package com.socib.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;

import java.util.HashMap;
import java.util.function.Supplier;

public class VariableStationViewModelFactory implements ViewModelProvider.Factory{

    private final VariableStationApiService variableStationApiService;
    private final SchedulerProvider schedulerProvider;
    private HashMap<Class, Supplier<ViewModel>> variableStationViewModelMap = new HashMap<>();

    public VariableStationViewModelFactory(VariableStationApiService variableStationApiService, SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
        this.variableStationApiService = variableStationApiService;
        this.variableStationViewModelMap.put(VariableCosatalStationViewModel.class, this::createVariableCostalFixedStationViewModel);
        this.variableStationViewModelMap.put(VariableSeaLevelStationViewModel.class, this::createVariableSeaLevelStationViewModel);
        this.variableStationViewModelMap.put(VariableWeatherStationViewModel.class, this::createVariableWeatherFixedStationViewModel);

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!variableStationViewModelMap.containsKey(modelClass)){
            return null;
        }
        return (T) variableStationViewModelMap.get(modelClass).get();
    }

    public ViewModel createVariableCostalFixedStationViewModel(){
        return new VariableCosatalStationViewModel(this.variableStationApiService, this.schedulerProvider);
    }
    public ViewModel createVariableSeaLevelStationViewModel(){
        return new VariableSeaLevelStationViewModel(this.variableStationApiService, this.schedulerProvider);
    }
    public ViewModel createVariableWeatherFixedStationViewModel(){
        return new VariableWeatherStationViewModel(this.variableStationApiService, this.schedulerProvider);
    }
}
