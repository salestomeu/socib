package com.socib.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.model.MobileStation;
import com.socib.model.StationType;
import com.socib.service.mobileStation.MobileStationApiService;
import com.socib.service.provider.SchedulerProviderImpl;

import java.util.List;

public class GliderStationViewModel extends AndroidViewModel {
    private MutableLiveData<List<MobileStation>> gliderStation;
    private MobileStationApiService mobileStationApiService;

    public GliderStationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<MobileStation>> getMobileStation() {
        if (gliderStation == null) {
            gliderStation = new MutableLiveData<>();
            mobileStationApiService = new MobileStationApiService(IntegrationOperationFactory.getAdapter(), new SchedulerProviderImpl());
        }
        mobileStationApiService.getMobileStations(StationType.GLIDER)
                .subscribe(gliderStation::setValue);
        return gliderStation;
    }
}
