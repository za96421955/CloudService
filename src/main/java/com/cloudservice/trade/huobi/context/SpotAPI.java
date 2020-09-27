package com.cloudservice.trade.huobi.context;

import com.cloudservice.trade.huobi.service.HuobiHttpRequest;
import lombok.Getter;

/**
 * 现货API
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/3
 */
public abstract class SpotAPI {
//    TEMPLATE("XXXXXX", "/XXXXXX", "XXXXXX", false)

    private SpotAPI() {}

    /** 基础类	/v1/common/*	基础类接口，包括币种、币种对、时间戳等接口 */
    @Getter
    public enum Basic {
        SYMBOLS(HuobiHttpRequest.GET, "/v1/common/symbols", "获取所有交易对", false)
        , CURRENCYS(HuobiHttpRequest.GET, "/v1/common/currencys", "获取所有币种", false)
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

    /** 行情类	/market/*	公共行情类接口，包括成交、深度、行情等 */
    @Getter
    public enum Market {
        KLINE(HuobiHttpRequest.GET, "/market/history/kline", "K 线数据（蜡烛图）", false)
        , MERGED(HuobiHttpRequest.GET, "/market/detail/merged", "聚合行情（Ticker）", false)
        , DEPTH(HuobiHttpRequest.GET, "/market/depth", "市场深度数据", false)
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

    /** 账户类	/v1/account/* /v1/subuser/*	账户类接口，包括账户信息，子用户等 */
    @Getter
    public enum Account {
        TEMPLATE(HuobiHttpRequest.GET, "/XXXXXX", "XXXXXX", false)
        , TEMPLATE1(HuobiHttpRequest.GET, "/XXXXXX", "XXXXXX", false)
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

    /** 订单类	/v1/order/*	订单类接口，包括下单、撤单、订单查询、成交查询等 */
    @Getter
    public enum Order {
        TEMPLATE(HuobiHttpRequest.GET, "/XXXXXX", "XXXXXX", false)
        , TEMPLATE1(HuobiHttpRequest.GET, "/XXXXXX", "XXXXXX", false)
        ;

        private final String method;
        private final String api;
        private final String desc;
        private final boolean sign;

        Order(String method, String api, String desc, boolean sign) {
            this.method = method;
            this.api = api;
            this.desc = desc;
            this.sign = sign;
        }
    }

}


