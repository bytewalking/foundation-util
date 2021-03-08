package com.evan.foundation.util;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;

/**
 * @author jiangfangyuan
 * @since 2019-09-09 15:51
 */
public class NetUtil {

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            ExceptionUtil.logError(ex, () -> "getHostName error");
            return StringUtils.EMPTY;
        }
    }
}

