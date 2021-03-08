package com.evan.foundation.util;

import com.evan.foundation.EnvEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author evan
 * @since 2021-03-08 18:09
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
