package com.socib.service.fixedStation.converter;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.factory.FixedStationFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class FixedStationConverter extends AbstractModelConverter<Product, FixedStation> {

    private FixedStationFactory fixedStationFactory;

    public FixedStationConverter() {
        fixedStationFactory = new FixedStationFactory();
    }

    public FixedStation toDomainModel(Product apiModel, List<DataSource> dataSources, StationType stationType) {
        FixedStation fixedStation = this.fixedStationFactory.get(stationType);
        super.mapApiToDomainModel(apiModel, fixedStation);
        fixedStation.setDataSourceId(new HashSet<>());
        dataSources.forEach(dataSource -> {
            if (dataSource.getInstrument() != null) {
                fixedStation.getDataSourceId().add(dataSource.getId());
                if (fixedStation.getLastUpdateDate() == null) {
                    fixedStation.setLastUpdateDate(dataSource.getEnd_datetime());
                    Optional.ofNullable(dataSource.getCoverage_bounding_box())
                            .flatMap(cbb -> cbb.getCoordinates().stream().findAny())
                            .map(lists -> lists.get(0))
                            .ifPresent(coordinates -> {
                                fixedStation.setLatitude(coordinates.get(1));
                                fixedStation.setLongitude(coordinates.get(0));
                            });
                }
            }
        });
        return fixedStation;
    }
}
