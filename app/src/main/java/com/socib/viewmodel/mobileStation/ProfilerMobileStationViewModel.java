package com.socib.viewmodel.mobileStation;

import com.socib.model.StationType;
import com.socib.service.mobileStation.MobileStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class ProfilerMobileStationViewModel extends AbstractMobileStationViewModel {
    public ProfilerMobileStationViewModel(MobileStationApiService mobileStationApiService, SchedulerProvider schedulerProvider) {
        super(mobileStationApiService, schedulerProvider);
    }

    @Override
    protected StationType getStationType() {
        return StationType.PROFILER;
    }
}
