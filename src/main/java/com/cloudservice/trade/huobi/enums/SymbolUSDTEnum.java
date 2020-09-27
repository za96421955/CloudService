package com.cloudservice.trade.huobi.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 交易对
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum SymbolUSDTEnum {
    BTC_USDT("btcusdt", "BTC/USDT")
    , ETH_USDT("ethusdt", "ETH/USDT")
    , TRX_USDT("trxusdt", "TRX/USDT")
    ;

    private final String value;
    private final String desc;

    SymbolUSDTEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SymbolUSDTEnum get(String value) {
        for (SymbolUSDTEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static SymbolUSDTEnum getUSDT(String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return null;
        }
        for (SymbolUSDTEnum e : values()) {
            if (e.getValue().equals(symbol.toLowerCase() + "usdt")) {
                return e;
            }
        }
        return null;
    }

}


