package com.cloudservice.trade.hedge.model;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.Jsonable;
import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.huobi.enums.ContractCodeEnum;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import com.cloudservice.trade.huobi.model.contract.Position;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单追踪
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/17
 */
@Getter
@Setter
public class Track implements Serializable, Jsonable<Track> {
    private static final long serialVersionUID = 1835677160307123550L;

    private String access;
    private String secret;
    private SymbolEnum symbol;
    /** 对冲合约类型 */
    private String hedgeType;
    /** 停止交易 */
    private boolean stopTrade = false;
    /** 策略配置 */
    private HedgeConfig hedgeConfig;

    public Track() {}

    public Track(String access, String secret) {
        this.access = access;
        this.secret = secret;
    }

    public ContractCodeEnum getContractCode() {
        if (this.symbol == null) {
            return null;
        }
        return ContractCodeEnum.getUSD(this.getSymbol().getValue());
    }

    /**
     * @description 获取止盈倍率
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 20:40
     * @param volume
     **/
    public BigDecimal getProfitMultiple(long volume) {
        return hedgeConfig.getChaseProfitMultipleMap().get(hedgeConfig.getChaseIndex(volume));
    }

    /**
     * @description 获取追仓倍率
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 20:14
     * @param volume
     **/
    public BigDecimal getChaseMultiple(long volume) {
        return hedgeConfig.getChaseMultipleMap().get(this.getHedgeConfig().getChaseIndex(volume));
    }

    public boolean equals(Track track) {
        if (track == null) {
            return false;
        }
        return this.getAccess().equals(track.getAccess())
                && this.getSymbol().equals(track.getSymbol())
                && this.getHedgeType().equals(track.getHedgeType());
    }

    @Override
    public String toString() {
        return "Track{" +
                "access='" + access + '\'' +
                ", symbol=" + symbol +
                ", hedgeType=" + hedgeType +
                ", stopTrade=" + stopTrade +
                ", hedgeConfig=" + hedgeConfig +
                '}';
    }

    @Override
    public JSONObject toJson() {
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }

    @Override
    public Track fromJson(String json) {
        return JSONObject.parseObject(json, this.getClass());
    }

    public static void main(String[] args) {
        HedgeConfig cfg = new HedgeConfig(StrategyTypeEnum.FIXED_BASIS);
        cfg.setBasisVolume(2);
        cfg.getChaseMultipleMap().put(1, BigDecimal.valueOf(4));
        cfg.getChaseMultipleMap().put(2, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(3, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(4, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(5, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(6, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(7, BigDecimal.valueOf(2));
        cfg.setIncomePricePlan(new BigDecimal("0.6"));
        cfg.setProfitMultiple(BigDecimal.valueOf(2));
        cfg.calculateChaseInfo();

        long positionVolume = 36;
        Track track = new Track();
        track.setHedgeConfig(cfg);
        System.out.println(track.getProfitMultiple(positionVolume));
        System.out.println(track.getChaseMultiple(positionVolume));
    }

}


