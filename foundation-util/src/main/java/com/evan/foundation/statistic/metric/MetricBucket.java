package com.evan.foundation.statistic.metric;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author jiangfangyuan
 * @since 2019-09-29 15:44
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



