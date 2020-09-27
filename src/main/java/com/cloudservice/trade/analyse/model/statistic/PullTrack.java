package com.cloudservice.trade.analyse.model.statistic;

import com.cloudservice.trade.huobi.model.spot.Kline;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 拉盘追踪
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/17
 */
@Getter
@Setter
@ToString
public class PullTrack implements Serializable {
    private static final long serialVersionUID = 3023009855683031967L;

    /** 开始时间 */
    private Date beginTime;
    /** 结束时间 */
    private Date endTime;
    /** 开始价格 */
    private BigDecimal beginPrice;
    /** 结束价格 */
    private BigDecimal endPrice;
    /** 拉盘比例 */
    private BigDecimal rate;
    /** K线数据集合 */
    private List<Kline> klineList;
    /** 前正向拉盘集合 */
    private List<PullTrack> beforePositiveList;
    /** 前反向拉盘集合 */
    private List<PullTrack> beforeReverseList;

    public PullTrack() {
        this.klineList = new ArrayList<>();
        this.beforePositiveList = new ArrayList<>();
        this.beforeReverseList = new ArrayList<>();
    }

}


