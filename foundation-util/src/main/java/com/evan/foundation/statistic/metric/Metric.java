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

/**
 * Represents a basic structure recording invocation metrics of protected resources.
 *
 * @author jialiang.linjl
 * @author Eric Zhao
 */
public interface Metric extends DebugSupport {

    /**
     * Get total count.
     *
     * @return total count
     */
    long total();


    double tps();


    /**
     * 增加总数量
     *
     * @param count
     */
    void addTotal(int count);


    public static Metric newArrayMetric(int sampleCount, int intervalInMs) {
        return new ArrayMetric(sampleCount, intervalInMs);
    }


}

