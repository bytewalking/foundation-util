package com.evan.foundation.error;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 错误码
 * @author jiangfangyuan
 * @since 2019-07-17 14:32
 */
@Data
@EqualsAndHashCode
@ToString
public class CommonErrorCode {

    /**
     * 错误码
     * TODO 错误码规范
     */
    private String errorCode;


    /**
     * 错误信息
     */
    private String errorMsg;

    public static CommonErrorCode create(String errorCode,String errorMsg){
        CommonErrorCode commonErrorCode = new CommonErrorCode();
        commonErrorCode.setErrorCode(errorCode);
        commonErrorCode.setErrorMsg(errorMsg);
        return commonErrorCode;
    }
}
