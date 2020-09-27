package com.cloudservice.trade.huobi.enums;

/**
 * 交易对
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum SymbolEnum {
    BTC("BTC", "BTC")
    , ETH("ETH", "ETH")
    , TRX("TRX", "TRX")
    , USDT("USDT", "USDT")
    ;

    private final String value;
    private final String desc;

    SymbolEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SymbolEnum get(String value) {
        for (SymbolEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


