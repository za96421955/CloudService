package com.cloudservice.trade.huobi.model.spot;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 买单/卖单，明细
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@Getter
@Setter
@ToString
public class DepthDetail implements Serializable {
    private static final long serialVersionUID = 5625453473002107386L;

    /** 价格 */
    private BigDecimal price;
    /** 数量 */
    private BigDecimal size;

    // 项目属性
    /** 累计总价 */
    private BigDecimal totalPrice;
    /** 占比 */
    private BigDecimal rate;

    public DepthDetail compareMultiple(DepthDetail target, BigDecimal multiple) {
        if (compare(this, target, multiple)) {
            return this;
        }
        if (compare(target, this, multiple)) {
            return target;
        }
        return null;
    }

    public static boolean compare(DepthDetail arg1, DepthDetail arg2, BigDecimal multiple) {
        return arg1.getSize().compareTo(arg2.getSize()) > 0
                && arg1.getSize().divide(arg2.getSize(), new MathContext(2)).compareTo(multiple) >= 0;
    }

    public static DepthDetail parse(JSONArray dataArray) {
        if (dataArray == null) {
            return null;
        }
        DepthDetail detail = new DepthDetail();
        detail.setPrice(dataArray.getBigDecimal(0));
        detail.setSize(dataArray.getBigDecimal(1));
        return detail;
    }

    public static List<DepthDetail> parseList(JSONArray dataArray) {
        if (dataArray == null) {
            return Collections.emptyList();
        }
        List<DepthDetail> list = new ArrayList<>();
        for (int i = 0; i < dataArray.size(); i++) {
            list.add(DepthDetail.parse(dataArray.getJSONArray(i)));
        }
        return list;
    }

    public static void main(String[] args) {
        String result =
                " [\n" +
                "      [7964, 0.0678],\n" +
                "      [7963, 0.9162],\n" +
                "      [7961, 0.1],\n" +
                "      [7960, 12.8898],\n" +
                "      [7958, 1.2]\n" +
                "    ]\n";
        System.out.println(DepthDetail.parseList(JSONArray.parseArray(result)));
    }

}


