package com.cloudservice.trade.huobi.enums;

/**
 * 合约杠杆倍数
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum ContractLeverRateEnum {
    LEVER_1("1", "1倍")
    , LEVER_5("5", "5倍")
    , LEVER_10("10", "10倍")
    , LEVER_20("20", "20倍")
    , LEVER_30("30", "30倍")
    , LEVER_50("50", "50倍")
    , LEVER_75("75", "75倍")
    , LEVER_100("100", "100倍")
    ;

    private final String value;
    private final String desc;

    ContractLeverRateEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ContractLeverRateEnum get(String value) {
        for (ContractLeverRateEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


