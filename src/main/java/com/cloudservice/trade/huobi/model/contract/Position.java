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
 * 用户持仓信息
 * <p>
 * {
 * 	"status": "ok",
 * 	"data": [{
 * 		"symbol": "ETH",
 * 		"contract_code": "ETH200911",
 * 		"contract_type": "this_week",
 * 		"volume": 1.000000000000000000,
 * 		"available": 1.000000000000000000,
 * 		"frozen": 0E-18,
 * 		"cost_open": 337.702000000000011131,
 * 		"cost_hold": 337.702000000000011131,
 * 		"profit_unreal": 0.000869666759998080000000000000000000000000000000000000,
 * 		"profit_rate": 0.029368820418486977,
 * 		"lever_rate": 1,
 * 		"position_margin": 0.028742239595309266,
 * 		"direction": "buy",
 * 		"profit": 0.000869666759998080000000000000000000000000000000000000,
 * 		"last_price": 347.92
 *        }],
 * 	"ts": 1599653845649
 * }
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@Getter
@Setter
public class Position implements Serializable {
    private static final long serialVersionUID = 6947415493203314099L;

    /** 品种代码 */
    private String symbol;
    /** 合约代码 */
    private String contractCode;
    /** 合约类型 */
    private String contractType;
    /** 持仓量 */
    private BigDecimal volume;
    /** 可平仓数量 */
    private BigDecimal available;
    /** 冻结数量 */
    private BigDecimal frozen;
    /** 开仓均价 */
    private BigDecimal costOpen;
    /** 持仓均价 */
    private BigDecimal costHold;
    /** 未实现盈亏 */
    private BigDecimal profitUnreal;
    /** 收益率 */
    private BigDecimal profitRate;
    /** 杠杠倍数 */
    private Integer leverRate;
    /** 持仓保证金 */
    private BigDecimal positionMargin;
    /** "buy":买 "sell":卖 */
    private String direction;
    /** 收益 */
    private BigDecimal profit;
    /** 最新价 */
    private BigDecimal lastPrice;

    @Override
    public String toString() {
        return "Position{" +
                "symbol='" + symbol + '\'' +
                ", contractCode='" + contractCode + '\'' +
                ", contractType='" + contractType + '\'' +
                ", volume=" + volume.longValue() +
                ", costHold=" + costHold.setScale(3, BigDecimal.ROUND_DOWN) +
                ", leverRate=" + leverRate +
                ", direction='" + direction + '\'' +
                ", profit=" + profit.setScale(6, BigDecimal.ROUND_DOWN) +
                ", lastPrice=" + lastPrice +
                '}';
    }

    public static Position parse(Object data) {
        if (data == null) {
            return null;
        }
        return JSONObject.parseObject(data.toString(), Position.class);
    }

    public static List<Position> parseList(JSONArray dataArray) {
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<Position> list = new ArrayList<>();
        for (Object data : dataArray) {
            list.add(Position.parse(data));
        }
        return list;
    }

    public static void main(String[] args) {
        String result = "{\n" +
                "\t\t\"symbol\": \"ETH\",\n" +
                "\t\t\"contract_code\": \"ETH200911\",\n" +
                "\t\t\"contract_type\": \"this_week\",\n" +
                "\t\t\"volume\": 1.000000000000000000,\n" +
                "\t\t\"available\": 1.000000000000000000,\n" +
                "\t\t\"frozen\": 0E-18,\n" +
                "\t\t\"cost_open\": 337.702000000000011131,\n" +
                "\t\t\"cost_hold\": 337.702000000000011131,\n" +
                "\t\t\"profit_unreal\": 0.000869666759998080000000000000000000000000000000000000,\n" +
                "\t\t\"profit_rate\": 0.029368820418486977,\n" +
                "\t\t\"lever_rate\": 1,\n" +
                "\t\t\"position_margin\": 0.028742239595309266,\n" +
                "\t\t\"direction\": \"buy\",\n" +
                "\t\t\"profit\": 0.000869666759998080000000000000000000000000000000000000,\n" +
                "\t\t\"last_price\": 347.92\n" +
                "       }";
        System.out.println(Position.parse(JSONObject.parseObject(result)));
    }

}


