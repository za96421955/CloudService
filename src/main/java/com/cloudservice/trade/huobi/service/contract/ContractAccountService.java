package com.cloudservice.trade.huobi.service.contract;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.trade.huobi.enums.ContractTradeTypeEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import com.cloudservice.trade.huobi.model.contract.Account;
import com.cloudservice.trade.huobi.model.contract.Position;

import java.util.List;

/**
 * 交割合约：账户接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/5
 */
public interface ContractAccountService {

    /**
     * @description 获取账户信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:35
     * @param access, secret, symbol
     *
     * @return*/
    Account getAccountInfo(String access, String secret, SymbolEnum symbol);

    /**
     * @description 获取用户持仓信息集合
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:59
     * @param access, secret, symbol
     */
    List<Position> getPositionList(String access, String secret, SymbolEnum symbol);

    /**
     * @description 获取用户持仓信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:59
     * @param access, secret, symbol
     */
    Position getPositionInfo(String access, String secret, SymbolEnum symbol);

    /**
     * @description 查询用户财务记录
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 16:15
     * @param access, secret, symbol, type, days, page, pageSize
     * @param typeList
     **/
    JSONObject getFinancialRecord(String access, String secret, SymbolEnum symbol, List<ContractTradeTypeEnum> typeList
            , int days, int page, int pageSize);

    /**
     * @description 查询用户账户和持仓信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:59
     * @param access, secret, symbol
     *
     * @return*/
    Account getAccountPositionInfo(String access, String secret, SymbolEnum symbol);

    /**
     * @description 查询用户当前的手续费费率
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:59
     * @param access, secret, symbol
     *
     * @return*/
    JSONObject getFee(String access, String secret, SymbolEnum symbol);

}


