package com.cloudservice.trade.strategy.model;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.Jsonable;
import com.cloudservice.trade.huobi.enums.ContractCodeEnum;
import com.cloudservice.trade.huobi.enums.ContractTypeEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易委托
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/17
 */
@Getter
@Setter
public class Entrust implements Serializable, Jsonable<Entrust> {
    private static final long serialVersionUID = 5338653082566178593L;

    private String access;
    private String secret;
    private SymbolEnum symbol;
    /** 交割合约类型 */
    private ContractTypeEnum contractType;

    /** 停止交易 */
    private boolean stopTrade = false;
    /** 策略配置 */
    private EntrustConfig entrustConfig;

    public Entrust() {}

    public Entrust(String access, String secret) {
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

    public void setEntrustConfig(EntrustConfig entrustConfig) {
        this.entrustConfig = entrustConfig.clone();
    }

    /**
     * @description 获取止损差价
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 20:40
     * @param currVolume
     **/
    public BigDecimal getLossDiffPrice(long currVolume) {
        return entrustConfig.getChasePositionDiffMap().get(entrustConfig.getChaseIndex(currVolume));
    }

    /**
     * @description 获取止损追仓张数
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 20:14
     * @param currVolume
     **/
    public long getLossChaseVolume(long currVolume) {
        return entrustConfig.getChaseVolumeMap().get(entrustConfig.getChaseIndex(currVolume));
    }

    @Override
    public String toString() {
        return "Track{" +
                "access=" + access +
                ", symbol=" + symbol +
                ", contractType=" + contractType +
                ", stopTrade=" + stopTrade +
                ", hedgeConfig=" + entrustConfig +
                '}';
    }

    @Override
    public JSONObject toJson() {
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }

    @Override
    public Entrust fromJson(String json) {
        return JSONObject.parseObject(json, this.getClass());
    }

    public static void main(String[] args) {
        EntrustConfig cfg = new EntrustConfig();
        cfg.setBasisVolume(2);
        cfg.getChaseMultipleMap().put(1, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(2, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(3, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(4, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(5, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(6, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(7, BigDecimal.valueOf(2));
        cfg.setIncomePricePlan(new BigDecimal("0.6"));
        cfg.setProfitMultiple(BigDecimal.valueOf(2));
        cfg.calculateChaseInfo();

        long currVolume = 96;
        Entrust entrust = new Entrust();
        entrust.setEntrustConfig(cfg);
        System.out.println(entrust.getLossDiffPrice(currVolume));
        System.out.println(entrust.getLossChaseVolume(currVolume));
    }

}


