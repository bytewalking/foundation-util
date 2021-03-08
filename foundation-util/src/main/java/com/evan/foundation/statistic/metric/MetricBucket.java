package com.evan.foundation.statistic.metric;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author evan
 * @since 2021-03-08 18:09
 */
public class MetricBucket {

    private LongAdder counter = new LongAdder();

    public long total() {
        return counter.sum();
    }

    public void addTotal(int count){
        counter.add(count);
    }

    public void reset() {
        counter.reset();
    }
}



