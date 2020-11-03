package com.cloudservice.trade.strategy.model;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.Jsonable;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * 交易委托配置
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/17
 */
@Getter
@Setter
public class EntrustConfig implements Serializable, Jsonable<EntrustConfig> {
    private static final long serialVersionUID = 1046968233757945755L;
    /** USD - CNY 比例 */
    public static final BigDecimal USD_CNY_RATE = new BigDecimal("6.9");
    /** 100%收益差价 */
    public static final BigDecimal DIFF_PRICE_RATE100 = new BigDecimal("5.3");

    // 开仓配置
    /** 倍数 */
    private ContractLeverRateEnum leverRate;
    /** 基础张数 */
    private long basisVolume;
    /** 追仓倍率 */
    private Map<Integer, BigDecimal> chaseMultipleMap;
    /** 追仓张数 */
    private Map<Integer, Long> chaseVolumeMap;
    /** 持仓所需USD */
    private Map<Integer, BigDecimal> positionUSDMap;

    // 收益配置
    /** 计划收益价格 */
    private BigDecimal incomePricePlan;
    /** 止盈倍率 */
    private BigDecimal profitMultiple;
    /** 追仓止损倍率 */
    private Map<Integer, BigDecimal> chaseLossMultipleMap;
    /** 追仓止损差价 */
    private Map<Integer, BigDecimal> chasePositionDiffMap;

    // 时间配置
    /** 止盈追踪间隔时间, 毫秒 */
    private long profitTrackIntervalTime;
    /** 超时时间, 秒 */
    private int timeout;

    public EntrustConfig() {
        this.chaseMultipleMap = new TreeMap<>();
        this.chaseVolumeMap = new TreeMap<>();
        this.positionUSDMap = new TreeMap<>();
        this.chaseLossMultipleMap = new TreeMap<>();
        this.chasePositionDiffMap = new TreeMap<>();
    }

    @Override
    public JSONObject toJson() {
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }

    @Override
    public EntrustConfig fromJson(String json) {
        return JSONObject.parseObject(json, this.getClass());
    }

    /**
     * @description 克隆
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/14 23:04
     */
    public EntrustConfig clone() {
        return this.fromJson(this.toJson().toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(leverRate);
        sb.append("@").append(incomePricePlan).append("-").append(profitMultiple);
        sb.append("@").append(basisVolume).append("-");
        for (BigDecimal multiple : chaseMultipleMap.values()) {
            sb.append(multiple);
        }
        return sb.toString();
    }

    /**
     * @description 计算追仓信息
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/2 0:44
     */
    public void calculateChaseInfo() {
        long currVolume = basisVolume;
        for (Map.Entry<Integer, BigDecimal> multiple : chaseMultipleMap.entrySet()) {
            long chaseVolume = currVolume * (multiple.getValue().subtract(BigDecimal.ONE)).longValue();
            chaseVolumeMap.put(multiple.getKey(), chaseVolume);
            currVolume += chaseVolume;
            positionUSDMap.put(multiple.getKey(), BigDecimal.valueOf(currVolume * 10));
            chaseLossMultipleMap.put(multiple.getKey(), profitMultiple.pow(multiple.getKey() - 1));
            chasePositionDiffMap.put(multiple.getKey(), incomePricePlan.multiply(chaseLossMultipleMap.get(multiple.getKey())));
        }
    }

    /**
     * @description 获取追仓次数
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/3 19:20
     * @param   volume
     */
    public int getChaseIndex(long volume) {
        boolean isGe = false;
        int lastIndex = 1;
        for (Map.Entry<Integer, Long> entry : chaseVolumeMap.entrySet()) {
            lastIndex = entry.getKey();
            long currVolume = entry.getValue() / (chaseMultipleMap.get(entry.getKey()).intValue() - 1);
            if (volume == currVolume) {
                return entry.getKey();
            }
            if (volume > currVolume) {
                isGe = true;
            }
            if (isGe && volume < currVolume) {
                return entry.getKey();
            }
        }
        return lastIndex;
    }

    /**
     * @description USD计算CNY金额
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 17:20
     **/
    private BigDecimal calculateCNYByUSD(BigDecimal usd) {
        if (usd == null) {
            return BigDecimal.ZERO;
        }
        return usd.multiply(USD_CNY_RATE);
    }

}


