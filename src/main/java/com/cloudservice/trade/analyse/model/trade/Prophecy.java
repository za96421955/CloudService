package com.cloudservice.trade.analyse.model.trade;

import com.cloudservice.trade.analyse.model.statistic.KlineRange;
import com.cloudservice.trade.huobi.enums.PeriodEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 预言
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/18
 */
@Getter
@Setter
@ToString
public class Prophecy implements Serializable {
    private static final long serialVersionUID = -3723112066796843206L;

    /** 时间片 */
    private PeriodEnum period;
    /** K线差价区间 */
    private KlineRange klineRange;
    /** 优先级：rate < 8：放弃，8~12：1，12~16：2，16~20：3，>20：4 */
    private int priority;

    public Prophecy setPeriod(PeriodEnum period) {
        this.period = period;
        return this;
    }

    public Prophecy setKlineRange(KlineRange klineRange) {
        this.klineRange = klineRange;
        return this;
    }

    public Prophecy setPriority(int priority) {
        this.priority = priority;
        return this;
    }

}


