package com.socib.service.profile;

import com.socib.model.Profile;
import com.socib.model.UnitType;

public class ProfileService {

    private static ProfileService instance;

    private Profile profile;

    private ProfileService(){
        loadProfile();
    }

    public static ProfileService getInstance(){
        if (instance == null){
            instance = new ProfileService();
        }
        return instance;
    }

    public Profile getProfile(){
        return profile;
    }


    private void loadProfile() {
        this.profile = new Profile();
        profile.setType("Scientific");
        profile.setUnits(UnitType.METERS_SECONDS);
    }

}
