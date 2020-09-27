package com.cloudservice.trade.huobi.enums;

/**
 * 合约类型
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum ContractTypeEnum {
    THIS_WEEK("this_week", "当周")
    , NEXT_WEEK("next_week", "下周")
    , QUARTER("quarter", "当季")
    , NEXT_QUARTER("next_quarter", "次季")
    ;

    private final String value;
    private final String desc;

    ContractTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ContractTypeEnum get(String value) {
        for (ContractTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


