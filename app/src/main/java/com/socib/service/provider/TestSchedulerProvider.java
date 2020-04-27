package com.socib.service.provider;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestSchedulerProvider implements SchedulerProvider {

    @Override
    public Scheduler getSchedulerIo() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler getSchedulerUi() {

        return Schedulers.trampoline();
    }

    @Override
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(getSchedulerIo()).observeOn(getSchedulerIo());
    }
}
