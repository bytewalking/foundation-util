package com.evan.foundation.cache;

import com.google.common.base.Joiner;
import com.evan.foundation.util.AssertUtil;
import com.evan.foundation.util.EnvUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.IntegerCodec;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author jiangfangyuan
 * @since 2020-03-12 01:32
 */
@Slf4j
public class CountClient {


    private static final String COLON = ":";


    @Getter
    private RedissonClient redissonClient;

    private FoundationCacheConfig foundationCacheConfig;

    /**
     * 构造CountClient
     *
     * @param redissonClient
     */
    public CountClient(RedissonClient redissonClient, FoundationCacheConfig foundationCacheConfig) {
        AssertUtil.notNull(redissonClient, () -> "redissonClient null");
        AssertUtil.notNull(foundationCacheConfig, () -> "foundationCacheConfig null");
        this.redissonClient = redissonClient;
        this.foundationCacheConfig = foundationCacheConfig;
    }

    public void increase(String nameSpace, String key,
                         Supplier<Integer> fullLoadSupplier,
                         long ttl, TimeUnit timeUnit,
                         Integer deltaValue) {

        AssertUtil.notBlank(nameSpace, () -> "nameSpace empty");
        AssertUtil.notBlank(key, () -> "key empty");
        AssertUtil.notNull(fullLoadSupplier, () -> "fullLoadSupplier null");
        AssertUtil.isTrue(ttl > 0, () -> "ttl>0");
        AssertUtil.notNull(timeUnit, () -> "timeUnit null");
        AssertUtil.notNull(deltaValue, () -> "deltaValue null");

        String countKey = buildCountKey(nameSpace, key);
        try {
            RMapCache<String, Integer> mapCache = redissonClient.getMapCache(nameSpace, IntegerCodec.INSTANCE);
            Integer oldValue = mapCache.putIfAbsent(countKey, 0, ttl, timeUnit);
            if (null == oldValue) {
                mapCache.addAndGet(countKey, fullLoadSupplier.get());
                return;
            }
            mapCache.addAndGet(countKey, deltaValue);
        } catch (Exception ex) {
            log.error("increase error nameSpace" + nameSpace + " key" + key, ex);
        }
    }


    public Integer getCount(String nameSpace, String key) {

        AssertUtil.notBlank(nameSpace, () -> "nameSpace empty");
        AssertUtil.notBlank(key, () -> "key empty");

        String countKey = buildCountKey(nameSpace, key);
        try {
            RMapCache<String, Integer> mapCache = redissonClient.getMapCache(nameSpace, IntegerCodec.INSTANCE);
            return mapCache.get(countKey);
        } catch (Exception ex) {
            log.error("getCount error nameSpace" + nameSpace + " key" + key, ex);
            return null;
        }
    }

    public void putIfAbsentCount(String nameSpace, String key, Integer count, long ttl, TimeUnit timeUnit) {

        AssertUtil.notBlank(nameSpace, () -> "nameSpace empty");
        AssertUtil.notBlank(key, () -> "key empty");
        AssertUtil.notNull(count, () -> "count null");
        AssertUtil.isTrue(ttl > 0, () -> "ttl>0");
        AssertUtil.notNull(timeUnit, () -> "timeUnit null");

        String countKey = buildCountKey(nameSpace, key);
        try {
            RMapCache<String, Integer> mapCache = redissonClient.getMapCache(nameSpace, IntegerCodec.INSTANCE);
            mapCache.putIfAbsent(countKey, count, ttl, timeUnit);
        } catch (Exception ex) {
            log.error("putIfAbsentCount error nameSpace" + nameSpace + " key" + key, ex);
        }
    }


    private String buildCountKey(String nameSpace, String key) {
        return Joiner.on(COLON).join(nameSpace, key, EnvUtil.getEnv().getCode(),
                foundationCacheConfig.getCountSuffixConfig());
    }

}

