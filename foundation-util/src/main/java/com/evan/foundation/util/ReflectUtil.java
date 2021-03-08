package com.evan.foundation.util;

import com.google.common.collect.Maps;
import org.mvel2.MVEL;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author evan
 * @since 2021-03-08 17:09
 */
public class ReflectUtil {

    public static Object getValue(Object obj, String fieldName) {

        AssertUtil.notNull(obj, () -> "obj null");
        AssertUtil.notBlank(fieldName, () -> "navigationPath empty");

        Map vars = Maps.newHashMap();
        vars.put(obj.getClass().getSimpleName(), obj);
        return MVEL.eval(obj.getClass().getSimpleName() + "." + fieldName, vars);
    }


    public static void setValue(Object obj, String fieldName, Object value) {
        try {
            AssertUtil.notNull(obj, () -> "obj null");
            AssertUtil.notBlank(fieldName, () -> "fieldName empty");

            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception ex) {
            throw new UnsupportedOperationException("不支持的操作 setValue obj" + obj
                    + " fieldName" + fieldName + " value" + value);
        }
    }
}

