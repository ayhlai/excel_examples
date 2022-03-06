package com.mycompany.xladdin;

import com.exceljava.jinx.ExcelFunction;
import com.exceljava.jinx.Rtd;

import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class RtdFunctions {
    private static final Logger log = Logger.getLogger(RtdFunctions.class.getName());
    private final ScheduledExecutorService executor;

    static class CurrentQuoteStoreRtd extends Rtd<Map<String,Quote>> implements Runnable {
        private final ScheduledExecutorService executor;
        private ScheduledFuture<?> future;
        QuotesConsumer consumer;

        CurrentQuoteStoreRtd(QuotesConsumer c, ScheduledExecutorService executor) {
            this.executor = executor;
            this.consumer = c;
            // call Rtd.notify with the initial value
            run();
        }


        public void run() {
            // notify Excel with the latest value
            try {
                notify(consumer.getQuotes());
            } catch (Exception e) {
                notifyError(e);
            }
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


    static class CurrentQuoteStoreSizeRtd extends Rtd<Integer> implements Runnable {
        private final ScheduledExecutorService executor;
        private ScheduledFuture<?> future;
        QuotesConsumer consumer;

        CurrentQuoteStoreSizeRtd(QuotesConsumer c, ScheduledExecutorService executor) {
            this.executor = executor;
            this.consumer = c;
            // call Rtd.notify with the initial value
            run();
        }


        public void run() {
            // notify Excel with the latest value
            try {
                notify(consumer.getQuotes().size());
            } catch (Exception e) {
                notifyError(e);
            }
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

    /**
     * The threads used are created as daemon threads so that if Excel
     * is closed while there are still threads running the process will
     * exit cleanly.
     */
    static class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    }

    public RtdFunctions() {
        this.executor = Executors.newScheduledThreadPool(4, new DaemonThreadFactory());
    }

    @ExcelFunction(
            value = "getQuotesSize",
            description = "Return the size of quotes"
    )
    public Rtd<Integer> getQuotesSize(QuotesConsumer c) {

        return new CurrentQuoteStoreSizeRtd(c,executor);
    }

    @ExcelFunction(
            value = "getQuotes",
            description = "Return the quotes"
    )
    public Rtd<Map<String,Quote>> getQuotes(QuotesConsumer c) {

        return new CurrentQuoteStoreRtd(c,executor);
    }
}
