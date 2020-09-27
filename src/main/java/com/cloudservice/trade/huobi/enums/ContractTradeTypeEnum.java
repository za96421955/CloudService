package com.cloudservice.trade.huobi.enums;

/**
 * 合约交易类型
 * <p>
 *     平多：3
 *     平空：4
 *     开仓手续费-吃单：5
 *     开仓手续费-挂单：6
 *     平仓手续费-吃单：7
 *     平仓手续费-挂单：8
 *     交割平多：9
 *     交割平空：10
 *     交割手续费：11
 *     强制平多：12
 *     强制平空：13
 *     从币币转入：14
 *     转出至币币：15
 *     结算未实现盈亏-多仓：16
 *     结算未实现盈亏-空仓：17
 *     穿仓分摊：19
 *     系统：26
 *     活动奖励：28
 *     返利：29
 *     转出到子账号合约账号：34
 *     从子账号合约账号转入: 35
 *     转出到母账号合约账号: 36
 *     从母账号合约账号转入：37
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum ContractTradeTypeEnum {
    CLOSE_MORE("3", "卖出平多")
    , CLOSE_EMPTY("4", "买入平空")
    , OPEN_FEE_EAT("5", "开仓手续费-吃单")
    , OPEN_FEE_PENDING("6", "开仓手续费-挂单")
    , CLOSE_FEE_EAT("7", "平仓手续费-吃单")
    , CLOSE_FEE_PENDING("8", "平仓手续费-挂单")
    , CLOSE_MORE_DELIVERY("9", "交割平多")
    , CLOSE_EMPTY_DELIVERY("10", "交割平空")
    , FEE_DELIVERY("11", "交割手续费")
    , CLOSE_MORE_FORCE("12", "强制平多 (卖出开空, 看跌爆仓)")
    , CLOSE_EMPTY_FORCE("13", "强制平空 (买入开多, 看涨爆仓)")
    , FROM_COIN("14", "从币币转入")
    , TO_COIN("15", "转出至币币")
    , SETTLEMENT_MORE("16", "结算未实现盈亏-多仓")
    , SETTLEMENT_EMPTY("17", "结算未实现盈亏-空仓")
//    , XXXXXXXXXXXX("19", "穿仓分摊")
//    , XXXXXXXXXXXX("26", "系统")
//    , XXXXXXXXXXXX("28", "活动奖励")
//    , XXXXXXXXXXXX("29", "返利")
//    , XXXXXXXXXXXX("34", "转出到子账号合约账号")
//    , XXXXXXXXXXXX("35", "从子账号合约账号转入")
//    , XXXXXXXXXXXX("36", "转出到母账号合约账号")
//    , XXXXXXXXXXXX("37", "从母账号合约账号转入")
    ;

    private final String value;
    private final String desc;

    ContractTradeTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ContractTradeTypeEnum get(String value) {
        for (ContractTradeTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


