package com.socib.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.integrationSocib.model.Variable;
import com.socib.model.FixedStation;
import com.socib.service.fixedStation.VariableStationApiServie;
import com.socib.service.provider.SchedulerProviderImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableStationViewModel extends AndroidViewModel {

    private MutableLiveData<Map<String, List<Variable>>> variablesStation;
    private VariableStationApiServie variableStationApiServie;
    public VariableStationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Map<String, List<Variable>>> getVariablesStation(String dataSourceId){
        if(variablesStation == null){
            variablesStation = new MutableLiveData<>();
            variablesStation.setValue(new HashMap<>());
            variableStationApiServie = new VariableStationApiServie(IntegrationOperationFactory.getAdapter(), new SchedulerProviderImpl());
        }
        variableStationApiServie.getVariables(dataSourceId)
                .subscribe(response -> variablesStation.getValue().put(dataSourceId, response));
        return variablesStation;
    }
}
