package com.cloudservice.trade.hedge.model;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.Jsonable;
import com.cloudservice.trade.huobi.enums.ContractCodeEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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

}


