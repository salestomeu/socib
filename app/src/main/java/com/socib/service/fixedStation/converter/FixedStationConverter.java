package com.socib.service.fixedStation.converter;

import android.util.Log;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.Data;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class FixedStationConverter extends AbstractModelConverter<FixedStation, Product> {

    /*public FixedStation toApiModel(Product domainModel, DataSource dataSource, List<Data> datas, Class<FixedStation> apiClass) {
        FixedStation fixedStation = super.toApiModel(domainModel, apiClass);
        if (dataSource.getCoverage_bounding_box() != null) {
            List<Double> coordinates = dataSource.getCoverage_bounding_box().getCoordinates()
                    .stream()
                    .findAny()
                    .get()
                    .get(0);
            fixedStation.setLastUpdateDate(dataSource.getEnd_datetime());
            //fixedStation.setDataSourceId(dataSource.getId());
            fixedStation.setLatitude(coordinates.get(1));
            fixedStation.setLongitude(coordinates.get(0));
        }
        fixedStation.setVariables( new ArrayList<>());
        if (datas != null) {
            for (Data data : datas) {
                System.out.println("data.variables:"+ data.getVariables().toString());
                fixedStation.getVariables().addAll(data.getVariables()
                        .stream()
                        .filter(variable -> variable.getData() != null && !"NaN".equals(variable.getData()))
                        .collect(Collectors.toList()));
            }
        }
        return fixedStation;
    }*/

    public FixedStation toApiModel(Product domainModel, List<DataSource> dataSources, Class<FixedStation> apiClass) {
        FixedStation fixedStation = super.toApiModel(domainModel, apiClass);
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
