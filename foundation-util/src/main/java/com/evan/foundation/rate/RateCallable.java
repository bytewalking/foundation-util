package com.evan.foundation.rate;

import com.evan.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author jiangfangyuan
 * @since 2020-06-11 14:14
 */
@Slf4j
public class RateCallable<V> implements Callable<V> {

    private RateLimiter rateLimiter;

    private Callable<V> callable;

    public RateCallable(RateLimiter rateLimiter, Callable<V> callable) {

        AssertUtil.notNull(rateLimiter, () -> "rateLimiter null");
        AssertUtil.notNull(callable, () -> "callable null");

        this.rateLimiter = rateLimiter;
        this.callable = callable;
    }

    @Override
    public V call() throws Exception {
        if (rateLimiter.tryAcquire()) {
            return callable.call();
        }
        return null;
    }
}

