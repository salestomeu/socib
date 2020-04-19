package com.socib.service.provider;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler getSchedulerIo();

    Scheduler getSchedulerUi();

    <T>ObservableTransformer<T, T> applySchedulers();
}
