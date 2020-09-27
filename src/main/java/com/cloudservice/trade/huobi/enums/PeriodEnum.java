package com.cloudservice.trade.huobi.enums;

import java.math.BigDecimal;

/**
 * 时间粒度
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum PeriodEnum {
    MIN_1("1min", "1分钟")
    , MIN_5("5min", "5分钟")
    , MIN_15("15min", "15分钟")
    , MIN_30("30min", "30分钟")
    , MIN_60("60min", "60分钟")
    , HOUR_4("4hour", "4小时")
    , DAY_1("1day", "1天")
    , MONTH_1("1mon", "1月")
    , WEEK_1("1week", "1周")
    , YEAR_1("1year", "1年")
    ;

    private final String value;
    private final String desc;

    PeriodEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public long second() {
        if (this.equals(MIN_1)) {
            return 60;
        }
        if (this.equals(MIN_5)) {
            return 5 * MIN_1.second();
        }
        if (this.equals(MIN_15)) {
            return 15 * MIN_1.second();
        }
        if (this.equals(MIN_30)) {
            return 30 * MIN_1.second();
        }
        if (this.equals(MIN_60)) {
            return 60 * MIN_1.second();
        }
        if (this.equals(HOUR_4)) {
            return 4 * MIN_60.second();
        }
        if (this.equals(DAY_1)) {
            return 24 * MIN_60.second();
        }
        if (this.equals(MONTH_1)) {
            return 30 * DAY_1.second();
        }
        if (this.equals(WEEK_1)) {
            return 7 * DAY_1.second();
        }
        if (this.equals(YEAR_1)) {
            return 365 * DAY_1.second();
        }
        return 0;
    }

    /**
     * @description 获取时间片调整价
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 17:43
     **/
    public BigDecimal getAdjustmentPrice() {
        if (this.equals(MIN_1)) {
            return new BigDecimal("0.01");
        }
        if (this.equals(MIN_5)) {
            return new BigDecimal("0.05");
        }
        return new BigDecimal("0.1");
    }

    public static PeriodEnum get(String value) {
        for (PeriodEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


