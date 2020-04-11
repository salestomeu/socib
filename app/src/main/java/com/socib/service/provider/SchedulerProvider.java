package com.socib.service.provider;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler getSchedulerIo();

    Scheduler getSchedulerUi();
}
