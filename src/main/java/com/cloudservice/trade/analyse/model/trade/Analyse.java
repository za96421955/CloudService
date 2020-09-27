package com.cloudservice.trade.analyse.model.trade;

import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 现价分析
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/10
 */
@Getter
@Setter
@ToString
public class Analyse implements Serializable {
    private static final long serialVersionUID = -1832028652673367894L;

    /** 开多/开空 */
    private ContractDirectionEnum direction;
    /** 下单价 */
    private BigDecimal price;
    /** 止盈触发价 */
    private BigDecimal triggerProfit;
    /** 止盈价 */
    private BigDecimal profit;
    /** 止损触发价 */
    private BigDecimal triggerLoss;
    /** 止损价 */
    private BigDecimal loss;

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(3, BigDecimal.ROUND_DOWN);
    }

    public void setTriggerProfit(BigDecimal triggerProfit) {
        this.triggerProfit = triggerProfit.setScale(3, BigDecimal.ROUND_DOWN);
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit.setScale(3, BigDecimal.ROUND_DOWN);
    }

    public void setTriggerLoss(BigDecimal triggerLoss) {
        this.triggerLoss = triggerLoss.setScale(3, BigDecimal.ROUND_DOWN);
    }

    public void setLoss(BigDecimal loss) {
        this.loss = loss.setScale(3, BigDecimal.ROUND_DOWN);
    }

    /**
     * @description 分析取反
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/21 19:13
     **/
    public void negate() {
        this.direction = this.direction.getNegate();
        BigDecimal temp = this.triggerProfit;
        this.triggerProfit = this.triggerLoss;
        this.triggerLoss = temp;
        temp = this.profit;
        this.profit = this.loss;
        this.loss = temp;
    }

}


