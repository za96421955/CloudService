package com.cloudservice.trade.huobi.service.contract.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseService;
import com.cloudservice.trade.huobi.context.ContractAPI;
import com.cloudservice.trade.huobi.context.Host;
import com.cloudservice.trade.huobi.enums.ContractTradeTypeEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import com.cloudservice.trade.huobi.model.contract.Account;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.service.HuobiHttpRequest;
import com.cloudservice.trade.huobi.service.contract.ContractAccountService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 交割合约：账户接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
@Service
public class ContractAccountServiceImpl extends BaseService implements ContractAccountService {

    @Override
    public Account getAccountInfo(String access, String secret, SymbolEnum symbol) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Account.ACCOUNT_INFO.getApi(), data.toString());
        return Account.parseList(this.getDataArray(result)).get(0);
    }

    @Override
    public List<Position> getPositionList(String access, String secret, SymbolEnum symbol) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Account.POSITION_INFO.getApi(), data.toString());
        return Position.parseList(this.getDataArray(result));
    }

    @Override
    public Position getPositionInfo(String access, String secret, SymbolEnum symbol) {
        List<Position> positionList = this.getPositionList(access, secret, symbol);
        if (CollectionUtils.isEmpty(positionList) || positionList.get(0) == null) {
            return null;
        }
        return positionList.get(0);
    }

    @Override
    public JSONObject getFinancialRecord(String access, String secret, SymbolEnum symbol, List<ContractTradeTypeEnum> typeList
            , int days, int page, int pageSize) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("create_date", days);
        data.put("page_index", page);
        data.put("page_size", pageSize);
        if (!CollectionUtils.isEmpty(typeList)) {
            StringBuilder types = new StringBuilder();
            for (ContractTradeTypeEnum type : typeList) {
                if (type != null) {
                    types.append(",").append(type.getValue());
                }
            }
            data.put("type", types.substring(1));
        }
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Account.FINANCIAL_RECORD.getApi(), data.toString());
        return JSONObject.parseObject(result);
    }

    @Override
    public Account getAccountPositionInfo(String access, String secret, SymbolEnum symbol) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Account.ACCOUNT_POSITION_INFO.getApi(), data.toString());
        return Account.parseList(this.getDataArray(result)).get(0);
    }

    @Override
    public JSONObject getFee(String access, String secret, SymbolEnum symbol) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Account.FEE.getApi(), data.toString());
        return JSONObject.parseObject(result);
    }

}


