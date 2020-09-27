package com.cloudservice.trade.huobi.enums;

/**
 * 深度
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public enum DepthEnum {
    DEPTH_5("5", "当前价格上下的5条数据")
    , DEPTH_10("10", "当前价格上下的10条数据")
    , DEPTH_20("20", "当前价格上下的20条数据")
    , DEPTH_150("150", "当前价格上下的150条数据 (当type值为‘step0’时,‘depth’的默认值为150而非20)")
    ;

    private final String value;
    private final String desc;

    DepthEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static DepthEnum get(String value) {
        for (DepthEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}


