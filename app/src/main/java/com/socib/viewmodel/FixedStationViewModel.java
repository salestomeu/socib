package com.socib.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.model.FixedStation;
import com.socib.service.product.FixedStationApiService;
import com.socib.service.product.converter.FixedStationConverter;

import java.util.List;

public class FixedStationViewModel extends AndroidViewModel {
    private LiveData<List<FixedStation>> fixedStations;
    private FixedStationApiService fixedStationApiService;

    public FixedStationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<FixedStation>> getFixedStation() {
        if (fixedStations == null){
            fixedStations = new MutableLiveData<>();
            fixedStationApiService = new FixedStationApiService(IntegrationOperationFactory.getAdapter(),new FixedStationConverter());
        }
        fixedStations = fixedStationApiService.getDataProducts("Coastal Station");
        return fixedStations;
    }

}
