package com.cloudservice.trade.huobi.enums;

/**
 * 合约计划委托触发类型
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum ContractTriggerTypeEnum {
    GE("ge", "大于等于(触发价比最新价大)")
    , LE("le", "小于(触发价比最新价小)")
    ;

    private final String value;
    private final String desc;

    ContractTriggerTypeEnum(String value, String desc) {
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
    public ContractTriggerTypeEnum getNegate() {
        if (GE.equals(this)) {
            return LE;
        }
        return GE;
    }

    public static ContractTriggerTypeEnum get(String value) {
        for (ContractTriggerTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


