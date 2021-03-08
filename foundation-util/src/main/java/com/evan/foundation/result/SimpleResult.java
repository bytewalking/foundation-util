package com.evan.foundation.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.evan.foundation.TraceIdAware;
import com.evan.foundation.util.NetUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author evan
 * @since 2021-03-08 18:09
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleResult<T> implements Serializable, TraceIdAware {


    private static final long serialVersionUID = -7930135262144871783L;

    /**
     * 错误码
     */
    private String errorCode = StringUtils.EMPTY;

    /**
     * 错误信息
     */
    private String errorMsg = StringUtils.EMPTY;

    /**
     * 返回结果
     */
    private T result;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 分布式链路跟踪id
     */
    private String traceId;

    /**
     * 主机名
     */
    private String hostName;


    @JsonProperty(value = "status")
    public int getStatus() {
        if (StringUtils.isBlank(errorCode)) {
            return 200;
        } else {
            return 500;
        }
    }

    @JsonProperty(value = "msg")
    public String getMsg() {
        if (StringUtils.isNotBlank(errorMsg)) {
            return errorMsg;
        } else {
            return StringUtils.EMPTY;
        }
    }

    public static <T> SimpleResult<T> success(T result) {
        SimpleResult<T> simpleResult = new SimpleResult<>();
        simpleResult.setSuccess(true);
        simpleResult.setResult(result);
        simpleResult.setHostName(NetUtil.getHostName());
        return simpleResult;
    }


    public static <T> SimpleResult<T> failure(String errorCode, String errorMsg) {
        SimpleResult<T> simpleResult = new SimpleResult<>();
        simpleResult.setSuccess(false);
        simpleResult.setErrorCode(errorCode);
        simpleResult.setErrorMsg(errorMsg);
        simpleResult.setHostName(NetUtil.getHostName());
        return simpleResult;
    }
}
