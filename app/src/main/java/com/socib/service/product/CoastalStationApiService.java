package com.socib.service.product;

import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.CoastalStation;
import com.socib.model.FixedStation;
import com.socib.service.product.converter.CoastalStationConverter;

import java.util.List;

import retrofit2.Retrofit;

public class CoastalStationApiService extends FixedStationApiService {
    private CoastalStationConverter coastalStationConverter;
    public CoastalStationApiService(Retrofit retrofit) {
        super(retrofit);
        this.coastalStationConverter = new CoastalStationConverter();
    }

    @Override
    protected CoastalStation converterFixedStation(final Product product, final List<DataSource> dataSources){
        return coastalStationConverter
                .toApiModel(fixedStationConverter
                        .toApiModel(product, dataSources, FixedStation.class), CoastalStation.class);
    }
}
