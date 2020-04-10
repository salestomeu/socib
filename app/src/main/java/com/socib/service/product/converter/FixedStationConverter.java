package com.socib.service.product.converter;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;

import java.util.List;
import java.util.Optional;

public class FixedStationConverter extends AbstractModelConverter<FixedStation, Product> {

    public FixedStation toApiModel(Product domainModel, List<DataSource> dataSources, Class<FixedStation> apiClass) {
        FixedStation fixedStation = super.toApiModel(domainModel, apiClass);

        DataSource dataSourceSelected = dataSources
                .stream()
                .filter(dataSource -> dataSource.getInstrument() != null)
                .findFirst().orElse(new DataSource());
        Optional<List<Double>> coordinates = dataSourceSelected.getCoverageBoundingBox().getCoordinates()
                .stream()
                .findAny();
        if (coordinates.isPresent()) {
            fixedStation.setLatitude(coordinates.get().get(0));
            fixedStation.setLongitude(coordinates.get().get(1));
        }
        return fixedStation;
    }
}
