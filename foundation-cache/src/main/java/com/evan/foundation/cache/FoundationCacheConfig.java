package com.evan.foundation.cache;

import com.evan.foundation.util.LogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    }

    public String getCacheSuffixConfig() {
        return this.cacheSuffixConfig;
    }

    public String getCountSuffixConfig() {
        return this.countSuffixConfig;
    }
}
