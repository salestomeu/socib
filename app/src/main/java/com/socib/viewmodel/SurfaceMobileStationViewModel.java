package com.socib.viewmodel;

import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;
import com.socib.viewmodel.fixedStation.AbstractFixedStationViewModel;

public class SurfaceMobileStationViewModel extends AbstractFixedStationViewModel {
    public SurfaceMobileStationViewModel(FixedStationApiService fixedStationApiService, SchedulerProvider schedulerProvider) {
        super(fixedStationApiService, schedulerProvider);
    }

    @Override
    protected StationType getStationType() {
        return StationType.SURFACE;
    }
}
