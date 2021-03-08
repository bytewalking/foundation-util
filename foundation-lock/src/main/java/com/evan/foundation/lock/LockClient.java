package com.evan.foundation.lock;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.hupu.foundation.cache.CacheClient;
import com.hupu.foundation.error.CommonSystemException;
import com.hupu.foundation.util.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangfangyuan
 * @since 2019-09-09 11:44
 */
@Slf4j
public class LockClient {

    private CacheClient lockCacheClient;

    private int ttl = 300;

    /**
     * 构造函数
     *
     * @param lockCacheClient
     * @param ttl
     */
    public LockClient(CacheClient lockCacheClient, int ttl) {
        this.lockCacheClient = lockCacheClient;
        this.ttl = ttl;
    }

    /**
     * 获取缓存，默认强制更新
     *
     * @param nameSpace
     * @param key
     * @param callable
     * @param leaseTime
     * @param <T>
     * @return
     */
    public <T extends Serializable> T get(String nameSpace, String key, Callable<T> callable, int leaseTime) {
        return get(nameSpace, key, callable, leaseTime, true);
    }

    /**
     * 获取缓存
     *
     * @param nameSpace
     * @param key
     * @param callable
     * @param leaseTime
     * @param <T>
     * @return
     */
    public <T extends Serializable> T get(String nameSpace, String key, Callable<T> callable, int leaseTime, boolean forceReload) {
        return get(nameSpace, key, callable, 1, leaseTime, forceReload);
    }

    public <T extends Serializable> T get(String nameSpace, String key, Callable<T> callable, int waitTime,
                                          int leaseTime, boolean forceReload) {
        AssertUtil.notBlank(nameSpace, () -> "nameSpace empty");
        AssertUtil.notBlank(key, () -> "key empty");
        AssertUtil.notNull(callable, () -> "callable null");
        AssertUtil.isTrue(leaseTime >= 1, () -> "leaseTime must >=1");

        Stopwatch stopwatch = Stopwatch.createStarted();

        String lockKey = Joiner.on(":").join(nameSpace, key, "theLockKey", EnvUtil.getEnv().getCode());
        try {
            if (!forceReload) {
                T value = lockCacheClient.get(nameSpace, key);
                if (value != null) {
                    return value;
                }
            }

            RLock lock = lockCacheClient.getRedissonClient().getLock(lockKey);
            log.info("开始获取分布式锁 {}", lockKey);
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                LogUtil.info(log, "获取分布式锁成功 {}", lockKey);
                T value = callable.call();
                lockCacheClient.set(nameSpace, key, value, 60 * 60 * 10);
                lockCacheClient.getRedissonClient().getMap("leader").put(lockKey, NetUtil.getHostName() + ":" + DateUtils.toString(DateUtils.nowDate()));
                return value;
            }
            LogUtil.info(log, "获取分布式锁失败 {}", lockKey);
            return lockCacheClient.get(nameSpace, key, callable, 60 * 60 * 10);
        } catch (Exception ex) {
            String errorMsg = "LockClient.get error nameSpace " + nameSpace + " key " + key;
            LogUtil.error(log, errorMsg, ex);
            throw new CommonSystemException("SYSTEM_ERROR", "SYSTEM_ERROR", ex);
        } finally {
            stopwatch.stop();
            log.info("{} 耗时 {}", lockKey, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    /**
     * @param nameSpace
     * @param key
     * @param runnable
     * @param leaseTime 如果为-1表示不超时，后台启动看门狗自动续约30s
     * @param waitTime
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T extends Serializable> boolean tryLock(String nameSpace, String key, Runnable runnable, int leaseTime,
                                                    int waitTime) {
        AssertUtil.notBlank(nameSpace, () -> "nameSpace empty");
        AssertUtil.notBlank(key, () -> "key empty");
        AssertUtil.notNull(runnable, () -> "runnable null");
        AssertUtil.isTrue(leaseTime >= -1, () -> "leaseTime>=-1");
        AssertUtil.isTrue(waitTime >= 1, () -> "waitTime >= 1");

        String lockKey = Joiner.on(":").join(nameSpace, key, "tryLockKey", EnvUtil.getEnv().getCode());
        RLock lock = lockCacheClient.getRedissonClient().getLock(lockKey);
        if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
            try {
                LogUtil.info(log, "获取分布式锁成功 lockKey={}", lockKey);
                runnable.run();
                return true;
            } finally {
                try {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                        LogUtil.info(log, "释放分布式锁 lockKey={}", lockKey);
                    } else {
                        LogUtil.error(log, "释放分布式锁 当前线程不持有锁 lockKey={}", lockKey);
                    }
                } catch (Exception e) {
                    LogUtil.error(log, "释放分布式锁失败 lockKey={} {}", lockKey, e);
                }
            }
        }
        LogUtil.warn(log, "获取分布式锁失败 lockKey={}", lockKey);
        return false;
    }


    @SneakyThrows
    public void lock(String nameSpace, String key, Runnable runnable) {
        AssertUtil.notBlank(nameSpace, () -> "nameSpace empty");
        AssertUtil.notBlank(key, () -> "key empty");
        AssertUtil.notNull(runnable, () -> "runnable null");

        String lockKey = Joiner.on(":").join(nameSpace, key, "lockKey", EnvUtil.getEnv().getCode());
        RLock lock = lockCacheClient.getRedissonClient().getLock(lockKey);
        try {
            lock.lock();
            runnable.run();
        } finally {
            try {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    LogUtil.info(log, "释放分布式锁 lockKey={}", lockKey);
                } else {
                    LogUtil.error(log, "释放分布式锁 当前线程不持有锁 lockKey={}", lockKey);
                }
            } catch (Exception e) {
                LogUtil.error(log, "释放分布式锁失败 lockKey={} {}", lockKey, e);
            }
        }
    }


    /**
     * 获取所有锁
     *
     * @return
     */
    public Map<String, String> getAllLeader() {
        return lockCacheClient.getRedissonClient().getMap("leader");
    }


    /**
     * 清楚所有锁
     */
    public void clearAllLeader() {
        lockCacheClient.getRedissonClient().getMap("leader").clear();
    }

    /**
     * @return
     */
    public CacheClient getLockCacheClient() {
        return lockCacheClient;
    }

}

