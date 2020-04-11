package com.socib.service.product;

import com.socib.integrationSocib.model.DataSource;
import com.socib.integrationSocib.model.Product;
import com.socib.model.FixedStation;
import com.socib.model.SeaLevelStation;
import com.socib.service.product.converter.SeaLevelStationConverter;

import java.util.List;

import retrofit2.Retrofit;

public class SeaLevelStationApiService extends FixedStationApiService{

    private SeaLevelStationConverter seaLevelStationConverter;

    public SeaLevelStationApiService(Retrofit retrofit) {
        super(retrofit);
        this.seaLevelStationConverter = new SeaLevelStationConverter();
    }

    @Override
    protected FixedStation converterFixedStation(Product product, List<DataSource> dataSources) {
        return seaLevelStationConverter
                .toApiModel(fixedStationConverter
                        .toApiModel(product,dataSources, FixedStation.class), SeaLevelStation.class);
    }
}
