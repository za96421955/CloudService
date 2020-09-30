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
    FIXED_BASIS_20X("fixedBasis_20X", "基础20X固定策略")
    , FIXED_BASIS_50X("fixedBasis_50X", "基础50X固定策略")
    , FIXED_BASIS_75X("fixedBasis_75X", "基础75X固定策略")
    // 2张中区间拉近固定策略
    , FIXED_2_MIDRANGE_STD_20X("fixed2MidRangeSTD_20X", "2张中区间拉近(2233222)20X固定策略")
    , FIXED_2_MIDRANGE_STD_50X("fixed2MidRangeSTD_50X", "2张中区间拉近(2233222)50X固定策略")
    , FIXED_2_MIDRANGE_STD_75X("fixed2MidRangeSTD_75X", "2张中区间拉近(2233222)75X固定策略")
    // 3张中区间拉近固定策略
    , FIXED_3_MIDRANGE_STD_20X("fixed3MidRangeSTD_20X", "3张中区间拉近(2233222)20X固定策略")
    , FIXED_3_MIDRANGE_STD_50X("fixed3MidRangeSTD_50X", "3张中区间拉近(2233222)50X固定策略")
    , FIXED_3_MIDRANGE_STD_75X("fixed3MidRangeSTD_75X", "3张中区间拉近(2233222)75X固定策略")
    // 4张中区间拉近固定策略
    , FIXED_4_MIDRANGE_STD_20X("fixed4MidRangeSTD_20X", "4张中区间拉近(2233222)20X固定策略")
    , FIXED_4_MIDRANGE_STD_50X("fixed4MidRangeSTD_50X", "4张中区间拉近(2233222)50X固定策略")
    , FIXED_4_MIDRANGE_STD_75X("fixed4MidRangeSTD_75X", "4张中区间拉近(2233222)75X固定策略")
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


