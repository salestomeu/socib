package com.socib.service.product.converter;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FixedStationConverter extends AbstractModelConverter<FixedStation, Product> {

    public FixedStation toApiModel(Product domainModel, List<DataSource> dataSources,Class<FixedStation> apiClass) {
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
            fixedStation.setLastUpdateDate(dataSource.getEnd_datetime());
            fixedStation.setDataSourceId(dataSource.getId());
            fixedStation.setLatitude(coordinates.get(1));
            fixedStation.setLongitude(coordinates.get(0));
        }
        return fixedStation;
    }


}
