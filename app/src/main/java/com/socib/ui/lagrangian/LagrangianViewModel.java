package com.socib.ui.lagrangian;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LagrangianViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public LagrangianViewModel() {
        this.mText = new MutableLiveData<>();
        mText.setValue("This is lagrangian fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
