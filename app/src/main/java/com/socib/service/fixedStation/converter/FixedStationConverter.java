package com.socib.service.fixedStation.converter;

import android.util.Log;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.factory.FixedStationFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FixedStationConverter extends AbstractModelConverter<Product, FixedStation> {

    private FixedStationFactory fixedStationFactory;

    public FixedStationConverter() {
        fixedStationFactory = new FixedStationFactory();
    }

    public FixedStation toDomainModel(Product apiModel, List<DataSource> dataSources, StationType stationType) {
        FixedStation fixedStation = this.fixedStationFactory.get(stationType);
        super.mapApiToDomainModel(apiModel, fixedStation);
        fixedStation.setVariables( new ArrayList<>());
        fixedStation.setDataSourceId(new HashSet<>());
        dataSources.forEach(dataSource ->{
            if (dataSource.getInstrument() != null) {
                fixedStation.getDataSourceId().add(dataSource.getId());
                if (fixedStation.getLastUpdateDate() == null){
                    fixedStation.setLastUpdateDate(dataSource.getEnd_datetime());
                    if (dataSource.getCoverage_bounding_box() != null) {
                        List<Double> coordinates = dataSource.getCoverage_bounding_box().getCoordinates()
                                .stream()
                                .findAny()
                                .get()
                                .get(0);
                        fixedStation.setLatitude(coordinates.get(1));
                        fixedStation.setLongitude(coordinates.get(0));
                    }
                }
            }
        });
        Log.i("fixedStation:",fixedStation.toString());
        return fixedStation;
    }
}
