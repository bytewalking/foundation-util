package com.evan.foundation;

/**
 * @author evan
 * @since 2021-03-08 18:09
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

