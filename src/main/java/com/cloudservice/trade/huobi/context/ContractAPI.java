package com.cloudservice.trade.huobi.context;

import com.cloudservice.trade.huobi.service.HuobiHttpRequest;
import lombok.Getter;

/**
 * 交割合约API
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/3
 */
public abstract class ContractAPI {
//    TEMPLATE("XXXXXX", "/XXXXXX", "XXXXXX");

    private ContractAPI() {}

    /** 基础信息接口 */
    @Getter
    public enum Basic {
        HEARTBEAT(HuobiHttpRequest.GET, "/heartbeat/", "查询系统是否可用", false)
        , TIMESTAMP(HuobiHttpRequest.GET, "/api/v1/timestamp", "获取当前系统时间戳", false)
        , INFO(HuobiHttpRequest.GET, "/api/v1/contract_contract_info", "获取合约信息", false)
        , INDEX(HuobiHttpRequest.GET, "/api/v1/contract_index", "获取合约指数信息", false)
        , PRICE_LIMIT(HuobiHttpRequest.GET, "/api/v1/contract_price_limit", "获取合约最高限价和最低限价", false)
        , OPEN_INTEREST(HuobiHttpRequest.GET, "/api/v1/contract_open_interest", "获取当前可用合约总持仓量", false)
        , DELIVERY_PRICE(HuobiHttpRequest.GET, "/api/v1/contract_delivery_price", "获取预估交割价", false)
        , API_STATE(HuobiHttpRequest.GET, "/api/v1/contract_api_state", "查询系统状态", false)
        ;

        private final String method;
        private final String api;
        private final String desc;
        private final boolean sign;

        Basic(String method, String api, String desc, boolean sign) {
            this.method = method;
            this.api = api;
            this.desc = desc;
            this.sign = sign;
        }
    }

    /** 市场行情接口 */
    @Getter
    public enum Market {
        DEPTH(HuobiHttpRequest.GET, "/market/depth", "获取行情深度数据", false)
        , BASIS(HuobiHttpRequest.GET, "/api/v1/index/market/history/basis", "获取基差数据", false)
        , KLINE(HuobiHttpRequest.GET, "/market/history/kline", "获取K线数据", false)
        , INDEX(HuobiHttpRequest.GET, "/api/v1/index/market/history/index", "获取指数K线数据", false)
        , MERGED(HuobiHttpRequest.GET, "/market/detail/merged", "获取聚合行情", false)
        , TRADE(HuobiHttpRequest.GET, "/market/trade", "获取市场最近成交记录", false)
        , HISTORY_TRADE(HuobiHttpRequest.GET, "/market/history/trade", "批量获取最近的交易记录", false)
        , RISK_INFO(HuobiHttpRequest.GET, "/api/v1/contract_risk_info", "查询合约风险准备金余额和预估分摊比例", false)
        , INSURANCE_FUND(HuobiHttpRequest.GET, "/api/v1/contract_insurance_fund", "查询合约风险准备金余额历史数据", false)
        , ADJUSTFACTOR(HuobiHttpRequest.GET, "/api/v1/contract_adjustfactor", "查询平台阶梯调整系数", false)
        , HIS_OPEN_INTEREST(HuobiHttpRequest.GET, "/api/v1/contract_his_open_interest", "平台持仓量的查询", false)
        , ELITE_ACCOUNT_RATIO(HuobiHttpRequest.GET, "/api/v1/contract_elite_account_ratio", "精英账户多空持仓对比-账户数", false)
        , ELITE_POSITION_RATIO(HuobiHttpRequest.GET, "/api/v1/contract_elite_position_ratio", "精英账户多空持仓对比-持仓量", false)
        , LIQUIDATION_ORDERS(HuobiHttpRequest.GET, "/api/v1/contract_liquidation_orders", "获取强平订单", false)
        ;

        private final String method;
        private final String api;
        private final String desc;
        private final boolean sign;

        Market(String method, String api, String desc, boolean sign) {
            this.method = method;
            this.api = api;
            this.desc = desc;
            this.sign = sign;
        }
    }

    /** 账户接口 */
    @Getter
    public enum Account {
        ACCOUNT_INFO(HuobiHttpRequest.POST, "/api/v1/contract_account_info", "获取用户账户信息", false)
        , POSITION_INFO(HuobiHttpRequest.POST, "/api/v1/contract_position_info", "获取用户持仓信息", false)
        , SUB_ACCOUNT_LIST(HuobiHttpRequest.POST, "/api/v1/contract_sub_account_list", "币查询母账户下所有子账户资产信息", false)
        , SUB_ACCOUNT_INFO(HuobiHttpRequest.POST, "/api/v1/contract_sub_account_info", "查询单个子账户资产信息", false)
        , SUB_POSITION_INFO(HuobiHttpRequest.POST, "/api/v1/contract_sub_position_info", "查询单个子账户持仓信息的", false)
        , FINANCIAL_RECORD(HuobiHttpRequest.POST, "/api/v1/contract_financial_record", "查询用户财务记录", false)
        , USER_SETTLEMENT_RECORDS(HuobiHttpRequest.POST, "/api/v1/contract_user_settlement_records", "查询用户结算记录", false)
        , ORDER_LIMIT(HuobiHttpRequest.POST, "/api/v1/contract_order_limit", "查询用户当前的下单量限制", false)
        , FEE(HuobiHttpRequest.POST, "/api/v1/contract_fee", "查询用户当前的手续费费率", false)
        , TRANSFER_LIMIT(HuobiHttpRequest.POST, "/api/v1/contract_transfer_limit", "查询用户当前的划转限制", false)
        , POSITION_LIMIT(HuobiHttpRequest.POST, "/api/v1/contract_position_limit", "用户持仓量限制的查询", false)
        , ACCOUNT_POSITION_INFO(HuobiHttpRequest.POST, "/api/v1/contract_account_position_info", "查询用户账户和持仓信息", false)
        , MASTER_SUB_TRANSFER(HuobiHttpRequest.POST, "/api/v1/contract_master_sub_transfer", "母子账户划转", false)
        , MASTER_SUB_TRANSFER_RECORD(HuobiHttpRequest.POST, "/api/v1/contract_master_sub_transfer_record", "获取母账户下的所有母子账户划转记录", false)
        , TRANSFER(HuobiHttpRequest.POST, "/v1/futures/transfer", "币币账户和合约账户间进行资金的划转", false)
        ;

        private final String method;
        private final String api;
        private final String desc;
        private final boolean sign;

        Account(String method, String api, String desc, boolean sign) {
            this.method = method;
            this.api = api;
            this.desc = desc;
            this.sign = sign;
        }
    }

    /** 交易接口 */
    @Getter
    public enum Trade {
        ORDER(HuobiHttpRequest.POST, "/api/v1/contract_order", "合约下单", false)
        , BATCHORDER(HuobiHttpRequest.POST, "/api/v1/contract_batchorder", "合约批量下单", false)
        , CANCEL(HuobiHttpRequest.POST, "/api/v1/contract_cancel", "撤销订单", false)
        , CANCELALL(HuobiHttpRequest.POST, "/api/v1/contract_cancelall", "全部撤单", false)
        , ORDER_INFO(HuobiHttpRequest.POST, "/api/v1/contract_order_info", "获取合约订单信息", false)
        , ORDER_DETAIL(HuobiHttpRequest.POST, "/api/v1/contract_order_detail", "获取订单明细信息", false)
        , OPENORDERS(HuobiHttpRequest.POST, "/api/v1/contract_openorders", "获取合约当前未成交委托", false)
        , HISORDERS(HuobiHttpRequest.POST, "/api/v1/contract_hisorders", "获取合约历史委托", false)
        , MATCHRESULTS(HuobiHttpRequest.POST, "/api/v1/contract_matchresults", "获取历史成交记录", false)
        , LIGHTNING_CLOSE_POSITION(HuobiHttpRequest.POST, "/api/v1/lightning_close_position", "闪电平仓下单", false)
        , TRIGGER_ORDER(HuobiHttpRequest.POST, "/api/v1/contract_trigger_order", "合约计划委托下单", false)
        , TRIGGER_CANCEL(HuobiHttpRequest.POST, "/api/v1/contract_trigger_cancel", "合约计划委托撤单", false)
        , TRIGGER_CANCELALL(HuobiHttpRequest.POST, "/api/v1/contract_trigger_cancelall", "合约计划委托全部撤单", false)
        , TRIGGER_OPENORDERS(HuobiHttpRequest.POST, "/api/v1/contract_trigger_openorders", "获取计划委托当前委托接口", false)
        , TRIGGER_HISORDERS(HuobiHttpRequest.POST, "/api/v1/contract_trigger_hisorders", "获取计划委托历史委托接口", false)
        ;

        private final String method;
        private final String api;
        private final String desc;
        private final boolean sign;

        Trade(String method, String api, String desc, boolean sign) {
            this.method = method;
            this.api = api;
            this.desc = desc;
            this.sign = sign;
        }
    }

}


