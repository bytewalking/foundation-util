package com.evan.foundation;

import lombok.Getter;

/**
 * @author jiangfangyuan
 * @since 2019-09-05 11:24
 */
public enum EnvEnum {

    STG("STG"),

    PROD("PROD");;

    @Getter
    private String code;

    EnvEnum(String code) {
        this.code = code;
    }
}

