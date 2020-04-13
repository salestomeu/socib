package com.socib.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.fixedStation.SeaLevelStationApiService;
import com.socib.service.provider.SchedulerProviderImpl;

import java.util.List;

public class SeaLevelStationViewModel extends AndroidViewModel {
    private LiveData<List<FixedStation>> fixedStations;
    private FixedStationApiService seaLevelStationApiService;

    public SeaLevelStationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<FixedStation>> getFixedStation() {
        if (fixedStations == null){
            fixedStations = new MutableLiveData<>();
            seaLevelStationApiService = new SeaLevelStationApiService(IntegrationOperationFactory.getAdapter(), new SchedulerProviderImpl());
        }
        fixedStations = seaLevelStationApiService.getFixedStationsLiveData(StationType.SEALEVEL.stationType());
        return fixedStations;
    }
}
