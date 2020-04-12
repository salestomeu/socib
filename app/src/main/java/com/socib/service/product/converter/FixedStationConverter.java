package com.socib.service.product.converter;

import android.util.Log;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.Data;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;

import java.util.ArrayList;
import java.util.List;

public class FixedStationConverter extends AbstractModelConverter<FixedStation, Product> {

    public FixedStation toApiModel(Product domainModel, DataSource dataSource, List<Data> datas, Class<FixedStation> apiClass) {
        FixedStation fixedStation = super.toApiModel(domainModel, apiClass);
        if (dataSource.getCoverage_bounding_box() != null) {
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
        fixedStation.setVariables( new ArrayList<>());
        if (datas != null) {
            for (Data data : datas) {
                Log.i("data.variables:", data.getVariables().toString());
                fixedStation.getVariables().addAll(data.getVariables());
            }
        }
        return fixedStation;
    }


}
