package com.evan.foundation.statistic.metric;

/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.evan.foundation.statistic.LeapArray;

import java.util.List;

/**
 * The basic metric class in Sentinel using a {@link BucketLeapArray} internal.
 *
 * @author jialiang.linjl
 * @author Eric Zhao
 */
public class ArrayMetric implements Metric {

    private final LeapArray<MetricBucket> data;

    public ArrayMetric(int sampleCount, int intervalInMs) {
        this.data = new BucketLeapArray(sampleCount, intervalInMs);
    }

    /**
     * For unit test.
     */
    public ArrayMetric(LeapArray<MetricBucket> array) {
        this.data = array;
    }

    @Override
    public long total() {
        data.currentWindow();
        long success = 0;

        List<MetricBucket> list = data.values();
        for (MetricBucket window : list) {
            success += window.total();
        }
        return success;
    }

    @Override
    public void addTotal(int count) {
        data.currentWindow().value().addTotal(count);
    }


    /**
     * Get tps for provided event per second.
     *
     * @return tps count per second for event
     */
    @Override
    public double tps() {
        return total() / data.getIntervalInSecond();
    }


    @Override
    public void debug() {
        data.debug(System.currentTimeMillis());
    }
}

