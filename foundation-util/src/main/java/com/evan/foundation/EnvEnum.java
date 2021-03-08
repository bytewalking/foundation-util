package com.evan.foundation;

import lombok.Getter;

/**
 * @author evan
 * @since 2021-03-08 18:09
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

