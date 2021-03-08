package com.evan.foundation.error;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统异常
 *
 * @author jiangfangyuan
 * @since 2019-07-17 14:29
 */
public class CommonSystemException extends RuntimeException {

    private static final long serialVersionUID = 6922631004492329389L;

    @Getter
    private String errorCode;

    @Getter
    private String errorMsg;

    @Getter
    private Map<String, Object> extraInfoMap = new HashMap<>();

    /**
     * Constructs a {@code CommonBizException} with no detail message.
     */
    public CommonSystemException() {
    }

    /**
     * Constructs a {@code CommonSystemException} with the specified
     * detail message.
     *
     * @param errorMsg the detail message.
     */
    public CommonSystemException(String errorCode, String errorMsg) {
        super(String.format("code:%s msg:%s", errorCode, errorMsg));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorCode));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorMsg));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CommonSystemException(String errorCode, String errorMsg, Throwable cause) {
        super(String.format("code:%s msg:%s", errorCode, errorMsg), cause);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorCode));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorMsg));
        Preconditions.checkNotNull(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CommonSystemException(CommonErrorCode commonErrorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = commonErrorCode.getErrorCode();
        this.errorMsg = commonErrorCode.getErrorMsg();
    }

    public CommonSystemException(CommonErrorCode commonErrorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = commonErrorCode.getErrorCode();
        this.errorMsg = commonErrorCode.getErrorMsg();
    }

    public CommonSystemException(CommonErrorCode commonErrorCode, Throwable cause) {
        super(cause);
        this.errorCode = commonErrorCode.getErrorCode();
        this.errorMsg = commonErrorCode.getErrorMsg();
    }


    public CommonSystemException putExtra(String key, Object value) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        Preconditions.checkNotNull(value);
        this.extraInfoMap.put(key, value);
        return this;
    }

    public CommonSystemException putAllExtra(Map<String, Object> extraInfoMap) {
        if (extraInfoMap != null && !extraInfoMap.isEmpty()) {
            this.extraInfoMap.putAll(extraInfoMap);
        }
        return this;
    }

    @Override
    public String getMessage() {
        return "errorCode:" + errorCode + " errorMsg: " + errorMsg + " extraInfo: " + extraInfoMap;
    }
}

