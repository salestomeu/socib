package com.socib.viewmodel;

import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;
import com.socib.viewmodel.fixedStation.AbstractFixedStationViewModel;

public class GliderMobileStationViewModel extends AbstractFixedStationViewModel {
    public GliderMobileStationViewModel(FixedStationApiService mobileStationApiService, SchedulerProvider schedulerProvider) {
        super(mobileStationApiService, schedulerProvider);
    }

    @Override
    protected StationType getStationType() {
        return StationType.GLIDER;
    }
}
