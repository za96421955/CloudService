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
 * 计划委托信息
 * <p>
 * {
 * 	"status": "ok",
 * 	"data": {
 * 		"orders": [{
 * 			"symbol": "ETH",
 * 			"contract_code": "ETH200911",
 * 			"contract_type": "this_week",
 * 			"trigger_type": "ge",
 * 			"volume": 1.000000000000000000,
 * 			"order_type": 1,
 * 			"direction": "sell",
 * 			"offset": "close",
 * 			"lever_rate": 10,
 * 			"order_id": 45,
 * 			"order_id_str": "45",
 * 			"order_source": "api",
 * 			"trigger_price": 346.800000000000000000,
 * 			"order_price": 346.800000000000000000,
 * 			"created_at": 1599655228145,
 * 			"order_price_type": "limit",
 * 			"status": 2
 *          }],
 * 		"total_page": 1,
 * 		"current_page": 1,
 * 		"total_size": 1
 *      },
 * 	"ts": 1599655531029
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
public class TriggerOrder implements Serializable {
    private static final long serialVersionUID = 3783844149946666877L;

    /** 合约品种 */
    private String symbol;
    /** 合约代码 */
    private String contractCode;
    /** 合约类型 */
    private String contractType;
    /** 触发类型： ge大于等于；le小于等于 */
    private String triggerType;
    /** 委托数量 */
    private BigDecimal volume;
    /** 订单类型：1、报单 2、撤单 */
    private Integer orderType;
    /** 订单方向 [买(buy),卖(sell)] */
    private String direction;
    /** 开平标志 [开(open),平(close)] */
    private String offset;
    /** 杠杆倍数 1\5\10\20 */
    private Integer leverRate;
    /** 计划委托单订单ID */
    private Long orderId;
    /** 字符串类型的订单ID */
    private String orderIdStr;
    /** 来源 */
    private String orderSource;
    /** 触发价 */
    private BigDecimal triggerPrice;
    /** 委托价 */
    private BigDecimal orderPrice;
    /** 订单创建时间 */
    private Long createdAt;
    /** 订单报价类型 "limit":限价，"optimal_5":最优5档，"optimal_10":最优10档，"optimal_20":最优20档 */
    private String orderPriceType;
    /** 订单状态：1:准备提交、2:已提交、3:报单中、8：撤单未找到、9：撤单中、10：失败' */
    private Integer status;

    public static TriggerOrder parse(Object data) {
        if (data == null) {
            return null;
        }
        return JSONObject.parseObject(data.toString(), TriggerOrder.class);
    }

    public static List<TriggerOrder> parseList(JSONArray dataArray) {
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<TriggerOrder> list = new ArrayList<>();
        for (Object data : dataArray) {
            list.add(TriggerOrder.parse(data));
        }
        return list;
    }

    public static void main(String[] args) {
        String result = "{\n" +
                "\t\t\t\"symbol\": \"ETH\",\n" +
                "\t\t\t\"contract_code\": \"ETH200911\",\n" +
                "\t\t\t\"contract_type\": \"this_week\",\n" +
                "\t\t\t\"trigger_type\": \"ge\",\n" +
                "\t\t\t\"volume\": 1.000000000000000000,\n" +
                "\t\t\t\"order_type\": 1,\n" +
                "\t\t\t\"direction\": \"sell\",\n" +
                "\t\t\t\"offset\": \"close\",\n" +
                "\t\t\t\"lever_rate\": 10,\n" +
                "\t\t\t\"order_id\": 45,\n" +
                "\t\t\t\"order_id_str\": \"45\",\n" +
                "\t\t\t\"order_source\": \"api\",\n" +
                "\t\t\t\"trigger_price\": 346.800000000000000000,\n" +
                "\t\t\t\"order_price\": 346.800000000000000000,\n" +
                "\t\t\t\"created_at\": 1599655228145,\n" +
                "\t\t\t\"order_price_type\": \"limit\",\n" +
                "\t\t\t\"status\": 2\n" +
                "         }";
        System.out.println(TriggerOrder.parse(JSONObject.parseObject(result)));
    }

}


