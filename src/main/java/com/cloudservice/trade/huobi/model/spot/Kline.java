package com.cloudservice.trade.huobi.model.spot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * K线数据
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@Getter
@Setter
public class Kline implements Serializable, Comparable {
    private static final long serialVersionUID = -5133685566402004655L;

    /** 调整为新加坡时间的时间戳，单位秒，并以此作为此K线柱的id */
    private Long id;
    /** 以基础币种计量的交易量 */
    private Float amount;
    /** 交易次数 */
    private Integer count;
    /** 本阶段开盘价 */
    private BigDecimal open;
    /** 本阶段收盘价 */
    private BigDecimal close;
    /** 本阶段最低价 */
    private BigDecimal low;
    /** 本阶段最高价 */
    private BigDecimal high;
    /** 以报价币种计量的交易量 */
    private Float vol;

    /**
     * @description 是否小差价区间
     * <p>
     *     过小的差价区间，不具备参考价值
     * </p>
     *
     * @author 陈晨
     * @date 2020/9/21 13:47
     **/
    public boolean isSmallDiffRange() {
        return high.subtract(low).compareTo(BigDecimal.valueOf(1.2)) <= 0;
    }

    /**
     * @description 获取拉盘比例
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/21 15:58
     **/
    public BigDecimal getPullRate() {
        BigDecimal max = open;
        BigDecimal min = close;
        if (close.compareTo(max) > 0) {
            max = close;
            min = open;
        }
        return max.subtract(min).divide(min, new MathContext(8));
    }

    /**
     * @description 获取买卖
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/21 16:20
     **/
    public ContractDirectionEnum getDirection() {
        if (open.compareTo(close) > 0) {
            return ContractDirectionEnum.SELL;
        }
        return ContractDirectionEnum.BUY;
    }

    @Override
    public int compareTo(Object kline) {
        return this.getPullRate().compareTo(((Kline) kline).getPullRate());
    }

    @Override
    public String toString() {
        return "Kline{" +
                "amount=" + amount +
                ", open=" + open +
                ", close=" + close +
                ", low=" + low +
                ", high=" + high +
                ", direction=" + this.getDirection() +
                ", pullRate=" + this.getPullRate() +
                '}';
    }

    public static Kline parse(Object data) {
        if (data == null) {
            return null;
        }
        return JSONObject.parseObject(data.toString(), Kline.class);
    }

    public static List<Kline> parseList(JSONArray dataArray) {
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<Kline> list = new ArrayList<>();
        for (Object data : dataArray) {
            list.add(Kline.parse(data));
        }
        return list;
    }

    public static void main(String[] args) {
        String result = "[\n" +
                "  {\n" +
                "    \"id\": 1499184000,\n" +
                "    \"amount\": 37593.0266,\n" +
                "    \"count\": 0,\n" +
                "    \"open\": 1935.2000,\n" +
                "    \"close\": 1879.0000,\n" +
                "    \"low\": 1856.0000,\n" +
                "    \"high\": 1940.0000,\n" +
                "    \"vol\": 71031537.97866500\n" +
                "  }\n" +
                "]";
        System.out.println(Kline.parseList(JSONArray.parseArray(result)));
    }

}


