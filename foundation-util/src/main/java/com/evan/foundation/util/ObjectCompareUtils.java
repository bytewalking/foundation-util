package com.evan.foundation.util;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 对象比较工具类
 *
 * @Author evan
 * @Description
 * @since 2021-03-08 17:09
 * **/
public class ObjectCompareUtils {

    /**
     * todo 待补充
     * 基础字段集合
     */
    private static final Set<String> basicTypeSet = new HashSet<>(Arrays.asList(
            "java.util.Date",
            "java.math.BigDecimal",
            "java.lang.Integer",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.Byte",
            "java.lang.Boolean",
            "java.lang.Character",
            "java.lang.String",
            "int", "double", "long", "short", "byte", "boolean", "char", "float"));


    /**
     *
     *
     * 比较同一类型两个对象忽略掉非空属性
     *      * 1,仅current中非空字段参与比较
     *      * 2,基础字段直接通过equals()比较
     *      * 3，BigDecimal特殊处理，current精度以original为准
     *      * 4,非基础字段递归比较
     *      * 5，过滤掉被忽略的字段。
     * @param toBeSyncModel 待比较的model
     * @param originalModel 原始的model
     * @param ignoreFieldSet 忽略比较的字段集合 注意！！！一旦某字段被添加到该集合中，在各个层次都将被忽略
     * @param <T>
     * @return
     */
    public static <T> boolean compareIgnoreNullField(T toBeSyncModel, T originalModel, Set<String> ignoreFieldSet) {
        AssertUtil.notNull(toBeSyncModel, () -> "current is null");
        if (null == originalModel) {
            return false;
        }
        Class clazz = toBeSyncModel.getClass();
        //基本类型
        if (basicTypeSet.contains(clazz.getName())) {
            if (toBeSyncModel instanceof BigDecimal && originalModel instanceof BigDecimal) {
                int scale = ((BigDecimal) originalModel).scale();
                BigDecimal currentValueWithScale = ((BigDecimal) toBeSyncModel).setScale(scale, BigDecimal.ROUND_HALF_UP);
                return currentValueWithScale.compareTo((BigDecimal) originalModel) == 0;
            }
            return toBeSyncModel.equals(originalModel);
        }
        //集合类型待处理 todo 待优化， 暂不处理
        if (toBeSyncModel instanceof Collection) {
            return ((Collection) toBeSyncModel).isEmpty();
        }
        if (toBeSyncModel instanceof Map) {
            return ((Map) toBeSyncModel).isEmpty();
        }
        //非基本类型
        Field[] fields = clazz.getDeclaredFields();
        //过滤静态属性
        List<Field> fieldList = Arrays.stream(fields).filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
        for (Field field : fieldList) {
            //过滤掉忽略比较的字段
            if (ignoreFieldSet.contains(field.getName())) {
                continue;
            }
            Object currentValue = ReflectUtil.getValue(toBeSyncModel, field.getName());
            Object originalValue = ReflectUtil.getValue(originalModel, field.getName());
            if (null != currentValue) {  //空值字段不参与比较
                if (!compareIgnoreNullField(currentValue, originalValue, ignoreFieldSet)) {
                    return false;
                }
            }
        }
        return true;
    }
}
