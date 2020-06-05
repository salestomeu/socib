package com.socib.viewmodel.mobileStation;

import com.socib.model.StationType;
import com.socib.service.mobileStation.MobileStationApiService;
import com.socib.service.provider.SchedulerProvider;

public class SurfaceMobileStationViewModel extends AbstractMobileStationViewModel {
    public SurfaceMobileStationViewModel(MobileStationApiService moobileStationApiService, SchedulerProvider schedulerProvider) {
        super(moobileStationApiService, schedulerProvider);
    }

    @Override
    protected StationType getStationType() {
        return StationType.SURFACE;
    }
}
