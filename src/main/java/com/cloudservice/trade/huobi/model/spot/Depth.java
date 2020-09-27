package com.cloudservice.trade.huobi.model.spot;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.ArrayUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
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
public class Depth implements Serializable {
    private static final long serialVersionUID = 1488100632919466011L;

    /** 调整为新加坡时间的时间戳，单位毫秒 */
    private Long ts;
    /** 内部字段 */
    private Long version;
    /** 当前的所有买单 [price, size] */
    private List<DepthDetail> bids;
    /** 当前的所有卖单 [price, size] */
    private List<DepthDetail> asks;

    // 项目属性
    private DepthDetail firstBuy;
    private DepthDetail secondBuy;
    /** 最大买入明细 */
    private DepthDetail maxBuy;

    private DepthDetail firstSell;
    private DepthDetail secondSell;
    /** 最大卖出明细 */
    private DepthDetail maxSell;

    /**
     * @description 计算明细比例
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/11 12:30
     **/
    public void calculateDetailRate() {
        for (int i = 0; i < bids.size(); i++) {
            DepthDetail buy = bids.get(i);
            DepthDetail sell = asks.get(i);
            if (buy == null || sell == null) {
                continue;
            }
            DepthDetail max = buy.getSize().compareTo(sell.getSize()) > 0
                    ? buy : sell;
            buy.setRate(buy.getSize().divide(max.getSize(), new MathContext(2)));
            sell.setRate(sell.getSize().divide(max.getSize(), new MathContext(2)));
        }
    }

    public DepthDetail getFirstBuy() {
        if (firstBuy == null) {
            firstBuy = bids.get(0);
        }
        return firstBuy;
    }

    public DepthDetail getSecondBuy() {
        if (secondBuy == null) {
            secondBuy = bids.get(1);
        }
        return secondBuy;
    }

    public DepthDetail getMaxBuy(BigDecimal... lowPrice) {
        if (maxBuy == null) {
            // 移除小于最低价的数据
            List<DepthDetail> buyList = bids;
            if (ArrayUtils.isNotEmpty(lowPrice) && lowPrice[0] != null) {
                buyList = new ArrayList<>();
                for (DepthDetail buy : bids) {
                    if (buy != null && buy.getPrice().compareTo(lowPrice[0]) <= 0) {
                        buyList.add(buy);
                    }
                }
            }
            maxBuy = this.getTotalMax(buyList);
        }
        return maxBuy;
    }

    public DepthDetail getFirstSell() {
        if (firstSell == null) {
            firstSell = asks.get(0);
        }
        return firstSell;
    }

    public DepthDetail getSecondSell() {
        if (secondSell == null) {
            secondSell = asks.get(1);
        }
        return secondSell;
    }

    public DepthDetail getMaxSell(BigDecimal... lowPrice) {
        if (maxSell == null) {
            // 移除大于于最低价的数据
            List<DepthDetail> sellList = asks;
            if (ArrayUtils.isNotEmpty(lowPrice) && lowPrice[0] != null) {
                sellList = new ArrayList<>();
                for (DepthDetail sell : asks) {
                    if (sell != null && sell.getPrice().compareTo(lowPrice[0]) >= 0) {
                        sellList.add(sell);
                    }
                }
            }
            maxSell = this.getTotalMax(sellList);
        }
        return maxSell;
    }

    /**
     * @description 获取总计、最大明细
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/10 18:01
     * @param detailList
     **/
    private DepthDetail getTotalMax(List<DepthDetail> detailList) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        DepthDetail max = null;
        for (DepthDetail detail : detailList) {
            totalPrice = totalPrice.add(detail.getSize());
            if (max == null || max.getSize().compareTo(detail.getSize()) < 0) {
                max = detail;
            }
        }
        if (max != null) {
            max.setTotalPrice(totalPrice);
        }
        return max;
    }

    public static Depth parse(JSONObject data) {
        if (data == null) {
            return null;
        }
        Depth depth = new Depth();
        depth.setTs(data.getLong("ts"));
        depth.setVersion(data.getLong("version"));
        depth.setBids(DepthDetail.parseList(data.getJSONArray("bids")));
        depth.setAsks(DepthDetail.parseList(data.getJSONArray("asks")));
        return depth;
    }

    public static void main(String[] args) {
        String result = " {\n" +
                "\t\t\"bids\": [\n" +
                "\t\t\t[368.0, 674.2691],\n" +
                "\t\t\t[367.0, 558.346],\n" +
                "\t\t\t[366.0, 419.65],\n" +
                "\t\t\t[365.0, 1094.7203],\n" +
                "\t\t\t[364.0, 466.4815],\n" +
                "\t\t\t[363.0, 902.6539],\n" +
                "\t\t\t[362.0, 572.8008],\n" +
                "\t\t\t[361.0, 10398.528],\n" +
                "\t\t\t[360.0, 12098.0151],\n" +
                "\t\t\t[359.0, 468.25969668142034]\n" +
                "\t\t],\n" +
                "\t\t\"asks\": [\n" +
                "\t\t\t[369.0, 1192.4349],\n" +
                "\t\t\t[370.0, 1421.5773114993906],\n" +
                "\t\t\t[371.0, 1639.2118],\n" +
                "\t\t\t[372.0, 711.7402],\n" +
                "\t\t\t[373.0, 1560.089],\n" +
                "\t\t\t[374.0, 1261.342],\n" +
                "\t\t\t[375.0, 2239.2967],\n" +
                "\t\t\t[376.0, 835.6997],\n" +
                "\t\t\t[377.0, 572.2878],\n" +
                "\t\t\t[378.0, 1129.588]\n" +
                "\t\t],\n" +
                "\t\t\"version\": 109042220235,\n" +
                "\t\t\"ts\": 1600073070785\n" +
                "\t}";
        Depth depth = Depth.parse(JSONObject.parseObject(result));
        depth.calculateDetailRate();
        System.out.println(depth.getBids());
        System.out.println(depth.getAsks());
    }

}


