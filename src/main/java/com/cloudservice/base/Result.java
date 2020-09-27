package com.cloudservice.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.ArrayUtils;

import java.io.Serializable;

/**
 * 结果
 * <p>
 * {"status":"ok","data":{"order_id":753353686666870784,"order_id_str":"753353686666870784"},"ts":1599655115538}
 * {"status":"error","err_code":1051,"err_msg":"No orders to cancel.","ts":1599655336683}
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@Getter
@Setter
@ToString
public class Result implements Serializable {
    private static final long serialVersionUID = 1901706731670418596L;

    /** 状态 */
    private String status;
    /** 时间戳 */
    private Long ts;
    /** 数据 */
    private Object data;
    /** 错误码 */
    private String errCode;
    /** 错误内容 */
    private String errMsg;

    public boolean success() {
        return "ok".equalsIgnoreCase(status);
    }

    public <T> T getData() {
        return (T) data;
    }

    public static Result buildSuccess(Object... data) {
        Result result = new Result();
        result.setStatus("ok");
        result.setTs(System.currentTimeMillis());
        if (ArrayUtils.isNotEmpty(data)) {
            if (data.length == 1) {
                result.setData(data[0]);
            } else {
                result.setData(data);
            }
        }
        return result;
    }

    public static Result buildFail(String message, Object... data) {
        Result result = new Result();
        result.setStatus("error");
        result.setTs(System.currentTimeMillis());
        result.setErrMsg(message);
        if (ArrayUtils.isNotEmpty(data)) {
            if (data.length == 1) {
                result.setData(data[0]);
            } else {
                result.setData(data);
            }
        }
        return result;
    }

    public static Result parse(Object data) {
        if (data == null) {
            return null;
        }
        return JSONObject.parseObject(data.toString(), Result.class);
    }

    public static void main(String[] args) {
        String result = "{\n" +
                "\t\"status\": \"ok\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"order_id\": 753353686666870784,\n" +
                "\t\t\"order_id_str\": \"753353686666870784\"\n" +
                "       },\n" +
                "\t\"ts\": 1599655115538\n" +
                "}";
        System.out.println(Result.parse(JSONObject.parseObject(result)));
    }

}


