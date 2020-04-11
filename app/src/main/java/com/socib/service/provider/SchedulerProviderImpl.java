package com.socib.service.provider;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProviderImpl implements SchedulerProvider {

    @Override
    public Scheduler getSchedulerIo() {
        return Schedulers.io();
    }

    @Override
    public Scheduler getSchedulerUi() {
        return AndroidSchedulers.mainThread();
    }
}
