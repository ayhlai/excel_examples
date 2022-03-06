package com.mycompany.xladdin;

import com.exceljava.jinx.ExcelFunction;
import com.exceljava.jinx.Rtd;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A collection of simple Excel functions.
 */
public class ExcelFunctions {

    private final ScheduledExecutorService executor;

    public ExcelFunctions() {
        this.executor = Executors.newScheduledThreadPool(4);
    }

    /***
     * Multiply two numbers and return the result.
     */
    @ExcelFunction
    public static double multiply(double x, double y) {
        return x * y;
    }

    /***
     * add 3 numbers
     */
    @ExcelFunction
    public static double addall(double x, double y, double z) {

        return x + y + z;
    }

    @ExcelFunction(
            value = "getQuotes",
            description = "Return the quotes"
    )
    public Rtd<Integer> getQuotes() {

        return new CurrentQuoteStoreRtd(executor);
    }
}