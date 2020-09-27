package com.cloudservice.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
public class BaseService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JSONObject getData(String response) {
        JSONObject result = JSONObject.parseObject(response);
        return result.getJSONObject("data");
    }

    public JSONArray getDataArray(String response) {
        JSONObject result = JSONObject.parseObject(response);
        return result.getJSONArray("data");
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


