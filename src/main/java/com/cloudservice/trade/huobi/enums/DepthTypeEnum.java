package com.cloudservice.trade.huobi.enums;

/**
 * 深度价格聚合度
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum DepthTypeEnum {
    STEP_0_01("step0", "无聚合 【0.01】")
    , STEP_0_1("step1", "聚合度为报价精度*10 【0.1】")
    , STEP_1("step2", "聚合度为报价精度*100 【1】")
    , STEP_10("step3", "聚合度为报价精度*1000 【10】")
    , STEP_50("step4", "聚合度为报价精度*10000 【50】")
    , STEP_100("step5", "聚合度为报价精度*100000 【100】")
    ;

    private final String value;
    private final String desc;

    DepthTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static DepthTypeEnum get(String value) {
        for (DepthTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


