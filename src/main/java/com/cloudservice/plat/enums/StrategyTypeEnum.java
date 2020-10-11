package com.cloudservice.plat.enums;

/**
 * 策略类型
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum StrategyTypeEnum {
    /** 固定策略 */
    FIXED_VOLUME_1("fixedVolume1", "1张固定策略")
    , FIXED_VOLUME_2("fixedVolume2", "2张固定策略")
    , FIXED_VOLUME_3("fixedVolume3", "3张固定策略")
    , FIXED_VOLUME_4("fixedVolume4", "4张固定策略")
    , FIXED_VOLUME_5("fixedVolume5", "5张固定策略")

    /** 复利策略 */
    , COMPOUND_LOW("compoundLow", "低资产复利策略")
    , COMPOUND_IN("compoundIn", "中资产复利策略")
    , COMPOUND_HIGH("compoundHigh", "高资产复利策略")
    , COMPOUND_LARGE("compoundLarge", "大额资产复利策略")
    ;

    private final String value;
    private final String desc;

    StrategyTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static StrategyTypeEnum get(String value) {
        for (StrategyTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


