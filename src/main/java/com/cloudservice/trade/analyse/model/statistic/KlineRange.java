package com.cloudservice.trade.analyse.model.statistic;

import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * K线差价区间
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/10
 */
@Getter
@Setter
@ToString
public class KlineRange implements Serializable {
    private static final long serialVersionUID = -2328088806260457754L;

    /**  差价区间 */
    private int range;
    /**  开多（涨）数 */
    private int buyCount;
    /**  开空（跌）数 */
    private int sellCount;
    /**  开盘上涨比例 */
    private BigDecimal highRate;
    /**  开盘下跌比例 */
    private BigDecimal lowRate;

    public KlineRange(int range) {
        this.range = range;
        this.buyCount = 0;
        this.sellCount = 0;
        this.highRate = BigDecimal.ZERO;
        this.lowRate = BigDecimal.ZERO;
    }

    public void addCount(ContractDirectionEnum direction) {
        if (ContractDirectionEnum.BUY.equals(direction)) {
            buyCount++;
        } else {
            sellCount++;
        }
    }

    public BigDecimal getBuyRate() {
        return BigDecimal.valueOf(buyCount)
                .divide(BigDecimal.valueOf(buyCount + sellCount), new MathContext(4));
    }

    public BigDecimal getSellRate() {
        return BigDecimal.valueOf(sellCount)
                .divide(BigDecimal.valueOf(buyCount + sellCount), new MathContext(4));
    }

    public void setHighRate(BigDecimal highRate) {
        if (this.highRate.compareTo(BigDecimal.ZERO) <= 0) {
            this.highRate = highRate;
        }
        this.highRate = this.highRate.add(highRate).divide(BigDecimal.valueOf(2), new MathContext(4));
    }

    public void setLowRate(BigDecimal lowRate) {
        if (this.lowRate.compareTo(BigDecimal.ZERO) <= 0) {
            this.lowRate = lowRate;
        }
        this.lowRate = this.lowRate.add(lowRate).divide(BigDecimal.valueOf(2), new MathContext(4));
    }

    public BigDecimal getBuyRate100() {
        return this.getBuyRate().multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getSellRate100() {
        return this.getSellRate().multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getHighRate100() {
        return this.getHighRate().multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getLowRate100() {
        return this.getLowRate().multiply(BigDecimal.valueOf(100));
    }

}


