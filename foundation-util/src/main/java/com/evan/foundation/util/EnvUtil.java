package com.evan.foundation.util;

import com.evan.foundation.EnvEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangfangyuan
 * @since 2019-09-05 11:24
 */
public class EnvUtil {

    public static EnvEnum getEnv() {
        String hostName = NetUtil.getHostName();
        if (hostName.toLowerCase().contains("stg") || StringUtils.contains(System.getenv("ENV_NAME"), "stg")) {
            return EnvEnum.STG;
        }
        return EnvEnum.PROD;
    }
}
