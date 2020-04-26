package com.socib.service.mobileStation.converter;

import com.socib.commons.AbstractModelConverter;
import com.socib.model.FixedStation;
import com.socib.model.MobileStation;

public class MobileStationConverter extends AbstractModelConverter<FixedStation, MobileStation> {

   public MobileStation toDomainModel(FixedStation apiModel, Class<MobileStation> domainClass){
       return super.toDomainModel(apiModel, domainClass);
   }
}
