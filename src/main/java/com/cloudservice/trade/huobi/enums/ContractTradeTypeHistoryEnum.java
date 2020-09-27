package com.cloudservice.trade.huobi.enums;

/**
 * 合约历史成交记录类型
 * <p></p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum ContractTradeTypeHistoryEnum {
    ALL("0", "全部")
    , BUY_OPEN("1", "买入开多")
    , SELL_CLOSE("4", "卖出平多")
    , SELL_FORCE("5", "卖出强平")
    , SELL_OPEN("2", "卖出开空")
    , BUY_CLOSE("3", "买入平空")
    , BUY_FORCE("6", "买入强平")
    ;

    private final String value;
    private final String desc;

    ContractTradeTypeHistoryEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ContractTradeTypeHistoryEnum get(String value) {
        for (ContractTradeTypeHistoryEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


