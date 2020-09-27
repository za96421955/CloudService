package com.cloudservice.trade.analyse.model.statistic;

import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 盈亏统计
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/17
 */
@Getter
@Setter
@ToString
public class ProfitLoss implements Serializable {
    private static final long serialVersionUID = 4244992564137032520L;

    private ContractDirectionEnum direction;
    private BigDecimal income;
    private BigDecimal fee;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private String openTime;
    private String closeTime;
    private List<Object> priceVolumeList = new ArrayList<>();

}


