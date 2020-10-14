package com.cloudservice.trade.hedge.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义配置
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/17
 */
@Getter
@Setter
@ToString
public class DIYConfig implements Serializable {
    private static final long serialVersionUID = 8189219694455517091L;

    /** 停止交易 */
    private Boolean stopTrade;

    // 开仓配置
    /** 倍数 */
    private String leverRate;
    /** 基础张数 */
    private Long basisVolume;
    /** 追仓倍率 */
    private BigDecimal chaseMultiple1;
    private BigDecimal chaseMultiple2;
    private BigDecimal chaseMultiple3;
    private BigDecimal chaseMultiple4;
    private BigDecimal chaseMultiple5;
    private BigDecimal chaseMultiple6;
    private BigDecimal chaseMultiple7;

    // 收益配置
    /** 计划收益价格 */
    private BigDecimal incomePricePlan;
    /** 止盈倍率 */
    private BigDecimal profitMultiple;

    // 时间配置
    /** 止盈追踪间隔时间, 毫秒 */
    private Long profitTrackIntervalTime;
    /** 超时时间, 秒 */
    private Integer timeout;

}


