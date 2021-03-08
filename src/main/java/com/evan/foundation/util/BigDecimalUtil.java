package com.evan.foundation.util;

import com.evan.foundation.Globals;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * @author evan
 * @since 2021-03-08 11:24
 */
public class BigDecimalUtil {

    private static final int ONE_DECIMAL = 1;

    private static final int THREE_DECIMAL = 3;

    /**
     * @param d1           被除数
     * @param d2           除数
     * @param scale        保留小数位数
     * @param roundingMode BigDecimal.ROUND_HALF_UP
     * @return 商
     */
    public static BigDecimal dividePossibleZero(BigDecimal d1, BigDecimal d2, int scale, int roundingMode) {


        if (Objects.isNull(d1) || Objects.isNull(d2)) {
            return null;
        }

        if (d1.equals(BigDecimal.ZERO) || d2.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return d1.divide(d2, scale, roundingMode);
    }

    /**
     * @param d1 被除数
     * @param d2 除数
     * @return 商
     */
    public static BigDecimal dividePossibleZero(BigDecimal d1, BigDecimal d2) {
        return dividePossibleZero(d1, d2, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @param d1 被除数
     * @param d2 除数
     * @return 商
     */
    public static BigDecimal dividePossibleZero(Integer d1, BigDecimal d2) {
        return dividePossibleZero(BigDecimal.valueOf(d1), d2, 2, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * @param d1 被除数
     * @param d2 除数
     * @return 商
     */
    public static BigDecimal dividePossibleZeroThree(Integer d1, Integer d2) {
        return dividePossibleZero(BigDecimal.valueOf(d1), BigDecimal.valueOf(d2), 3, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * @param d1 被除数
     * @param d2 除数
     * @return 商
     */
    public static BigDecimal dividePossibleZeroThree(BigDecimal d1, Integer d2) {
        return dividePossibleZero(d1, BigDecimal.valueOf(d2), 3, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * @param d1 被除数
     * @param d2 除数
     * @return 商
     */
    public static BigDecimal dividePossibleZero(Integer d1, Integer d2) {
        return dividePossibleZero(BigDecimal.valueOf(d1), BigDecimal.valueOf(d2), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @param d1 被除数
     * @param d2 除数
     * @return 商
     */
    public static BigDecimal dividePossibleZero(BigDecimal d1, Integer d2) {
        return dividePossibleZero(d1, BigDecimal.valueOf(d2), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入返回1位小数
     *
     * @param d1 被除数
     * @param d2 除数
     * @return 商
     */
    public static BigDecimal divideReturnOne(BigDecimal d1, BigDecimal d2) {
        return dividePossibleZero(d1, d2, ONE_DECIMAL, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入返回1位小数 若数据非零但四舍五入后为0，将返回0.1作为保底条件
     *
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal divideReturnOneWithGuarantee(BigDecimal d1, BigDecimal d2) {
        BigDecimal result = divideReturnOne(d1, d2);
        if (result.compareTo(BigDecimal.ZERO) == 0 && d1.compareTo(BigDecimal.ZERO) > 0) {
            return BigDecimal.valueOf(0.1);
        }
        return result;
    }

    /**
     * 四舍五入返回3位小数
     *
     * @param d1 被除数
     * @param d2 除数
     * @return 商
     */
    public static BigDecimal divideReturnThree(BigDecimal d1, BigDecimal d2) {
        return dividePossibleZero(d1, d2, THREE_DECIMAL, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入返回3位小数 若数据非零但四舍五入后为0，将返回0.001作为保底条件
     *
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal divideReturnThreeWithGuarantee(BigDecimal d1, BigDecimal d2) {
        BigDecimal result = divideReturnThree(d1, d2);
        if (result.compareTo(BigDecimal.ZERO) == 0 && d1.compareTo(BigDecimal.ZERO) > 0) {
            return BigDecimal.valueOf(0.001);
        }
        return result;
    }

    public static BigDecimal convertToDecimalOrNull(Integer value) {
        return null == value ? null : BigDecimal.valueOf(value);
    }

    public static BigDecimal convertToDecimalOrNull(Double value) {
        return null == value ? null : BigDecimal.valueOf(value);
    }

    public static BigDecimal convertToDecimalOrNull(Float value) {
        return null == value ? null : BigDecimal.valueOf(value);
    }

    public static Double getDoubleValue(BigDecimal bigDecimal) {
        return Optional.ofNullable(bigDecimal).map(item -> item.doubleValue()).orElse(null);
    }

    /**
     * 进行通用的四舍五入保留小数操作
     *
     * @param decimal
     * @param scale   保留范围
     * @return
     */
    public static BigDecimal setScaleRoundHalfUp(BigDecimal decimal, int scale) {
        return null == decimal ? null : decimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }


    public static boolean equals(BigDecimal one, BigDecimal two) {
        if (one == null && two == null) {
            return true;
        }
        if (one == null) {
            return false;
        }
        if (two == null) {
            return false;
        }
        return one.doubleValue() == two.doubleValue();
    }

    public static boolean equalsZero(BigDecimal decimal) {
        if (null == decimal) {
            return false;
        }
        return decimal.setScale(3, BigDecimal.ROUND_HALF_UP)
                .equals(BigDecimal.ZERO.setScale(3, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean isBigger(BigDecimal one, BigDecimal two) {
        return one.compareTo(two) > Globals.ZERO;
    }

    public static boolean greaterThan(BigDecimal one, BigDecimal two) {
        return isBigger(one, two) || equals(one, two);
    }
}

