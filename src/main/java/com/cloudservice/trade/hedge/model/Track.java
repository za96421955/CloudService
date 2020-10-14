package com.cloudservice.trade.hedge.model;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.Jsonable;
import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.huobi.enums.ContractCodeEnum;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import com.cloudservice.trade.huobi.enums.ContractTypeEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
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
    /** 交割合约类型 */
    private ContractTypeEnum contractType;

    /** 策略类型 */
    private StrategyTypeEnum strategyType;
    /** 风险类型 */
    private Integer riskType;
    /** 停止交易 */
    private boolean stopTrade = false;

    /** 策略配置 */
    private HedgeConfig hedgeConfig;
    /** 自定义配置 */
    private DIYConfig diyConfig;

    public Track() {
        this.diyConfig = new DIYConfig();
    }

    public Track(String access, String secret) {
        this();
        this.access = access;
        this.secret = secret;
    }

    public ContractCodeEnum getContractCode() {
        if (this.symbol == null) {
            return null;
        }
        return ContractCodeEnum.getUSD(this.getSymbol().getValue());
    }

    public void setHedgeConfig(HedgeConfig hedgeConfig) {
        this.hedgeConfig = hedgeConfig;
        this.setDiy();
    }

    public void setDiyConfig(DIYConfig diyConfig) {
        if (diyConfig == null) {
            return;
        }
        if (diyConfig.getStopTrade() != null) {
            this.diyConfig.setStopTrade(diyConfig.getStopTrade());
        }
        this.diyConfig.setLeverRate(diyConfig.getLeverRate());
        this.diyConfig.setBasisVolume(diyConfig.getBasisVolume());
        this.diyConfig.setIncomePricePlan(diyConfig.getIncomePricePlan());
        this.diyConfig.setProfitMultiple(diyConfig.getProfitMultiple());
        this.diyConfig.setProfitTrackIntervalTime(diyConfig.getProfitTrackIntervalTime());
        this.diyConfig.setTimeout(diyConfig.getTimeout());

        this.diyConfig.setChaseMultiple1(diyConfig.getChaseMultiple1());
        this.diyConfig.setChaseMultiple2(diyConfig.getChaseMultiple2());
        this.diyConfig.setChaseMultiple3(diyConfig.getChaseMultiple3());
        this.diyConfig.setChaseMultiple4(diyConfig.getChaseMultiple4());
        this.diyConfig.setChaseMultiple5(diyConfig.getChaseMultiple5());
        this.diyConfig.setChaseMultiple6(diyConfig.getChaseMultiple6());
        this.diyConfig.setChaseMultiple7(diyConfig.getChaseMultiple7());
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
                "access=" + access +
                ", symbol=" + symbol +
                ", hedgeType=" + hedgeType +
                ", contractType=" + contractType +
                ", strategyType=" + strategyType +
                ", stopTrade=" + stopTrade +
                ", hedgeConfig=" + hedgeConfig +
                ", diyConfig=" + diyConfig +
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

    /**
     * @description 设置自定义配置
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/14 19:33
     */
    public void setDiy() {
        if (diyConfig.getStopTrade() != null) {
            stopTrade = diyConfig.getStopTrade();
        }
        if (hedgeConfig == null) {
            return;
        }
        if (diyConfig.getLeverRate() != null) {
            hedgeConfig.setLeverRate(ContractLeverRateEnum.get(diyConfig.getLeverRate()));
        }
        if (diyConfig.getBasisVolume() != null) {
            hedgeConfig.setBasisVolume(diyConfig.getBasisVolume());
        }
        if (diyConfig.getIncomePricePlan() != null) {
            hedgeConfig.setIncomePricePlan(diyConfig.getIncomePricePlan());
        }
        if (diyConfig.getProfitMultiple() != null) {
            hedgeConfig.setProfitMultiple(diyConfig.getProfitMultiple());
        }
        if (diyConfig.getProfitTrackIntervalTime() != null) {
            hedgeConfig.setProfitTrackIntervalTime(diyConfig.getProfitTrackIntervalTime());
        }
        if (diyConfig.getTimeout() != null) {
            hedgeConfig.setTimeout(diyConfig.getTimeout());
        }

        if (diyConfig.getChaseMultiple1() != null) {
            hedgeConfig.getChaseMultipleMap().put(1, diyConfig.getChaseMultiple1());
        }
        if (diyConfig.getChaseMultiple2() != null) {
            hedgeConfig.getChaseMultipleMap().put(2, diyConfig.getChaseMultiple2());
        }
        if (diyConfig.getChaseMultiple3() != null) {
            hedgeConfig.getChaseMultipleMap().put(3, diyConfig.getChaseMultiple3());
        }
        if (diyConfig.getChaseMultiple4() != null) {
            hedgeConfig.getChaseMultipleMap().put(4, diyConfig.getChaseMultiple4());
        }
        if (diyConfig.getChaseMultiple5() != null) {
            hedgeConfig.getChaseMultipleMap().put(5, diyConfig.getChaseMultiple5());
        }
        if (diyConfig.getChaseMultiple6() != null) {
            hedgeConfig.getChaseMultipleMap().put(6, diyConfig.getChaseMultiple6());
        }
        if (diyConfig.getChaseMultiple7() != null) {
            hedgeConfig.getChaseMultipleMap().put(7, diyConfig.getChaseMultiple7());
        }

        hedgeConfig.calculateChaseInfo();
    }

    public static void main(String[] args) {
        HedgeConfig cfg = new HedgeConfig(StrategyTypeEnum.FIXED_VOLUME_2);
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


