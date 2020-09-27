package com.cloudservice.trade.huobi.enums;

/**
 * K线波动
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum KlineFluctuationEnum {
    BIG_RISE(2, "大涨")
    , RISE(1, "涨")
    , LEVEL(0, "平")
    , FALL(-1, "跌")
    , BIG_FALL(-2, "大跌")
    ;

    private final int value;
    private final String desc;

    KlineFluctuationEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static KlineFluctuationEnum get(int value) {
        for (KlineFluctuationEnum e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return null;
    }

}


