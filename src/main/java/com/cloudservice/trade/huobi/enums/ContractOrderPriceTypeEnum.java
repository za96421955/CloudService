package com.cloudservice.trade.huobi.enums;

/**
 * 合约开平仓
 * <p>
 *     "limit":限价
 *     "opponent":对手价
 *     "post_only":只做maker单,post only下单只受用户持仓数量限制, Post only(也叫maker only订单，只下maker单)每个周期合约的开仓/平仓的下单数量限制为500000，同时也会受到用户持仓数量限制。
 *     optimal_5：最优5档
 *     optimal_10：最优10档
 *     optimal_20：最优20档
 *     ioc:IOC订单
 *     fok：FOK订单
 *     "opponent_ioc"： 对手价-IOC下单
 *     "optimal_5_ioc"：最优5档-IOC下单
 *     "optimal_10_ioc"：最优10档-IOC下单
 *     "optimal_20_ioc"：最优20档-IOC下单
 *     "opponent_fok"： 对手价-FOK下单
 *     "optimal_5_fok"：最优5档-FOK下单
 *     "optimal_10_fok"：最优10档-FOK下单
 *     "optimal_20_fok"：最优20档-FOK下单
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum ContractOrderPriceTypeEnum {
    LIMIT("limit", "限价")
    , OPPONENT("opponent", "对手价 (下单price价格参数不用传, 对手价下单价格是买一和卖一价)")
    , OPTIMAL_5("optimal_5", "最优5档 (下单price价格参数不用传)")
    , optimal_10("optimal_10", "最优10档 (下单price价格参数不用传)")
    , optimal_20("optimal_20", "最优20档 (下单price价格参数不用传)")
    , IOC("ioc", "IOC订单 (立即成交并取消剩余)")
    , FOK("fok", "FOK订单 (全部成交或立即取消)")
    ;

    private final String value;
    private final String desc;

    ContractOrderPriceTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ContractOrderPriceTypeEnum get(String value) {
        for (ContractOrderPriceTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


