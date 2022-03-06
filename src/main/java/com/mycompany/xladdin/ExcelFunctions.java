package com.mycompany.xladdin;

import com.exceljava.jinx.ExcelFunction;
import com.exceljava.jinx.Rtd;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A collection of simple Excel functions.
 */
public class ExcelFunctions {

    private ScheduledExecutorService executor;

    public ExcelFunctions() {
        executor = Executors.newScheduledThreadPool(4);
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
            value = "subscribe",
            description = "starts subscriber"
    )
    public static QuotesConsumer subscribe() {

        QuotesConsumer c = QuotesConsumer.getInstance().subscribe();
        return c;
    }

    @ExcelFunction(
            value = "quoteDetail",
            description = "starts subscriber"
    )
    public static String getquoteDetail(Map<String,Quote> quotes, Integer id) {

        Quote q = quotes.get(id.toString());
        return q.getStatus();
    }


}