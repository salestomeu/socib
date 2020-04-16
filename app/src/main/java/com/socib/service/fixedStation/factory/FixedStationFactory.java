package com.socib.service.fixedStation.factory;

import com.socib.R;
import com.socib.model.FixedStation;
import com.socib.model.StationType;

import java.util.EnumMap;
import java.util.function.Supplier;

public class FixedStationFactory {
    private EnumMap<StationType, Supplier<? extends FixedStation>> fixedStationEnumSet;

    public FixedStationFactory(){
        fixedStationEnumSet =  new EnumMap<>(StationType.class);
        fixedStationEnumSet.put(StationType.COASTALSTATION, this::getCoastalStation);
        fixedStationEnumSet.put(StationType.SEALEVEL, this::getSeaLevelStation);
        fixedStationEnumSet.put(StationType.WEATHERSTATION, this::getWeatherStation);
    }

    public FixedStation get(StationType stationType){
        if(!fixedStationEnumSet.containsKey(stationType)){
            return null;
        }
        return fixedStationEnumSet.get(stationType).get();
    }

    private FixedStation getCoastalStation() {
        FixedStation fixedStation = new FixedStation();
        fixedStation.setIcon(R.drawable.ic_map_station);
        fixedStation.setType(StationType.COASTALSTATION.stationType());
        return fixedStation;
    }

    private FixedStation getSeaLevelStation() {
        FixedStation fixedStation = new FixedStation();
        fixedStation.setIcon(R.drawable.ic_map_sea_level);
        fixedStation.setType(StationType.SEALEVEL.stationType());
        return fixedStation;
    }

    private FixedStation getWeatherStation() {
        FixedStation fixedStation = new FixedStation();
        fixedStation.setIcon(R.drawable.ic_map_meteo);
        fixedStation.setType(StationType.WEATHERSTATION.stationType());
        return fixedStation;
    }
}
