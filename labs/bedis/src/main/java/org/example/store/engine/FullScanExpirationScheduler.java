package org.example.store.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FullScanExpirationScheduler implements ExpirationScheduler{
    private final StoreEngine storeEngine;
    private final ScheduledExecutorService scheduler;
    public FullScanExpirationScheduler (StoreEngine storeEngine) {
        this.storeEngine = storeEngine;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }
    @Override
    public void start() {
        scheduler.scheduleAtFixedRate(
                storeEngine::cleanup,
                100,100, TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void stop() {
        scheduler.shutdown();
    }
}
