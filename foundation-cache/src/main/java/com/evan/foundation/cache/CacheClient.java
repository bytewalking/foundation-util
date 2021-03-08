package com.evan.foundation.cache;

import com.google.common.base.Joiner;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.evan.foundation.error.CommonErrorCode;
import com.evan.foundation.error.CommonSystemException;
import com.evan.foundation.statistic.metric.Metric;
import com.evan.foundation.util.AssertUtil;
import com.evan.foundation.util.EnvUtil;
import com.evan.foundation.util.ExceptionUtil;
import com.evan.foundation.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 缓存客户端
 *
 * @author jiangfangyuan
 * @since 2019-07-09 18:09
 */
@Slf4j
public class CacheClient {

    private static final Logger ERROR_LOGGER = LoggerFactory.getLogger("COMMON-ERROR");

    private static final int ONE = 1;
    private static final int TWO = 2;

    private static final int FIVE = 5;

    private static final int NUM_500 = 500;


    private static final int NUM_100 = 100;

    private static final int NUM_1000 = 1000;
    private static final int NUM_6000 = 6000;


    private static final String COLON = ":";

    /**
     * 锁cache
     */
    private Cache<String, Serializable> lockCache =
            CacheBuilder.newBuilder().maximumSize(NUM_500).expireAfterWrite(ONE, TimeUnit.SECONDS).build();

    /**
     * 热点缓存 cache
     */
    private Cache<String, Serializable> hotKeyCache =
            CacheBuilder.newBuilder().maximumSize(NUM_6000).expireAfterWrite(FIVE, TimeUnit.SECONDS).build();

    /**
     * redisson 客户端
     */
    private RedissonClient redissonClient;

    /**
     * 热点统计map
     */
    private Map<String, Metric> hotKeyStatisticsMap = CacheBuilder.newBuilder().maximumSize(500).<String, Metric>build().asMap();

    private FoundationCacheConfig foundationCacheConfig;

    /**
     * 构造CacheClient
     *
     * @param redissonClient
     */
    public CacheClient(RedissonClient redissonClient, FoundationCacheConfig foundationCacheConfig) {
        AssertUtil.notNull(redissonClient, () -> "redissonClient null");
        AssertUtil.notNull(foundationCacheConfig, () -> "foundationCacheConfig null");
        this.redissonClient = redissonClient;
        this.foundationCacheConfig = foundationCacheConfig;
    }

    /**
     * 数据放入缓存 永不过期
     *
     * @param nameSpace 命名空间
     * @param key       缓存key
     * @param value     缓存value
     */
    public void setForever(String nameSpace, String key, Serializable value) {

        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");

        try {
            redissonClient.getBucket(buildCacheKey(nameSpace, key)).set(value);
        } catch (Exception ex) {
            String msg = MessageFormat.format("set error {0}", buildCacheKey(nameSpace, key));
            LogUtil.error(ERROR_LOGGER, msg, ex);
        }
    }

    /**
     * 获取使用的redisClient
     *
     * @return
     */
    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    /**
     * 数据放入缓存
     *
     * @param nameSpace  命名空间
     * @param key        缓存key
     * @param value      缓存value
     * @param timeToLive 过期时间 秒为单位
     */
    public void set(String nameSpace, String key, Serializable value, int timeToLive) {

        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");
        AssertUtil.isTrue(timeToLive >= ONE, () -> "过期时间必须>=1秒");

        try {
            redissonClient.getBucket(buildCacheKey(nameSpace, key)).set(value, timeToLive, TimeUnit.SECONDS);
        } catch (Exception ex) {
            String msg = MessageFormat.format("set error {0}", buildCacheKey(nameSpace, key));
            LogUtil.error(ERROR_LOGGER, msg, ex);
        }
    }

    /**
     * 没有自定义key后缀
     *
     * @param nameSpace
     * @param key
     * @param value
     * @param timeToLive
     */
    public void setNoSuffix(String nameSpace, String key, Serializable value, int timeToLive) {

        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");
        AssertUtil.isTrue(timeToLive >= ONE, () -> "过期时间必须>=1秒");

        try {
            redissonClient.getBucket(buildNoSuffixCacheKey(nameSpace, key)).set(value, timeToLive, TimeUnit.SECONDS);
        } catch (Exception ex) {
            String msg = MessageFormat.format("set error {0}", buildNoSuffixCacheKey(nameSpace, key));
            LogUtil.error(ERROR_LOGGER, msg, ex);
        }
    }

    /**
     * 从缓存获取数据
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String nameSpace, String key) {

        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");

        try {
            return (T) redissonClient.getBucket(buildCacheKey(nameSpace, key)).get();
        } catch (Exception ex) {
            String msg = MessageFormat.format("get error {0}", buildCacheKey(nameSpace, key));
            LogUtil.error(ERROR_LOGGER, msg, ex);
        }
        return null;
    }

    public <T> T getNoSuffix(String nameSpace, String key) {
        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");

        try {
            return (T) redissonClient.getBucket(buildNoSuffixCacheKey(nameSpace, key)).get();
        } catch (Exception ex) {
            String msg = MessageFormat.format("get error {0}", buildNoSuffixCacheKey(nameSpace, key));
            LogUtil.error(ERROR_LOGGER, msg, ex);
        }
        return null;
    }

    /**
     * 从缓存移除数据
     *
     * @param key 缓存key
     */
    public void removeNoSuffix(String nameSpace, String key) {

        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");

        try {
            redissonClient.getBucket(buildNoSuffixCacheKey(nameSpace, key)).delete();
        } catch (Exception ex) {
            String msg = MessageFormat.format("remove error {0}", buildNoSuffixCacheKey(nameSpace, key));
            LogUtil.error(ERROR_LOGGER, msg, key);
        }
    }

    /**
     * 从缓存移除数据
     *
     * @param key 缓存key
     */
    public void remove(String nameSpace, String key) {

        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");

        try {
            redissonClient.getBucket(buildCacheKey(nameSpace, key)).delete();
        } catch (Exception ex) {
            String msg = MessageFormat.format("remove error {0}", buildCacheKey(nameSpace, key));
            LogUtil.error(ERROR_LOGGER, msg, key);
        }

    }

    /**
     * 从缓存获取数据 如果获取失败 从底层数据源回源,放入缓存
     * <ul>
     * <li>优先本地缓存</li>
     * <li>其次redis</li>
     * <li>其次回源</li>
     * <li>放入本地缓存 redis</li>
     * </ul>
     *
     * @param key
     * @param callable
     * @param timeToLive
     * @param <T>
     * @return
     */
    public <T extends Serializable> T get(String nameSpace, String key, Callable<T> callable, int timeToLive) {

        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");
        AssertUtil.notNull(callable, () -> "callable null");
        AssertUtil.isTrue(timeToLive >= ONE, () -> "过期时间必须>=1秒");

        String cacheKey = buildCacheKey(nameSpace, key);
        Metric statisticsMetric = hotKeyStatisticsMap.computeIfAbsent(cacheKey, item -> Metric.newArrayMetric(TWO,
                NUM_1000));

        T result = (T) hotKeyCache.getIfPresent(cacheKey);
        if (result == null) {
            result = innerGet(nameSpace, key, callable, timeToLive);
        }

        statisticsMetric.addTotal(ONE);
        if (statisticsMetric.tps() >= NUM_100) {
            LogUtil.warn(log, "检测到热点key{} tps {}", cacheKey, statisticsMetric.tps());
            if (result != null) {
                hotKeyCache.put(cacheKey, result);
            }
        }
        return result;
    }

    /**
     * 查询缓存，如果没有就设置缓存，没有自定义key后缀
     *
     * @param nameSpace
     * @param key
     * @param callable
     * @param timeToLive
     * @param <T>
     * @return
     */
    public <T extends Serializable> T getNoSuffix(String nameSpace, String key, Callable<T> callable, int timeToLive) {

        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");
        AssertUtil.notNull(callable, () -> "callable null");
        AssertUtil.isTrue(timeToLive >= ONE, () -> "过期时间必须>=1秒");

        String cacheKey = buildNoSuffixCacheKey(nameSpace, key);
        try {
            T value = (T) redissonClient.getBucket(cacheKey).get();
            if (value == null) {
                value = (T) lockCache.get(cacheKey, callable);
                setNoSuffix(nameSpace, key, value, timeToLive);
            }
            return value;
        } catch (Throwable ex) {
            Throwable rootCause = ExceptionUtil.getRootCause(ex);
            if (CacheLoader.InvalidCacheLoadException.class.isInstance(rootCause)) {
                return null;
            }
            String msg = MessageFormat.format("get error {0}", buildNoSuffixCacheKey(nameSpace, key));
            LogUtil.error(ERROR_LOGGER, msg, ex);
        }
        return null;
    }

    private String buildCacheKey(String nameSpace, String key) {
        return Joiner.on(COLON).join(nameSpace, key, EnvUtil.getEnv().getCode(),
                foundationCacheConfig.getCacheSuffixConfig());
    }

    private String buildNoSuffixCacheKey(String nameSpace, String key) {
        return Joiner.on(COLON).join(nameSpace, key, EnvUtil.getEnv().getCode());
    }


    /**
     * 从缓存获取数据 如果获取失败 从底层数据源回源,放入缓存
     * <ul>
     * <li>优先本地缓存</li>
     * <li>其次redis</li>
     * <li>其次回源</li>
     * <li>放入本地缓存 redis</li>
     * </ul>
     *
     * @param key
     * @param callable
     * @param timeToLive
     * @param <T>
     * @return
     */
    private <T extends Serializable> T innerGet(String nameSpace, String key, Callable<T> callable,
                                                int timeToLive) {

        Serializable value = get(nameSpace, key);
        if (value != null) {
            return (T) value;
        }
        value = fetchSource(nameSpace, key, callable);
        set(nameSpace, key, value, timeToLive);
        return (T) value;
    }

    /**
     * 回源<br>
     * 并发情况下只有单线程回源
     *
     * @param nameSpace
     * @param key
     * @param callable
     * @param <T>
     * @return
     */
    private <T extends Serializable> T fetchSource(String nameSpace, String key, Callable<T> callable) {

        try {
            T value = (T) lockCache.get(buildCacheKey(nameSpace, key), callable);
            return value;
        } catch (Throwable ex) {
            Throwable rootCause = ExceptionUtil.getRootCause(ex);
            if (CacheLoader.InvalidCacheLoadException.class.isInstance(rootCause)) {
                return null;
            }
            throw new CommonSystemException(CommonErrorCode.create("FETCH_SOURCE_ERROR",
                    buildCacheKey(nameSpace, key)), rootCause);
        }
    }

    /**
     * 利用SETPXNX命令，原子的进行比较非空设值的操作
     *
     * @param nameSpace  nameSpace
     * @param key        key
     * @param value      value
     * @param timeToLive 有效时间 单位秒
     * @return 是否成功
     */
    public boolean trySet(String nameSpace, String key, Serializable value, int timeToLive) {
        AssertUtil.notBlank(nameSpace, () -> "缓存nameSpace empty");
        AssertUtil.notBlank(key, () -> "缓存key empty");
        AssertUtil.isTrue(timeToLive >= ONE, () -> "过期时间必须>=1秒");

        try {
            return redissonClient.getBucket(buildCacheKey(nameSpace, key)).trySet(value, timeToLive, TimeUnit.SECONDS);
        } catch (Exception ex) {
            String msg = MessageFormat.format("set error {0}", buildCacheKey(nameSpace, key));
            LogUtil.error(ERROR_LOGGER, msg, ex);
            return false;
        }
    }

}

