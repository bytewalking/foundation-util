package com.evan.foundation.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author evan
 * @since 2021-03-08 11:24
 */
public class ExceptionUtil {

    private static final Logger ERROR_LOGGER = LoggerFactory.getLogger(LogNames.COMMON_ERROR);


    public static void logError(Throwable ex, Supplier<String> msg){
        ERROR_LOGGER.error(msg.get(),ex);
    }

    /**
     * 打印error日志
     * @param msg
     * @param arguments
     */
    public static void logError(String msg, Object... arguments) {
        ERROR_LOGGER.error(msg, arguments);
    }

    /**
     * 获取异常的rootCause
     * @param throwable
     * @return
     */
    public static Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        return Optional.ofNullable(rootCause).orElse(throwable);
    }

}

