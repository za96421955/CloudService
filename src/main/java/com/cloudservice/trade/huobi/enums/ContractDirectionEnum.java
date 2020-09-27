package com.cloudservice.trade.huobi.enums;

/**
 * 合约买卖
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum ContractDirectionEnum {
    BUY("buy", "买")
    , SELL("sell", "卖")
    ;

    private final String value;
    private final String desc;

    ContractDirectionEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * @description 取反
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/11 0:05
     **/
    public ContractDirectionEnum getNegate() {
        if (BUY.equals(this)) {
            return SELL;
        }
        return BUY;
    }

    public static ContractDirectionEnum get(String value) {
        for (ContractDirectionEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


