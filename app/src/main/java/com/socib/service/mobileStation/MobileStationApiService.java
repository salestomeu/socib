package com.socib.service.mobileStation;

import com.socib.integrationSocib.GetApiOperation;
import com.socib.model.FixedStation;
import com.socib.model.MobileStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.mobileStation.converter.MobileStationConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class MobileStationApiService extends FixedStationApiService {

    private MobileStationConverter mobileStationConverter;

    public MobileStationApiService(GetApiOperation getApiOperation, String apiKey) {
        super(getApiOperation, apiKey);
        mobileStationConverter = new MobileStationConverter();
    }

    public Observable<List<MobileStation>> getMobileStations(StationType glider) {
        Observable<List<FixedStation>> fixedStations = super.getFixedStations(glider);
        return fixedStations.map(this::getMobileStationList);
    }

    private List<MobileStation> getMobileStationList(List<FixedStation> fixedStationList) {
        return fixedStationList
                .stream()
                .map(fixedStation -> mobileStationConverter.toDomainModel(fixedStation, MobileStation.class))
                .collect(Collectors.toList());
    }
}
