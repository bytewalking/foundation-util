package com.evan.foundation.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author evan
 * @since 2021-03-08
 */
@Service
@Slf4j
public class FoundationCacheConfig implements InitializingBean {
    @Value("${cache.suffix:DEFAULT}")
    private String cacheSuffixConfig;


    @Value("${count.suffix:DEFAULT}")
    private String countSuffixConfig;

    @Override
    public void afterPropertiesSet() {
        // 从阿波罗获取配置
    }

    public String getCacheSuffixConfig() {
        return this.cacheSuffixConfig;
    }

    public String getCountSuffixConfig() {
        return this.countSuffixConfig;
    }
}
