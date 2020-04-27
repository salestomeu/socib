package com.socib.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;
import com.socib.viewmodel.GliderMobileStationViewModel;
import com.socib.viewmodel.ProfilerMobileStationViewModel;
import com.socib.viewmodel.SurfaceMobileStationViewModel;
import com.socib.viewmodel.fixedStation.BuoyFixedStationViewModel;
import com.socib.viewmodel.fixedStation.CoastalFixedStationViewModel;
import com.socib.viewmodel.fixedStation.SeaLevelFixedStationViewModel;
import com.socib.viewmodel.fixedStation.WeatherFixedStationViewModel;

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
        this.fixedStationViewModelMap.put(BuoyFixedStationViewModel.class, this::createBuoyFixedStationViewModel);
        this.fixedStationViewModelMap.put(GliderMobileStationViewModel.class, this::createGliderMobileStationViewModel);
        this.fixedStationViewModelMap.put(ProfilerMobileStationViewModel.class, this::createProfilerMobileStationViewModel);
        this.fixedStationViewModelMap.put(SurfaceMobileStationViewModel.class, this::createSurfaceMobileStationViewModel);
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

    public ViewModel createBuoyFixedStationViewModel() {
        return new BuoyFixedStationViewModel(this.fixedStationApiService, this.schedulerProvider);
    }

    private ViewModel createGliderMobileStationViewModel() {
        return new GliderMobileStationViewModel(this.fixedStationApiService, this.schedulerProvider);
    }

    private ViewModel createSurfaceMobileStationViewModel() {
        return new SurfaceMobileStationViewModel(this.fixedStationApiService, this.schedulerProvider);
    }

    private ViewModel createProfilerMobileStationViewModel() {
        return new ProfilerMobileStationViewModel(this.fixedStationApiService, this.schedulerProvider);
    }
}
