package com.evan.foundation.rate;

import com.evan.foundation.util.AssertUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangfangyuan
 * @since 2020-06-11 14:19
 */
public class LocalRateLimiter implements RateLimiter {

    private com.google.common.util.concurrent.RateLimiter rateLimiter;

    public LocalRateLimiter(com.google.common.util.concurrent.RateLimiter rateLimiter) {
        AssertUtil.notNull(rateLimiter, () -> "rateLimiter null");
        this.rateLimiter = rateLimiter;
    }

    @Override
    public boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }

    @Override
    public boolean tryAcquire(int permits) {
        return rateLimiter.tryAcquire(permits);
    }

    @Override
    public void acquire() {
        rateLimiter.acquire();
    }

    @Override
    public void acquire(int permits) {
        rateLimiter.acquire(permits);
    }

    @Override
    public boolean tryAcquire(int timeout, TimeUnit unit) {
        return rateLimiter.tryAcquire(timeout, unit);
    }

    @Override
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        return rateLimiter.tryAcquire(permits, timeout, unit);
    }
}

