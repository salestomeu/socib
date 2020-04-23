package com.socib.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;

import java.util.HashMap;
import java.util.function.Supplier;

public class FixedStationViewModelFactory implements ViewModelProvider.Factory {

    private final FixedStationApiService fixedStationApiService;
    private final SchedulerProvider schedulerProvider;
    private HashMap<Class, Supplier<ViewModel>> fixedStationViewModelMap = new HashMap<>();

    public FixedStationViewModelFactory(FixedStationApiService fixedStationApiService, SchedulerProvider schedulerProvider) {
        this.fixedStationApiService = fixedStationApiService;
        this.schedulerProvider = schedulerProvider;
        this.fixedStationViewModelMap.put(CoastalFixedStationViewModel.class, this::createCoastalFixedStationViewModel);
        this.fixedStationViewModelMap.put(SeaLevelFixedStationViewModel.class, this::createSealevelFixedStationViewModel);
        this.fixedStationViewModelMap.put(WeatherFixedStationViewModel.class, this::createWeatherFixedStationViewModel);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(!fixedStationViewModelMap.containsKey(modelClass)){
            return null;
        }
        return (T) fixedStationViewModelMap.get(modelClass).get();
    }

    public ViewModel createCoastalFixedStationViewModel() {
        return new CoastalFixedStationViewModel(this.fixedStationApiService, this.schedulerProvider);
    }

    public ViewModel createSealevelFixedStationViewModel() {
        return new SeaLevelFixedStationViewModel(this.fixedStationApiService, this.schedulerProvider);
    }

    public ViewModel createWeatherFixedStationViewModel() {
        return new WeatherFixedStationViewModel(this.fixedStationApiService, this.schedulerProvider);
    }
}
