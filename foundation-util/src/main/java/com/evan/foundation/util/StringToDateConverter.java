package com.evan.foundation.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author evan
 * @since 2021-03-08 17:09
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String value) {

        if (StringUtils.isEmpty(value)) {
            return null;
        }
        value = value.trim();
        try {
            if (value.contains("-")) {
                SimpleDateFormat formatter;
                if (value.contains(":")) {
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                }
                return formatter.parse(value);
            }
            if (value.length() == 8) {
                return new SimpleDateFormat("yyyyMMdd").parse(value);
            }
            if (value.matches("^\\d+$")) {
                return new Date(new Long(value));
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to Date fail", value));
        }
        throw new RuntimeException(String.format("parser %s to Date fail", value));
    }
}
