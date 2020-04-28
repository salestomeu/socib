package com.socib.service.mobileStation.converter;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.DataSource;
import com.socib.model.Coordinates;
import com.socib.model.MobileStation;
import com.socib.model.StationType;
import com.socib.service.mobileStation.factory.MobileStationFactory;

import java.util.Optional;
import java.util.regex.Pattern;

public class MobileStationConverter extends AbstractModelConverter<DataSource, MobileStation> {

    private static final String BASE_NAME = "http://api.socib.es/platforms/";
    private MobileStationFactory mobileStationFactory;

    public MobileStationConverter(){
        this.mobileStationFactory = new MobileStationFactory();
    }

    public MobileStation toDomainModel(DataSource apiModel, Class<MobileStation> domainClass, StationType stationType) {
        MobileStation mobileStation = this.mobileStationFactory.get(stationType);
        super.toDomainModel(apiModel, domainClass);
        mobileStation.setName(deleteBaseName(apiModel.getPlatform()));
        mobileStation.setStartDate(apiModel.getInitial_datetime());
        mobileStation.setLastUpdateDate(apiModel.getEnd_datetime());
        Optional.ofNullable(apiModel.getCoverage_bounding_box())
                .flatMap(cbb -> cbb.getCoordinates().stream().findAny())
                .map(lists -> lists.get(0))
                .ifPresent(coordinates -> {
                    Coordinates actualCoordinate = new Coordinates();
                    actualCoordinate.setLatitude(coordinates.get(1));
                    actualCoordinate.setLongitude(coordinates.get(0));
                    mobileStation.setActualPosition(actualCoordinate);
                });
        return mobileStation;
    }

    private String deleteBaseName(String name) {
     return  Pattern.compile(BASE_NAME).matcher(name)
                .replaceAll("");
    }
}
