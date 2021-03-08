package com.evan.foundation.util;

/**
 * @author evan
 * @since 2021-03-08 11:24
 */
public class IDUtil {

    private static final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    /**
     * 获取雪花算法生成的ID
     *
     * @return
     */
    public static String getId() {
        return String.valueOf(snowflakeIdWorker.nextId());
    }
}


