package com.evan.foundation.error;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;

public class CommonBizException extends Exception {
    private static final long serialVersionUID = -1525668676119894006L;
    private String errorCode;
    private String errorMsg;
    private Map<String, Object> extraInfoMap = new HashMap();

    public CommonBizException() {
    }

    public CommonBizException(String errorCode, String errorMsg) {
        super(String.format("code:%s msg:%s", errorCode, errorMsg));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorCode));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorMsg));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CommonBizException(String errorCode, String errorMsg, Throwable cause) {
        super(String.format("code:%s errorMsg:%s", errorCode, errorMsg), cause);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorCode));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorMsg));
        Preconditions.checkNotNull(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CommonBizException putExtra(String key, Object value) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        Preconditions.checkNotNull(value);
        this.extraInfoMap.put(key, value);
        return this;
    }

    public CommonBizException(CommonErrorCode commonErrorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = commonErrorCode.getErrorCode();
        this.errorMsg = commonErrorCode.getErrorMsg();
    }

    public CommonBizException(CommonErrorCode commonErrorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = commonErrorCode.getErrorCode();
        this.errorMsg = commonErrorCode.getErrorMsg();
    }

    public CommonBizException(CommonErrorCode commonErrorCode, Throwable cause) {
        super(cause);
        this.errorCode = commonErrorCode.getErrorCode();
        this.errorMsg = commonErrorCode.getErrorMsg();
    }

    public String getMessage() {
        return "errorCode:" + this.errorCode + " errorMsg: " + this.errorMsg + " extraInfo: " + this.extraInfoMap;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public Map<String, Object> getExtraInfoMap() {
        return this.extraInfoMap;
    }
}
