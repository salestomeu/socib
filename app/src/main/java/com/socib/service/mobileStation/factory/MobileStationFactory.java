package com.socib.service.mobileStation.factory;

import com.socib.R;
import com.socib.model.MobileStation;
import com.socib.model.StationType;

import java.util.EnumMap;
import java.util.function.Supplier;

public class MobileStationFactory {
    private EnumMap<StationType, Supplier<? extends MobileStation>> mobileStationEnumSet;

    public MobileStationFactory(){
        mobileStationEnumSet =  new EnumMap<>(StationType.class);
        mobileStationEnumSet.put(StationType.GLIDER, this::getGliderStation);
    }

    public MobileStation get(StationType stationType){
        if(!mobileStationEnumSet.containsKey(stationType)){
            return null;
        }
        return mobileStationEnumSet.get(stationType).get();
    }

    private MobileStation getGliderStation() {
        MobileStation mobileStation = new MobileStation();
        mobileStation.setIcon(R.drawable.ic_map_station);
        return mobileStation;
    }
}
