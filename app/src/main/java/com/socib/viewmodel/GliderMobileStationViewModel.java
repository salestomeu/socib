package com.socib.viewmodel;

import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.mobileStation.MobileStationApiService;
import com.socib.service.provider.SchedulerProvider;
import com.socib.viewmodel.fixedStation.AbstractFixedStationViewModel;
import com.socib.viewmodel.mobileStation.AbstractMobileStationViewModel;

public class GliderMobileStationViewModel extends AbstractMobileStationViewModel {
    public GliderMobileStationViewModel(MobileStationApiService moobileStationApiService, SchedulerProvider schedulerProvider) {
        super(moobileStationApiService, schedulerProvider);
    }

    @Override
    protected StationType getStationType() {
        return StationType.GLIDER;
    }
}
