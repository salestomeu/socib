package com.socib.service.fixedStation.factory;

import com.socib.R;
import com.socib.model.FixedStation;
import com.socib.model.StationType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixedStationFactoryTest {

    private FixedStationFactory fixedStationFactory;

    @Before
    public void setUp() {
        fixedStationFactory = new FixedStationFactory();
    }

    @Test
    public void it_should_be_coastal_station() {
        FixedStation coastalStation = fixedStationFactory.get(StationType.COASTALSTATION);
        Assert.assertEquals(R.drawable.ic_map_station, coastalStation.getIcon());
        Assert.assertEquals(StationType.COASTALSTATION.stationType(), coastalStation.getType());
    }

    @Test
    public void it_should_be_sea_level_station() {
        FixedStation seaLevelStation = fixedStationFactory.get(StationType.SEALEVEL);
        Assert.assertEquals(R.drawable.ic_map_sea_level, seaLevelStation.getIcon());
        Assert.assertEquals(StationType.SEALEVEL.stationType(), seaLevelStation.getType());
    }

    @Test
    public void it_should_be_weather_station() {
        FixedStation weatherStation = fixedStationFactory.get(StationType.WEATHERSTATION);
        Assert.assertEquals(R.drawable.ic_map_meteo, weatherStation.getIcon());
        Assert.assertEquals(StationType.WEATHERSTATION.stationType(), weatherStation.getType());
    }
}
