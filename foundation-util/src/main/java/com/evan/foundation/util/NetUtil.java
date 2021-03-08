package com.evan.foundation.util;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;

/**
 * @author evan
 * @since 2021-03-08 18:09
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

