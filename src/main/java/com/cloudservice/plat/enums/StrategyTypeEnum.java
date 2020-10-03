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
    FIXED_BASIS("fixedBasis", "基础固定策略")
    , FIXED_2_INCOME_CHASE("fixed2IncomeChase", "2次收益追逐固定策略")
    , FIXED_3_INCOME_CHASE("fixed3IncomeChase", "3次收益追逐固定策略")
    , FIXED_4_INCOME_CHASE("fixed4IncomeChase", "4次收益追逐固定策略")
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


