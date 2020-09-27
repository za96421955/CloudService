package com.cloudservice.trade.huobi.model.contract;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 订单信息
 * <p>
 * {
 * 	"status": "ok",
 * 	"data": {
 * 		"trades": [{
 * 			"match_id": 101783172747,
 * 			"order_id": 752953395404455937,
 * 			"symbol": "ETH",
 * 			"contract_type": "this_week",
 * 			"contract_code": "ETH200911",
 * 			"direction": "buy",
 * 			"offset": "open",
 * 			"trade_volume": 1.000000000000000000,
 * 			"trade_price": 337.702000000000000000,
 * 			"trade_turnover": 10.000000000000000000,
 * 			"trade_fee": -0.000011844762542122,
 * 			"offset_profitloss": 0E-18,
 * 			"create_date": 1599559678670,
 * 			"role": "Taker",
 * 			"order_source": "api",
 * 			"order_id_str": "752953395404455937",
 * 			"fee_asset": "ETH",
 * 			"id": "101783172747-752953395404455937-1"
 *          }],
 * 		"total_page": 1,
 * 		"current_page": 1,
 * 		"total_size": 6
 *      },
 * 	"ts": 1599653952316
 * }
 *
 * {
 *     "data": [
 *         {
 *             "symbol": "ETH",
 *             "order_id_str": "753358035837157376",
 *             "trade_volume": 1,
 *             "canceled_at": 0,
 *             "fee": -0.000011545510961019,
 *             "created_at": 1599656152459,
 *             "order_price_type": "opponent",
 *             "trade_avg_price": 346.455000000000006530000000000000000000,
 *             "contract_code": "ETH200911",
 *             "price": 346.455,
 *             "margin_frozen": 0E-18,
 *             "order_type": 1,
 *             "profit": 0,
 *             "direction": "buy",
 *             "lever_rate": 10,
 *             "client_order_id": null,
 *             "offset": "open",
 *             "trade_turnover": 10.000000000000000000,
 *             "fee_asset": "ETH",
 *             "liquidation_type": "0",
 *             "volume": 1,
 *             "contract_type": "this_week",
 *             "order_source": "api",
 *             "order_id": 753358035837157376,
 *             "status": 6
 *         }
 *     ],
 *     "status": "ok",
 *     "ts": 1599725050105
 * }
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@Getter
@Setter
@ToString
public class Order implements Serializable {
    private static final long serialVersionUID = -5992839864446463545L;

    /** 全局唯一的交易标识 */
    private String id;
    /**
     * 撮合结果id, 与订单ws推送orders.$symbol以及撮合订单ws推送matchOrders.$symbol推送结果中的trade_id是相同的，非唯一，可重复，
     * 注意：一个撮合结果代表一个taker单和N个maker单的成交记录的集合，如果一个taker单吃了N个maker单，那这N笔trade都是一样的撮合结果id
     */
    private Long matchId;
    /** 订单ID */
    private Long orderId;
    /** 品种代码 */
    private String symbol;
    /** 合约类型 */
    private String contractType;
    /** 合约代码 */
    private String contractCode;
    /** "buy":买 "sell":卖 */
    private String direction;
    /** "open":开 "close":平 */
    private String offset;
    /** 累计成交数量 */
    private BigDecimal tradeVolume;
    /** 成交价格 */
    private BigDecimal tradePrice;
    /** 本笔成交金额 */
    private BigDecimal tradeTurnover;
    /** 成交手续费 */
    private BigDecimal tradeFee;
    /** 平仓盈亏 */
    private BigDecimal offsetProfitloss;
    /** 成交时间 */
    private Long createDate;
    /** taker或maker */
    private String role;
    /** 订单来源 */
    private String orderSource;
    /** String类型订单ID */
    private String orderIdStr;
    /** 手续费币种 */
    private String feeAsset;

    // ↓↓↓ 指定订单查询属性 ↓↓↓

    /** 撤单时间 */
    private Long canceledAt;
    /** 手续费 */
    private BigDecimal fee;
    /** 创建时间 */
    private Long createdAt;
    /** 订单报价类型 */
    private String orderPriceType;
    /** 成交均价 */
    private BigDecimal tradeAvgPrice;
    /** 委托价格 */
    private BigDecimal price;
    /** 冻结保证金 */
    private BigDecimal marginFrozen;
    /** 订单类型 */
    private Integer orderType;
    /** 收益 */
    private Integer profit;
    /** 杠杆倍数 */
    private Integer leverRate;
    /** 客户订单ID */
    private String clientOrderId;
    /** 强平类型 0:非强平类型，1：多空轧差， 2:部分接管，3：全部接管 */
    private String liquidationType;
    /** 委托数量 */
    private Integer volume;
    /** 订单状态 (1准备提交 2准备提交 3已提交 4部分成交 5部分成交已撤单 6全部成交 7已撤单 11撤单中) */
    private Integer status;

    // ↑↑↑ 指定订单查询属性 ↑↑↑

    public static Order parse(Object data) {
        if (data == null) {
            return null;
        }
        return JSONObject.parseObject(data.toString(), Order.class);
    }

    public static List<Order> parseList(JSONArray dataArray) {
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<Order> list = new ArrayList<>();
        for (Object data : dataArray) {
            list.add(Order.parse(data));
        }
        return list;
    }

    public static void main(String[] args) {
        String result = "{\n" +
                "\"symbol\": \"ETH\",\n" +
                "\"order_id_str\": \"753358035837157376\",\n" +
                "\"trade_volume\": 1,\n" +
                "\"canceled_at\": 0,\n" +
                "\"fee\": -0.000011545510961019,\n" +
                "\"created_at\": 1599656152459,\n" +
                "\"order_price_type\": \"opponent\",\n" +
                "\"trade_avg_price\": 346.455000000000006530000000000000000000,\n" +
                "\"contract_code\": \"ETH200911\",\n" +
                "\"price\": 346.455,\n" +
                "\"margin_frozen\": 0E-18,\n" +
                "\"order_type\": 1,\n" +
                "\"profit\": 0,\n" +
                "\"direction\": \"buy\",\n" +
                "\"lever_rate\": 10,\n" +
                "\"client_order_id\": null,\n" +
                "\"offset\": \"open\",\n" +
                "\"trade_turnover\": 10.000000000000000000,\n" +
                "\"fee_asset\": \"ETH\",\n" +
                "\"liquidation_type\": \"0\",\n" +
                "\"volume\": 1,\n" +
                "\"contract_type\": \"this_week\",\n" +
                "\"order_source\": \"api\",\n" +
                "\"order_id\": 753358035837157376,\n" +
                "\"status\": 6\n" +
                "}";
        System.out.println(Order.parse(JSONObject.parseObject(result)));
    }

}


