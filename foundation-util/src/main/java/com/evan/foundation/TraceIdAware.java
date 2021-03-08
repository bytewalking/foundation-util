package com.evan.foundation;

/**
 * @author evan
 * @since 2021-03-08 18:09
 */
public interface TraceIdAware {


    /**
     * 设置traceId
     *
     * @param traceId
     */
    public void setTraceId(String traceId);


    /**
     * 获取traceId
     *
     * @return
     */
    public String getTraceId();


}

