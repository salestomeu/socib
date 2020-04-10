package com.socib.service.product.converter;

import android.util.Log;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;

import java.util.List;
import java.util.Optional;

public class FixedStationConverter extends AbstractModelConverter<FixedStation, Product> {

    public FixedStation toApiModel(Product domainModel, List<DataSource> dataSources, Class<FixedStation> apiClass) {
        FixedStation fixedStation = super.toApiModel(domainModel, apiClass);
        Optional<DataSource> dataSourceSelected = dataSources
                .stream()
                .filter(dataSource -> dataSource.getInstrument() != null)
                .findFirst();
        if (dataSourceSelected.isPresent()) {
            DataSource dataSource = dataSourceSelected.get();
            List<Double> coordinates = dataSource.getCoverage_bounding_box().getCoordinates()
                    .stream()
                    .findAny()
                    .get()
                    .get(0);
            fixedStation.setLatitude(coordinates.get(0));
            fixedStation.setLongitude(coordinates.get(1));

        }
        return fixedStation;
    }
}
