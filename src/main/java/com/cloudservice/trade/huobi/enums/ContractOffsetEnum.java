package com.cloudservice.trade.huobi.enums;

/**
 * 合约开平仓
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum ContractOffsetEnum {
    OPEN("open", "开")
    , CLOSE("close", "平")
    ;

    private final String value;
    private final String desc;

    ContractOffsetEnum(String value, String desc) {
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
    public ContractOffsetEnum getNegate() {
        if (OPEN.equals(this)) {
            return CLOSE;
        }
        return OPEN;
    }

    public static ContractOffsetEnum get(String value) {
        for (ContractOffsetEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


