package com.socib.service.profile;

import com.socib.model.Profile;

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

    public void setLanguage(String language){
        this.profile.setLanguage(language);
    }

    private void loadProfile() {
        this.profile = new Profile();
        profile.setLanguage("es");
        profile.setType("Scientific");
        profile.setUnits("m/s");
    }

}
