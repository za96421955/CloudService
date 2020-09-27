package com.cloudservice.trade.huobi.service.swap;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.trade.huobi.enums.ContractCodeEnum;
import com.cloudservice.trade.huobi.enums.ContractTradeTypeEnum;
import com.cloudservice.trade.huobi.model.contract.Account;
import com.cloudservice.trade.huobi.model.contract.Position;

import java.util.List;

/**
 * 永续合约：账户接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/5
 */
public interface SwapAccountService {

    /**
     * @description 获取账户信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:35
     * @param access, secret, contractCode
     *
     * @return*/
    Account getAccountInfo(String access, String secret, ContractCodeEnum contractCode);

    /**
     * @description 获取用户持仓信息集合
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:59
     * @param access, secret, contractCode
     */
    List<Position> getPositionList(String access, String secret, ContractCodeEnum contractCode);

    /**
     * @description 获取用户持仓信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:59
     * @param access, secret, contractCode
     */
    Position getPositionInfo(String access, String secret, ContractCodeEnum contractCode);

    /**
     * @description 查询用户财务记录
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 16:15
     * @param access, secret, contractCode, type, days, page, pageSize
     * @param typeList
     **/
    JSONObject getFinancialRecord(String access, String secret, ContractCodeEnum contractCode, List<ContractTradeTypeEnum> typeList
            , int days, int page, int pageSize);

    /**
     * @description 查询用户当前的手续费费率
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:59
     * @param access, secret, contractCode
     *
     * @return*/
    JSONObject getFee(String access, String secret, ContractCodeEnum contractCode);

}


