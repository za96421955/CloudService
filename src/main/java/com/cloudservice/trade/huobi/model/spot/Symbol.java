package com.cloudservice.trade.huobi.model.spot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 交易对
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@Getter
@Setter
@ToString
public class Symbol implements Serializable {
    private static final long serialVersionUID = -7283330509641860455L;

    /** 交易对中的基础币种 */
    private String baseCurrency;
    /** 交易对中的报价币种 */
    private String quoteCurrency;
    /** 交易对报价的精度（小数点后位数） */
    private Integer pricePrecision;
    /** 交易对基础币种计数精度（小数点后位数） */
    private Integer amountPrecision;
    /** 交易区，可能值: [main，innovation] */
    private String symbolPartition;
    /** 交易对 */
    private String symbol;
    /** 交易对状态；可能值: [online，offline,suspend] online - 已上线；offline - 交易对已下线，不可交易；suspend -- 交易暂停；pre-online - 即将上线 */
    private String state;
    /** 交易对交易金额的精度（小数点后位数） */
    private Integer valuePrecision;
    /** 交易对限价单最小下单量 ，以基础币种为单位（即将废弃） */
    private Float minOrderAmt;
    /** 交易对限价单最大下单量 ，以基础币种为单位（即将废弃） */
    private Float maxOrderAmt;
    /** 交易对限价单最小下单量 ，以基础币种为单位（NEW） */
    private Float limitOrderMinOrderAmt;
    /** 交易对限价单最大下单量 ，以基础币种为单位（NEW） */
    private Float limitOrderMaxOrderAmt;
    /** 交易对市价卖单最小下单量，以基础币种为单位（NEW） */
    private Float sellMarketMinOrderAmt;
    /** 交易对市价卖单最大下单量，以基础币种为单位（NEW） */
    private Float sellMarketMaxOrderAmt;
    /** 交易对市价买单最大下单金额，以计价币种为单位（NEW） */
    private Float buyMarketMaxOrderValue;
    /** 交易对限价单和市价买单最小下单金额 ，以计价币种为单位 */
    private Float minOrderValue;
    /** 交易对限价单和市价买单最大下单金额 ，以折算后的USDT为单位（NEW） */
    private Float maxOrderValue;
    /** 交易对杠杆最大倍数(仅对逐仓杠杆交易对、全仓杠杆交易对、杠杆ETP交易对有效） */
    private Float leverageRatio;
    /** 标的交易代码 (仅对杠杆ETP交易对有效) */
    private String underlying;
    /** 持仓管理费费率 (仅对杠杆ETP交易对有效) */
    private Float mgmtFeeRate;
    /** 持仓管理费收取时间 (24小时制，GMT+8，格式：HH:MM:SS，仅对杠杆ETP交易对有效) */
    private String chargeTime;
    /** 每日调仓时间 (24小时制，GMT+8，格式：HH:MM:SS，仅对杠杆ETP交易对有效) */
    private String rebalTime;
    /** 临时调仓阈值 (以实际杠杆率计，仅对杠杆ETP交易对有效) */
    private Float rebalThreshold;
    /** 初始净值（仅对杠杆ETP交易对有效） */
    private Float initNav;
    /** API交易使能标记（有效值：enabled, disabled） */
    private String apiTrading;

    public static Symbol parse(Object data) {
        if (data == null) {
            return null;
        }
        return JSONObject.parseObject(data.toString(), Symbol.class);
    }

    public static List<Symbol> parseList(JSONArray dataArray) {
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<Symbol> list = new ArrayList<>();
        for (Object data : dataArray) {
            list.add(Symbol.parse(data));
        }
        return list;
    }

}


