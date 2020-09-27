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
 * 账户信息
 * <p>
 * {
 * 	"status": "ok",
 * 	"data": [{
 * 		"symbol": "ETH",
 * 		"margin_balance": 0.236127973624564735000000000000000000000000000000000000,
 * 		"margin_position": 0.028688312955068364,
 * 		"margin_frozen": 0E-18,
 * 		"margin_available": 0.207439660669496371000000000000000000000000000000000000,
 * 		"profit_real": -0.030664017819144891,
 * 		"profit_unreal": 0.000923593400238980000000000000000000000000000000000000,
 * 		"risk_rate": 8.220807227820902865,
 * 		"withdraw_available": 0.206516067269257391,
 * 		"liquidation_price": 38.139648170630252282,
 * 		"lever_rate": 1,
 * 		"adjust_factor": 0.010000000000000000,
 * 		"margin_static": 0.235204380224325755,
 * 		"is_debit": 0
 *        }],
 * 	"ts": 1599653637618
 * }
 *
 * {
 * 	"status": "ok",
 * 	"data": [{
 * 		"symbol": "ETH",
 * 		"margin_balance": 0.236094190096366595000000000000000000000000000000000000,
 * 		"margin_position": 0.028722096483266506,
 * 		"margin_frozen": 0E-18,
 * 		"margin_available": 0.207372093613100089000000000000000000000000000000000000,
 * 		"profit_real": -0.030664017819144891,
 * 		"profit_unreal": 0.000889809872040840000000000000000000000000000000000000,
 * 		"risk_rate": 8.209949760071138086,
 * 		"withdraw_available": 0.206482283741059249,
 * 		"liquidation_price": 38.139648170630252282,
 * 		"lever_rate": 1,
 * 		"adjust_factor": 0.010000000000000000,
 * 		"margin_static": 0.235204380224325755,
 * 		"positions": [{
 * 			"symbol": "ETH",
 * 			"contract_code": "ETH200911",
 * 			"contract_type": "this_week",
 * 			"volume": 1.000000000000000000,
 * 			"available": 1.000000000000000000,
 * 			"frozen": 0E-18,
 * 			"cost_open": 337.702000000000011131,
 * 			"cost_hold": 337.702000000000011131,
 * 			"profit_unreal": 0.000889809872040840000000000000000000000000000000000000,
 * 			"profit_rate": 0.030049057340793388,
 * 			"lever_rate": 1,
 * 			"position_margin": 0.028722096483266506,
 * 			"direction": "buy",
 * 			"profit": 0.000889809872040840000000000000000000000000000000000000,
 * 			"last_price": 348.164
 *                }]
 *      }],
 * 	"ts": 1599653727837
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
public class Account implements Serializable {
    private static final long serialVersionUID = -8563676066670138058L;

    /** 品种代码 */
    private String symbol;
    /** 账户权益 */
    private BigDecimal marginBalance;
    /** 持仓保证金（当前持有仓位所占用的保证金） */
    private BigDecimal marginPosition;
    /** 冻结保证金 */
    private BigDecimal marginFrozen;
    /** 可用保证金 */
    private BigDecimal marginAvailable;
    /** 已实现盈亏 */
    private BigDecimal profitReal;
    /** 未实现盈亏 */
    private BigDecimal profitUnreal;
    /** 保证金率 */
    private BigDecimal riskRate;
    /** 可划转数量 */
    private BigDecimal withdrawAvailable;
    /** 预估强平价 */
    private BigDecimal liquidationPrice;
    /** 杠杠倍数 */
    private Integer leverRate;
    /** 调整系数 */
    private BigDecimal adjustFactor;
    /** 静态权益 */
    private BigDecimal marginStatic;
    /** XXXXXXXXXX */
    private Integer isDebit;

    /** 用户持仓信息 */
    private List<Position> positions;

    public static Account parse(Object data) {
        if (data == null) {
            return null;
        }
        return JSONObject.parseObject(data.toString(), Account.class);
    }

    public static List<Account> parseList(JSONArray dataArray) {
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<Account> list = new ArrayList<>();
        for (Object data : dataArray) {
            list.add(Account.parse(data));
        }
        return list;
    }

    public static void main(String[] args) {
        String result = "{\n" +
                "\t\t\"symbol\": \"ETH\",\n" +
                "\t\t\"margin_balance\": 0.236094190096366595000000000000000000000000000000000000,\n" +
                "\t\t\"margin_position\": 0.028722096483266506,\n" +
                "\t\t\"margin_frozen\": 0E-18,\n" +
                "\t\t\"margin_available\": 0.207372093613100089000000000000000000000000000000000000,\n" +
                "\t\t\"profit_real\": -0.030664017819144891,\n" +
                "\t\t\"profit_unreal\": 0.000889809872040840000000000000000000000000000000000000,\n" +
                "\t\t\"risk_rate\": 8.209949760071138086,\n" +
                "\t\t\"withdraw_available\": 0.206482283741059249,\n" +
                "\t\t\"liquidation_price\": 38.139648170630252282,\n" +
                "\t\t\"lever_rate\": 1,\n" +
                "\t\t\"adjust_factor\": 0.010000000000000000,\n" +
                "\t\t\"margin_static\": 0.235204380224325755,\n" +
                "\t\t\"positions\": [{\n" +
                "\t\t\t\"symbol\": \"ETH\",\n" +
                "\t\t\t\"contract_code\": \"ETH200911\",\n" +
                "\t\t\t\"contract_type\": \"this_week\",\n" +
                "\t\t\t\"volume\": 1.000000000000000000,\n" +
                "\t\t\t\"available\": 1.000000000000000000,\n" +
                "\t\t\t\"frozen\": 0E-18,\n" +
                "\t\t\t\"cost_open\": 337.702000000000011131,\n" +
                "\t\t\t\"cost_hold\": 337.702000000000011131,\n" +
                "\t\t\t\"profit_unreal\": 0.000889809872040840000000000000000000000000000000000000,\n" +
                "\t\t\t\"profit_rate\": 0.030049057340793388,\n" +
                "\t\t\t\"lever_rate\": 1,\n" +
                "\t\t\t\"position_margin\": 0.028722096483266506,\n" +
                "\t\t\t\"direction\": \"buy\",\n" +
                "\t\t\t\"profit\": 0.000889809872040840000000000000000000000000000000000000,\n" +
                "\t\t\t\"last_price\": 348.164\n" +
                "               }]\n" +
                "     }";
        System.out.println(Account.parse(JSONObject.parseObject(result)));
    }

}


