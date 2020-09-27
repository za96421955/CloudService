package com.cloudservice.trade.analyse.service.trade.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseService;
import com.cloudservice.trade.analyse.model.statistic.KlineRange;
import com.cloudservice.trade.analyse.model.statistic.ProfitLoss;
import com.cloudservice.trade.analyse.model.statistic.PullTrack;
import com.cloudservice.trade.analyse.service.trade.StatisticService;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.contract.ContractTradeService;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import com.cloudservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

/**
 * 统计服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/16
 */
@Service
public class StatisticServiceImpl extends BaseService implements StatisticService {

    @Autowired
    private SpotMarketService spotMarketService;
    @Autowired
    private ContractTradeService contractTradeService;

    @Override
    public JSONObject transactionOrder(String access, String secret, String symbol, String tradeType
            , String beginTime, String endTime) {
        List<ProfitLoss> profitLossList = this.getProfitLossList(access, secret, symbol, tradeType, beginTime, endTime);
        // 统计盈利、亏损、手续费
        int buyCount = 0;
        int sellCount = 0;
        int profitCount = 0;
        int lossCount = 0;
        BigDecimal profit = BigDecimal.ZERO;
        BigDecimal loss = BigDecimal.ZERO;
        BigDecimal fee = BigDecimal.ZERO;
        JSONArray timeData = new JSONArray();
        JSONArray incomeData = new JSONArray();
        for (ProfitLoss profitLoss : profitLossList) {
            if (profitLoss == null) {
                continue;
            }
            fee = fee.add(profitLoss.getFee());
            if (ContractDirectionEnum.BUY.equals(profitLoss.getDirection())) {
                buyCount++;
            } else {
                sellCount++;
            }
            if (profitLoss.getIncome().compareTo(BigDecimal.ZERO) > 0) {
                profit = profit.add(profitLoss.getIncome());
                profitCount++;
            } else {
                loss = loss.add(profitLoss.getIncome());
                lossCount++;
            }
            timeData.add(profitLoss.getOpenTime());
            incomeData.add(profitLoss.getIncome());
        }
        // 设置返回信息
        JSONObject count = new JSONObject();
        count.put("openCount", profitLossList.size());
        count.put("buyCount", buyCount);
        count.put("sellCount", sellCount);
        count.put("profitCount", profitCount);
        count.put("lossCount", lossCount);
        JSONObject funds = new JSONObject();
        funds.put("profit", profit);
        funds.put("loss", loss);
        funds.put("income", profit.add(loss));
        funds.put("fee", fee);
        funds.put("totalIncome", profit.add(loss).add(fee));
        JSONObject data = new JSONObject();
        data.put("timeData", timeData);
        data.put("incomeData", incomeData);
        JSONObject result = new JSONObject();
        result.put("count", count);
        result.put("funds", funds);
        result.put("data", data);
        result.put("profitLossList", profitLossList);
        return result;
    }

    /**
     * @description 获取盈亏信息集合
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/22 11:16
     * @param access, secret, symbol, tradeType, beginTime, endTime
     **/
    private List<ProfitLoss> getProfitLossList(String access, String secret, String symbol, String tradeType
            , String beginTime, String endTime) {
        List<Order> orderList = contractTradeService.getOrderHistory(access, secret, SymbolEnum.get(symbol), ContractTradeTypeHistoryEnum.get(tradeType)
                , 1, 50
                , DateUtil.parse(beginTime, DateUtil.DATE_LONG)
                , DateUtil.parse(endTime, DateUtil.DATE_LONG));
        List<ProfitLoss> profitLossList = new ArrayList<>();
        ProfitLoss currBuy = null;
        ProfitLoss currSell = null;
        for (int i = orderList.size() - 1; i >= 0; i--) {
            Order order = orderList.get(i);
            if (order == null) {
                continue;
            }
            if (ContractOffsetEnum.OPEN.getValue().equals(order.getOffset())) {
                if (ContractDirectionEnum.BUY.getValue().equals(order.getDirection())) {
                    if (currBuy == null) {
                        currBuy = new ProfitLoss();
                        currBuy.setDirection(ContractDirectionEnum.get(order.getDirection()));
                        currBuy.setOpenTime(DateUtil.format(DateUtil.now(order.getCreateDate()), DateUtil.DATE_LONG));
                        currBuy.setFee(order.getTradeFee());
                        currBuy.setOpenPrice(order.getTradePrice());
                    } else {
                        currBuy.setFee(currBuy.getFee().add(order.getTradeFee()));
                    }
                    currBuy.getPriceVolumeList().add(new Object[]{order.getTradePrice(), order.getTradeVolume()});
                } else {
                    if (currSell == null) {
                        currSell = new ProfitLoss();
                        currSell.setDirection(ContractDirectionEnum.get(order.getDirection()));
                        currSell.setOpenTime(DateUtil.format(DateUtil.now(order.getCreateDate()), DateUtil.DATE_LONG));
                        currSell.setFee(order.getTradeFee());
                        currSell.setOpenPrice(order.getTradePrice());
                    } else {
                        currSell.setFee(currSell.getFee().add(order.getTradeFee()));
                    }
                    currSell.getPriceVolumeList().add(new Object[]{order.getTradePrice(), order.getTradeVolume()});
                }
            } else {
                if (ContractDirectionEnum.SELL.getValue().equals(order.getDirection())) {
                    if (currBuy != null) {
                        currBuy.setCloseTime(DateUtil.format(DateUtil.now(order.getCreateDate()), DateUtil.DATE_LONG));
                        currBuy.setIncome(order.getOffsetProfitloss());
                        currBuy.setFee(currBuy.getFee().add(order.getTradeFee()));
                        currBuy.setClosePrice(order.getTradePrice());
                        profitLossList.add(currBuy);
                        currBuy = null;
                    }
                } else {
                    if (currSell != null) {
                        currSell.setCloseTime(DateUtil.format(DateUtil.now(order.getCreateDate()), DateUtil.DATE_LONG));
                        currSell.setIncome(order.getOffsetProfitloss());
                        currSell.setFee(currSell.getFee().add(order.getTradeFee()));
                        currSell.setClosePrice(order.getTradePrice());
                        profitLossList.add(currSell);
                        currSell = null;
                    }
                }
            }
        }
        return profitLossList;
    }

    @Override
    public Map<Integer, KlineRange> getKlineRange(SymbolUSDTEnum symbol) {
        // 获取各时间粒度区间信息
//        Map<Integer, KlineRange> klineRangeMap_1 = this.getKlineRange(symbol, PeriodEnum.MIN_1, 2000);
        Map<Integer, KlineRange> klineRangeMap_5 = this.getKlineRange(symbol, PeriodEnum.MIN_5, 2000);
        Map<Integer, KlineRange> klineRangeMap_15 = this.getKlineRange(symbol, PeriodEnum.MIN_15, 2000);
        Map<Integer, KlineRange> klineRangeMap_30 = this.getKlineRange(symbol, PeriodEnum.MIN_30, 2000);
//        Map<Integer, KlineRange> klineRangeMap_60 = this.getKlineRange(symbol, PeriodEnum.MIN_60, 2000);
        // 粒度信息合并
        Map<Integer, KlineRange> klineRangeMap = new HashMap<>();
        for (int i = 1; i <= SpotMarketService.RANGE_COUNT; i++) {
            KlineRange klineRange = klineRangeMap.get(i);
            if (klineRange == null) {
                klineRangeMap.put(i, new KlineRange(i));
                klineRange = klineRangeMap.get(i);
            }
//            this.mergeKlineRange(klineRange, klineRangeMap_1.get(i));
            this.mergeKlineRange(klineRange, klineRangeMap_5.get(i));
            this.mergeKlineRange(klineRange, klineRangeMap_15.get(i));
            this.mergeKlineRange(klineRange, klineRangeMap_30.get(i));
//            this.mergeKlineRange(klineRange, klineRangeMap_60.get(i));
        }
        return klineRangeMap;
    }

    /**
     * @description 合并K线区间信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/16 16:16
     **/
    private void mergeKlineRange(KlineRange klineRange, KlineRange merged) {
        klineRange.setBuyCount(klineRange.getBuyCount() + merged.getBuyCount());
        klineRange.setSellCount(klineRange.getSellCount() + merged.getSellCount());
        klineRange.setHighRate(merged.getHighRate());
        klineRange.setLowRate(merged.getLowRate());
    }

    @Override
    public Map<Integer, KlineRange> getKlineRange(SymbolUSDTEnum symbol, PeriodEnum period, int size) {
        List<Kline> klineList = spotMarketService.getKline(symbol, period, size);
        // 从1开始统计, 忽略当前项、最后一项
        // 预言机
        Map<Integer, KlineRange> klineRangeMap = new HashMap<>();
        for (int i = 1; i < klineList.size() - 1; i++) {
            Kline curr = klineList.get(i);
            if (curr.isSmallDiffRange()) {
                continue;
            }
            Kline last = klineList.get(i + 1);
            int range = spotMarketService.getRange(last, curr.getOpen());
            KlineRange klineRange = klineRangeMap.get(range);
            if (klineRange == null) {
                klineRangeMap.put(range, new KlineRange(range));
                klineRange = klineRangeMap.get(range);
            }
            klineRange.setHighRate(curr.getHigh().subtract(curr.getOpen()).divide(curr.getOpen(), new MathContext(8)));
            klineRange.setLowRate(curr.getLow().subtract(curr.getOpen()).abs().divide(curr.getOpen(), new MathContext(8)));
            klineRange.addCount(spotMarketService.getDirection(klineRange));
        }
        return klineRangeMap;
    }

    @Override
    public KlineFluctuationEnum getFluctuation(SymbolUSDTEnum symbol) {
        return null;
    }

    @Override
    public List<PullTrack> getPullTrack(SymbolUSDTEnum symbol, PeriodEnum period, int size) {
        // 获取涨跌比最大的20%的K线数据
        List<Kline> klineList = spotMarketService.getKline(symbol, period, size);
        int top20Size = (int) (klineList.size() * 0.2);
        Set<Kline> top20Set = new TreeSet<>();
        for (Kline kline : klineList) {
            if (kline == null) {
                continue;
            }
            if (top20Set.size() < top20Size) {
                top20Set.add(kline);
            } else {
                Kline first = top20Set.iterator().next();
                if (kline.compareTo(first) > 0) {
                    top20Set.remove(first);
                    top20Set.add(kline);
                }
            }
        }
        // 生成拉盘数据
        List<PullTrack> pullTrackList = new ArrayList<>();
        for (Kline kline : top20Set) {
            PullTrack pullTrack = new PullTrack();
            pullTrack.setBeginTime(DateUtil.now(kline.getId() * 1000));
            pullTrack.setBeginPrice(kline.getOpen());
            pullTrack.setEndTime(DateUtil.now((kline.getId() + period.second()) * 1000));
            pullTrack.setEndPrice(kline.getClose());
            pullTrack.setRate(kline.getPullRate());
            pullTrack.getKlineList().add(kline);
            // 时区调整
            pullTrack.setBeginTime(DateUtil.addHour(pullTrack.getBeginTime(), -8));
            pullTrack.setEndTime(DateUtil.addHour(pullTrack.getEndTime(), -8));
            pullTrackList.add(pullTrack);
        }
        return pullTrackList;
    }

    public static void main(String[] args) {
        Set<Kline> set = new TreeSet<>();
        Kline kline = new Kline();
        kline.setOpen(BigDecimal.valueOf(380));
        kline.setClose(BigDecimal.valueOf(360));
        set.add(kline);
        kline = new Kline();
        kline.setClose(BigDecimal.valueOf(400));
        kline.setOpen(BigDecimal.valueOf(375));
        set.add(kline);
        kline = new Kline();
        kline.setClose(BigDecimal.valueOf(360));
        kline.setOpen(BigDecimal.valueOf(340));
        set.add(kline);
        kline = new Kline();
        kline.setClose(BigDecimal.valueOf(350));
        kline.setOpen(BigDecimal.valueOf(330));
        set.add(kline);
        kline = new Kline();
        kline.setOpen(BigDecimal.valueOf(321));
        kline.setClose(BigDecimal.valueOf(302));
        set.add(kline);
        System.out.println(set);
        System.out.println(set.iterator().next());
    }

}


