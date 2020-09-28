package com.cloudservice.trade.huobi.enums;

/**
 * 交易对: 交割合约
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum SymbolContractEnum {
    BTC_CW("BTC_CW", "BTC当周")
    , BTC_NW("BTC_NW", "BTC次周")
    , BTC_CQ("BTC_CQ", "BTC当季")
    , BTC_NQ("BTC_NQ", "BTC次季")
    , ETH_CW("ETH_CW", "ETH当周")
    , ETH_NW("ETH_NW", "ETH次周")
    , ETH_CQ("ETH_CQ", "ETH当季")
    , ETH_NQ("ETH_NQ", "ETH次季")
    ;

    private final String value;
    private final String desc;

    SymbolContractEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SymbolContractEnum get(String value) {
        for (SymbolContractEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static SymbolContractEnum get(SymbolEnum symbol, ContractTypeEnum contractType) {
        if (!(SymbolEnum.BTC.equals(symbol) || SymbolEnum.ETH.equals(symbol))) {
            return null;
        }
        if (ContractTypeEnum.THIS_WEEK.equals(contractType)) {
            return SymbolContractEnum.get(symbol.getValue() + "_CW");
        }
        if (ContractTypeEnum.NEXT_WEEK.equals(contractType)) {
            return SymbolContractEnum.get(symbol.getValue() + "_NW");
        }
        if (ContractTypeEnum.QUARTER.equals(contractType)) {
            return SymbolContractEnum.get(symbol.getValue() + "_CQ");
        }
        if (ContractTypeEnum.NEXT_QUARTER.equals(contractType)) {
            return SymbolContractEnum.get(symbol.getValue() + "_NQ");
        }
        return null;
    }

}


