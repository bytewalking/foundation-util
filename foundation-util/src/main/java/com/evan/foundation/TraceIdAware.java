package com.evan.foundation;

/**
 * @author jiangfangyuan
 * @since 2019-07-28 14:41
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

