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
    // 基础固定策略
    FIXED_BASIS_STEADY("fixed_basis_steady", "基础固定策略 - 稳健")
    , FIXED_BASIS_MEDIOCRE("fixed_basis_mediocre", "基础固定策略 - 中庸")
    , FIXED_BASIS_RADICAL("fixed_basis_radical", "基础固定策略 - 激进")
    // 2张中区间拉近固定策略
    , FIXED_2_2233222_STEADY("fixed_2_2233222_steady", "2张中区间拉近固定策略 - 稳健")
    , FIXED_2_2233222_MEDIOCRE("fixed_2_2233222_mediocre", "2张中区间拉近固定策略 - 中庸")
    , FIXED_2_2233222_RADICAL("fixed_2_2233222_radical", "2张中区间拉近固定策略 - 激进")
    // 3张中区间拉近固定策略
    , FIXED_3_2233222_STEADY("fixed_3_2233222_steady", "3张中区间拉近固定策略 - 稳健")
    , FIXED_3_2233222_MEDIOCRE("fixed_3_2233222_mediocre", "3张中区间拉近固定策略 - 中庸")
    , FIXED_3_2233222_RADICAL("fixed_3_2233222_radical", "3张中区间拉近固定策略 - 激进")
    // 4张中区间拉近固定策略
    , FIXED_4_2233222_STEADY("fixed_4_2233222_steady", "4张中区间拉近固定策略 - 稳健")
    , FIXED_4_2233222_MEDIOCRE("fixed_4_2233222_mediocre", "4张中区间拉近固定策略 - 中庸")
    , FIXED_4_2233222_RADICAL("fixed_4_2233222_radical", "4张中区间拉近固定策略 - 激进")
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


