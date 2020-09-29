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
    // 20X
    FIXED_BASIS_STEADY_20X("fixedBasisSteady_20X", "基础20X固定策略 - 稳健")
    , FIXED_BASIS_MEDIOCRE_20X("fixedBasisMediocre_20X", "基础20X固定策略 - 中庸")
    , FIXED_BASIS_RADICAL_20X("fixedBasisRadical_20X", "基础20X固定策略 - 激进")
    // 50X
    , FIXED_BASIS_STEADY_50X("fixedBasisSteady_50X", "基础50X固定策略 - 稳健")
    , FIXED_BASIS_MEDIOCRE_50X("fixedBasisMediocre_50X", "基础50X固定策略 - 中庸")
    , FIXED_BASIS_RADICAL_50X("fixedBasisRadical_50X", "基础50X固定策略 - 激进")

    // 2张中区间拉近固定策略
    // 20X
    , FIXED_2_MIDRANGE_STD_STEADY_20X("fixed2MidRangeSTDSteady_20X", "2张中区间拉近(2233222)20X固定策略 - 稳健")
    , FIXED_2_MIDRANGE_STD_MEDIOCRE_20X("fixed2MidRangeSTDMediocre_20X", "2张中区间拉近(2233222)20X固定策略 - 中庸")
    , FIXED_2_MIDRANGE_STD_RADICAL_20X("fixed2MidRangeSTDRadical_20X", "2张中区间拉近(2233222)20X固定策略 - 激进")
    // 50X
    , FIXED_2_MIDRANGE_STD_STEADY_50X("fixed2MidRangeSTDSteady_50X", "2张中区间拉近(2233222)50X固定策略 - 稳健")
    , FIXED_2_MIDRANGE_STD_MEDIOCRE_50X("fixed2MidRangeSTDMediocre_50X", "2张中区间拉近(2233222)50X固定策略 - 中庸")
    , FIXED_2_MIDRANGE_STD_RADICAL_50X("fixed2MidRangeSTDRadical_50X", "2张中区间拉近(2233222)50X固定策略 - 激进")

    // 3张中区间拉近固定策略
    // 20X
    , FIXED_3_MIDRANGE_STD_STEADY_20X("fixed3MidRangeSTDSteady_20X", "3张中区间拉近(2233222)20X固定策略 - 稳健")
    , FIXED_3_MIDRANGE_STD_MEDIOCRE_20X("fixed3MidRangeSTDMediocre_20X", "3张中区间拉近(2233222)20X固定策略 - 中庸")
    , FIXED_3_MIDRANGE_STD_RADICAL_20X("fixed3MidRangeSTDRadical_20X", "3张中区间拉近(2233222)20X固定策略 - 激进")
    // 50X
    , FIXED_3_MIDRANGE_STD_STEADY_50X("fixed3MidRangeSTDSteady_50X", "3张中区间拉近(2233222)50X固定策略 - 稳健")
    , FIXED_3_MIDRANGE_STD_MEDIOCRE_50X("fixed3MidRangeSTDMediocre_50X", "3张中区间拉近(2233222)50X固定策略 - 中庸")
    , FIXED_3_MIDRANGE_STD_RADICAL_50X("fixed3MidRangeSTDRadical_50X", "3张中区间拉近(2233222)50X固定策略 - 激进")

    // 4张中区间拉近固定策略
    // 20X
    , FIXED_4_MIDRANGE_STD_STEADY_20X("fixed4MidRangeSTDSteady_20X", "4张中区间拉近(2233222)20X固定策略 - 稳健")
    , FIXED_4_MIDRANGE_STD_MEDIOCRE_20X("fixed4MidRangeSTDMediocre_20X", "4张中区间拉近(2233222)20X固定策略 - 中庸")
    , FIXED_4_MIDRANGE_STD_RADICAL_20X("fixed4MidRangeSTDRadical_20X", "4张中区间拉近(2233222)20X固定策略 - 激进")
    // 50X
    , FIXED_4_MIDRANGE_STD_STEADY_50X("fixed4MidRangeSTDSteady_50X", "4张中区间拉近(2233222)50X固定策略 - 稳健")
    , FIXED_4_MIDRANGE_STD_MEDIOCRE_50X("fixed4MidRangeSTDMediocre_50X", "4张中区间拉近(2233222)50X固定策略 - 中庸")
    , FIXED_4_MIDRANGE_STD_RADICAL_50X("fixed4MidRangeSTDRadical_50X", "4张中区间拉近(2233222)50X固定策略 - 激进")
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


