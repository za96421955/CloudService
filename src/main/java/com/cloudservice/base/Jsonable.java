package com.cloudservice.base;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/28
 */
public interface Jsonable<T> {

    /**
     * @description 对象转JSON
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/28 21:20
     **/
    JSONObject toJson();

    /**
     * @description JSON字符串转对象
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/28 21:21
     * @param json
     **/
    T fromJson(String json);

}


