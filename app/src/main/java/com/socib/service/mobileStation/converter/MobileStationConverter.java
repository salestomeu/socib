package com.socib.service.mobileStation.converter;

import android.util.Log;

import com.socib.commons.AbstractModelConverter;
import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.MobileStation;
import com.socib.model.StationType;
import com.socib.service.mobileStation.factory.MobileStationFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class MobileStationConverter extends AbstractModelConverter<Product, MobileStation> {
    private MobileStationFactory mobileStationFactory;

    public MobileStationConverter() {
        mobileStationFactory = new MobileStationFactory();
    }

    public MobileStation toDomainModel(Product apiModel, List<DataSource> dataSources, StationType stationType) {
        MobileStation mobileStation = this.mobileStationFactory.get(stationType);
        super.mapApiToDomainModel(apiModel, mobileStation);
        mobileStation.setDataSourceId(new HashSet<>());
        dataSources.forEach(dataSource -> {
            if (dataSource.getInstrument() != null) {
                mobileStation.getDataSourceId().add(dataSource.getId());
                if (mobileStation.getLastUpdateDate() == null) {
                    mobileStation.setLastUpdateDate(dataSource.getEnd_datetime());
                    Optional.ofNullable(dataSource.getCoverage_bounding_box())
                            .flatMap(cbb -> cbb.getCoordinates().stream().findAny())
                            .map(lists -> lists.get(0))
                            .ifPresent(coordinates -> {
                                mobileStation.setLatitude(coordinates.get(1));
                                mobileStation.setLongitude(coordinates.get(0));
                            });
                }
            }
        });
        Log.i("fixedStation:", mobileStation.toString());
        return mobileStation;
    }
}
