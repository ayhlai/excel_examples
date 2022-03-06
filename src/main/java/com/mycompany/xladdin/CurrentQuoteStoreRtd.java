package com.mycompany.xladdin;

import com.exceljava.jinx.Rtd;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CurrentQuoteStoreRtd extends Rtd<Integer> implements Runnable {
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> future;

    CurrentQuoteStoreRtd(ScheduledExecutorService executor) {
        this.executor = executor;

        // call Rtd.notify with the initial value
        notify(QuotesConsumer.getInstance().getQuotes().size());
    }


    public void run() {
        // notify Excel with the latest value
        notify(QuotesConsumer.getInstance().getQuotes().size());
    }

    @Override
    public void onConnected() {
        // schedule 'run' to update the value in Excel periodically
        this.future = executor.scheduleAtFixedRate(this, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDisconnected() {
        // cancel the scheduler
        if (null != future)
            future.cancel(true);
    }
}
