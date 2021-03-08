package com.evan.foundation.rate;


import com.evan.foundation.util.AssertUtil;
import org.redisson.api.RRateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author evan
 * @since 2021-03-08 18:09
 */
public class CenterRateLimiter implements RateLimiter {

    private RRateLimiter rateLimiter;

    public CenterRateLimiter(RRateLimiter rateLimiter) {
        AssertUtil.notNull(rateLimiter, () -> "rateLimiter null");
        AssertUtil.isTrue(rateLimiter.isExists(), () -> "rateLimiter 不存在");
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

