package com.socib.service.mobileStation;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.GetDataSourceResponse;
import com.socib.model.MobileStation;
import com.socib.model.StationType;
import com.socib.service.mobileStation.converter.MobileStationConverter;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class MobileStationApiService{

    private MobileStationConverter mobileStationConverter;
    private final GetApiOperation getApiOperation;
    private final String apiKey;
    private static final String TRUE = "true";

    public MobileStationApiService(GetApiOperation getApiOperation, String apiKey) {
        mobileStationConverter = new MobileStationConverter();
        this.getApiOperation = getApiOperation;
        this.apiKey = apiKey;
    }

    public Observable<List<MobileStation>> getMobileStations(StationType stationType) {
        return getApiOperation.getMobileStation(stationType.stationType(), TRUE, apiKey)
                .map(GetDataSourceResponse::getResults)
                .map(dataSources -> getMobileStationList(dataSources, stationType));
    }

    private List<MobileStation> getMobileStationList(List<DataSource> fixedStationList, StationType stationType) {
        return fixedStationList
                .stream()
                .map(fixedStation -> mobileStationConverter.toDomainModel(fixedStation, MobileStation.class, stationType))
                .collect(Collectors.toList());
    }
}
