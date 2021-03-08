package com.evan.foundation.rate;

import java.util.concurrent.TimeUnit;

/**
 * @author evan
 * @since 2021-03-08 18:09
 */
public interface RateLimiter {


    /**
     * Acquires a permit only if one is available at the
     * time of invocation.
     *
     * <p>Acquires a permit, if one is available and returns immediately,
     * with the value {@code true},
     * reducing the number of available permits by one.
     *
     * <p>If no permit is available then this method will return
     * immediately with the value {@code false}.
     *
     * @return {@code true} if a permit was acquired and {@code false}
     * otherwise
     */
    boolean tryAcquire();

    /**
     * Acquires the given number of <code>permits</code> only if all are available at the
     * time of invocation.
     *
     * <p>Acquires a permits, if all are available and returns immediately,
     * with the value {@code true},
     * reducing the number of available permits by given number of permits.
     *
     * <p>If no permits are available then this method will return
     * immediately with the value {@code false}.
     *
     * @param permits the number of permits to acquire
     * @return {@code true} if a permit was acquired and {@code false}
     * otherwise
     */
    boolean tryAcquire(int permits);

    /**
     * Acquires a permit from this RateLimiter, blocking until one is available.
     *
     * <p>Acquires a permit, if one is available and returns immediately,
     * reducing the number of available permits by one.
     */
    void acquire();


    /**
     * Acquires a specified <code>permits</code> from this RateLimiter,
     * blocking until one is available.
     *
     * <p>Acquires the given number of permits, if they are available
     * and returns immediately, reducing the number of available permits
     * by the given amount.
     *
     * @param permits the number of permits to acquire
     */
    void acquire(int permits);


    /**
     * Acquires a permit from this RateLimiter, if one becomes available
     * within the given waiting time.
     *
     * <p>Acquires a permit, if one is available and returns immediately,
     * with the value {@code true},
     * reducing the number of available permits by one.
     *
     * <p>If no permit is available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until
     * specified waiting time elapses.
     *
     * <p>If a permit is acquired then the value {@code true} is returned.
     *
     * <p>If the specified waiting time elapses then the value {@code false}
     * is returned.  If the time is less than or equal to zero, the method
     * will not wait at all.
     *
     * @param timeout the maximum time to wait for a permit
     * @param unit    the time unit of the {@code timeout} argument
     * @return {@code true} if a permit was acquired and {@code false}
     * if the waiting time elapsed before a permit was acquired
     */
    boolean tryAcquire(int timeout, TimeUnit unit);

    /**
     * Acquires the given number of <code>permits</code> only if all are available
     * within the given waiting time.
     *
     * <p>Acquires the given number of permits, if all are available and returns immediately,
     * with the value {@code true}, reducing the number of available permits by one.
     *
     * <p>If no permit is available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until
     * the specified waiting time elapses.
     *
     * <p>If a permits is acquired then the value {@code true} is returned.
     *
     * <p>If the specified waiting time elapses then the value {@code false}
     * is returned.  If the time is less than or equal to zero, the method
     * will not wait at all.
     *
     * @param permits amount
     * @param timeout the maximum time to wait for a permit
     * @param unit    the time unit of the {@code timeout} argument
     * @return {@code true} if a permit was acquired and {@code false}
     * if the waiting time elapsed before a permit was acquired
     */
    boolean tryAcquire(int permits, long timeout, TimeUnit unit);


}

