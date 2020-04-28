package com.socib.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.socib.service.mobileStation.MobileStationApiService;
import com.socib.service.provider.SchedulerProvider;
import com.socib.viewmodel.GliderMobileStationViewModel;
import com.socib.viewmodel.mobileStation.ProfilerMobileStationViewModel;
import com.socib.viewmodel.mobileStation.SurfaceMobileStationViewModel;

import java.util.HashMap;
import java.util.function.Supplier;

public class MobileStationViewModelFactory implements ViewModelProvider.Factory {

    private final MobileStationApiService mobileStationApiService;
    private final SchedulerProvider schedulerProvider;
    private HashMap<Class, Supplier<ViewModel>> mobileStationViewModelMap = new HashMap<>();

    public MobileStationViewModelFactory(MobileStationApiService mobileStationApiService, SchedulerProvider schedulerProvider) {
        this.mobileStationApiService = mobileStationApiService;
        this.schedulerProvider = schedulerProvider;
        this.mobileStationViewModelMap.put(GliderMobileStationViewModel.class, this::createGliderMobileStationViewModel);
        this.mobileStationViewModelMap.put(ProfilerMobileStationViewModel.class, this::createProfilerMobileStationViewModel);
        this.mobileStationViewModelMap.put(SurfaceMobileStationViewModel.class, this::createSurfaceMobileStationViewModel);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(!mobileStationViewModelMap.containsKey(modelClass)){
            return null;
        }
        return (T) mobileStationViewModelMap.get(modelClass).get();
    }

    private ViewModel createGliderMobileStationViewModel() {
        return new GliderMobileStationViewModel(this.mobileStationApiService, this.schedulerProvider);
    }

    private ViewModel createSurfaceMobileStationViewModel() {
        return new SurfaceMobileStationViewModel(this.mobileStationApiService, this.schedulerProvider);
    }

    private ViewModel createProfilerMobileStationViewModel() {
        return new ProfilerMobileStationViewModel(this.mobileStationApiService, this.schedulerProvider);
    }
}
