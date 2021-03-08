package com.evan.foundation;

/**
 * @author jiangfangyuan
 * @since 2020-06-11 11:05
 */
public interface CursorAble {

    /**
     * 获取游标
     *
     * @param
     * @return
     */
    <T> T getCursor();
}

