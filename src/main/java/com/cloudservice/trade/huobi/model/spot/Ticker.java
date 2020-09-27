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
 * 聚合行情
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@Getter
@Setter
@ToString
public class Ticker implements Serializable {
    private static final long serialVersionUID = -5133685566402004655L;

    /** NA */
    private Long id;
    /** 调整为新加坡时间的时间戳，单位毫秒 */
    private Long ts;
    /** 以基础币种计量的交易量（以滚动24小时计） */
    private Float amount;
    /** 交易次数（以滚动24小时计） */
    private Integer count;
    /** 本阶段开盘价（以滚动24小时计） */
    private Float open;
    /** 本阶段最新价（以滚动24小时计） */
    private Float close;
    /** 本阶段最低价（以滚动24小时计） */
    private Float low;
    /** 本阶段最高价（以滚动24小时计） */
    private Float high;
    /** 以报价币种计量的交易量（以滚动24小时计） */
    private Float vol;
    /** 当前的最高买价 [price, size] */
    private Float[] bid;
    /** 当前的最低卖价 [price, size] */
    private Float[] ask;

    public static Ticker parse(Object data) {
        if (data == null) {
            return null;
        }
        return JSONObject.parseObject(data.toString(), Ticker.class);
    }

    public static List<Ticker> parseList(JSONArray dataArray) {
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<Ticker> list = new ArrayList<>();
        for (Object data : dataArray) {
            list.add(Ticker.parse(data));
        }
        return list;
    }

    public static void main(String[] args) {
        String result = "{\n" +
                "  \"id\":1499225271,\n" +
                "  \"ts\":1499225271000,\n" +
                "  \"close\":1885.0000,\n" +
                "  \"open\":1960.0000,\n" +
                "  \"high\":1985.0000,\n" +
                "  \"low\":1856.0000,\n" +
                "  \"amount\":81486.2926,\n" +
                "  \"count\":42122,\n" +
                "  \"vol\":157052744.85708200,\n" +
                "  \"ask\":[1885.0000,21.8804],\n" +
                "  \"bid\":[1884.0000,1.6702]\n" +
                "}";
        System.out.println(Ticker.parse(JSONObject.parseObject(result)));
    }

}


