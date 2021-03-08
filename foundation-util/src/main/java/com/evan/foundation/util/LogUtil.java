package com.evan.foundation.util;

import org.slf4j.Logger;

/**
 * 日志工具类
 *
 * @author evan
 * @since 2021-03-08 11:24
 */
public class LogUtil {


    /**
     * 打印debug日志
     *
     * @param logger
     * @param msg
     * @param arguments
     */
    public static void debug(Logger logger, String msg, Object... arguments) {
        logger.debug(msg, arguments);
    }

    /**
     * 打印info日志
     *
     * @param logger
     * @param msg
     * @param arguments
     */
    public static void info(Logger logger, String msg, Object... arguments) {
        logger.info(msg, arguments);
    }


    /**
     * 打印warn日志
     *
     * @param logger
     * @param msg
     * @param arguments
     */
    public static void warn(Logger logger, String msg, Object... arguments) {
        logger.warn(msg, arguments);
    }

    /**
     * 打印warn日志
     *
     * @param logger
     * @param msg
     * @param e
     */
    public static void warn(Logger logger, String msg, Throwable e) {
        logger.warn(msg, msg, e);
    }


    /**
     * 打印error日志
     *
     * @param logger
     * @param msg
     * @param arguments
     */
    public static void error(Logger logger, String msg, Object... arguments) {
        logger.error(msg, arguments);
    }

    /**
     * 打印error日志
     *
     * @param logger
     * @param msg
     */
    public static void error(Logger logger, String msg,Throwable throwable) {
        logger.error(msg, throwable);
    }
}

