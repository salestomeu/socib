package com.socib.ui.glider;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GliderViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public GliderViewModel() {
        this.mText = new MutableLiveData<>();
        mText.setValue("This is glider fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
