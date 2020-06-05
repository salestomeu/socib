package com.socib.ui.research;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResearchViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ResearchViewModel() {
        this.mText = new MutableLiveData<>();
        mText.setValue("This is Research Vessel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
