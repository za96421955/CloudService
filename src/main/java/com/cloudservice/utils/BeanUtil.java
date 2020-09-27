package com.cloudservice.utils;
/*
 * Copyright (C), 2002-2018, 苏宁易购电子商务有限公司
 * FileName: com.suning.logistics.jwms.utils
 * Author:   陈晨(17061934)
 * Date:     2020/1/7 19:44
 * Description: // 模块目的、功能描述      
 * History:		// 修改记录
 * <author>		<time>		<version>		<desc>
 * 17061934		2020/1/7 19:44		1.0		<描述>
 */

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @version v1.0
 * @Description: 对象工具
 * <p>〈功能详细描述〉</p>
 *
 * @ClassName BeanUtil
 * @author 陈晨(17061934)
 * @date 2020/1/7 19:44
 */
public final class BeanUtil {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    private BeanUtil() {}

//    /**
//     * @description 序列化
//     * <p>〈功能详细描述〉</p>
//     *
//     * @auther: 鲁高宇 (18066659)
//     * @date: 2019/7/16  23:11
//     * @param   value
//     */
//    public static String beanToString(Object value) {
//        if (null == value) {
//            return null;
//        }
//        return JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect);
//    }
//
//    /**
//     * @description 反序列化
//     * <p>〈功能详细描述〉</p>
//     *
//     * @auther: 鲁高宇 (18066659)
//     * @date: 2019/7/16  23:12
//     * @param   value
//     */
//    public static Object stringToBean(String value, Type returnType) {
//        Class<T> clazz = (Class<T>) getActualClassType(returnType);
//        return stringToBean(value, clazz);
//    }
//
//    /**
//     * @description 反序列化
//     * <p>〈功能详细描述〉</p>
//     *
//     * @auther: 鲁高宇 (18066659)
//     * @date: 2019/7/16  23:12
//     * @param   value
//     */
//    private static Object stringToBean(String value, Class<T> clazz) {
//        Object obj = JSON.parse(value);
//        if (obj instanceof JSONObject) {
//            return JSON.parseObject(value, clazz);
//        } else if(obj instanceof JSONArray) {
//            return JSON.parseArray(value, clazz);
//        } else {
//            return obj;
//        }
//    }

    /**
     * 功能描述: 获取泛型类型参数类型
     *
     * @auther: 鲁高宇 (18066659)
     * @date: 2019/7/18 20:33
     */
    private static Object getActualClassType(Type returnType) {
        if (null == returnType) {
            return null;
        }
        Object actualType;
        if (returnType instanceof ParameterizedType) {
            ParameterizedType pReturnType = (ParameterizedType) returnType;
            Type[] types = pReturnType.getActualTypeArguments();
            boolean isMap = types.length == 2;
            if (isMap) {
                // List<Map<String,Object>
                actualType = HashMap.class;
            } else {
                // List<JavaBean>
                actualType = types[0];
            }
        } else {
            // JavaBean
            actualType = returnType;
        }
        return actualType;
    }

    private static final String SPLIT_SQLTIME = "@@!";
    private static final String SPLIT_FIELD = "@";
    private static final String SPLIT_INFO = ";";

    /**
     * @description Hessian序列化
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨(17061934)
     * @date    2020/1/8 21:09
     */
    public static String serialHessian(Object obj) {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        HessianOutput output = null;
        try {
            String sqlTimeSerial = serialSqlTime(obj);
            baos = new ByteArrayOutputStream();
            output = new HessianOutput(baos);
            output.writeObject(obj);
            byte[] bytes = baos.toByteArray();
            return new String(bytes, "UTF-8")
                    + SPLIT_SQLTIME + sqlTimeSerial;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("obj=" + obj + ", 序列化异常, " + e.getMessage());
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String serialSqlTime(Object obj) throws Exception {
        List<Field> fieldList = getDeclaredFields(obj.getClass());
        StringBuilder sqlTimeSerial = new StringBuilder();
        for (Field field : fieldList) {
            long time = getSqlTime(obj, field);
            if (time < 0) {
                continue;
            }
            sqlTimeSerial.append(SPLIT_FIELD).append(field.getName());
            sqlTimeSerial.append(SPLIT_INFO).append(time);
            field.set(obj, null);
        }
        return sqlTimeSerial.toString();
    }

    private static List<Field> getDeclaredFields(Class clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }
        List<Field> fieldList = new ArrayList<>();
        Collections.addAll(fieldList, clazz.getDeclaredFields());
        fieldList.addAll(getDeclaredFields(clazz.getSuperclass()));
        return fieldList;
    }

    private static long getSqlTime(Object obj, Field field) throws Exception {
        field.setAccessible(true);
        if (field.get(obj) == null) {
            return -1;
        }
        Class type = field.getType();
        if (type.getName().equals("java.sql.Date")) {
            return ((Date) field.get(obj)).getTime();
        }
        else if (type.getName().equals("java.sql.Timestamp")) {
            return ((Timestamp) field.get(obj)).getTime();
        }
        else if (type.getName().equals("java.sql.Time")) {
            return ((Time) field.get(obj)).getTime();
        }
        return -1;
    }

    /**
     * @description Hessian反序列化
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨(17061934)
     * @date    2020/1/8 21:10
     */
    public static <T> T reserialHessian(String serial) {
        if (StringUtils.isBlank(serial)) {
            return null;
        }
        String[] spliteSerial = serial.split(SPLIT_SQLTIME);
        String sqlTimeSerial = null;
        if (spliteSerial.length > 1) {
            sqlTimeSerial = serial.split(SPLIT_SQLTIME)[1];
        }

        ByteArrayInputStream bais = null;
        HessianInput input = null;
        try {
            byte[] bytes = spliteSerial[0].getBytes("UTF-8");
            bais = new ByteArrayInputStream(bytes);
            input = new HessianInput(bais);
            T obj = (T) input.readObject();
            reserialSqlTime(obj, sqlTimeSerial);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("serial=" + serial + ", 反序列化异常, " + e.getMessage());
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void reserialSqlTime(Object obj, String sqlTimeSerial) throws Exception {
        if (StringUtils.isBlank(sqlTimeSerial)) {
            return;
        }
        String[] serialInfos = sqlTimeSerial.substring(1).split(SPLIT_FIELD);
        for (String serialInfo : serialInfos) {
            String[] sqlTimeInfos = serialInfo.split(SPLIT_INFO);
            Field field = getField(obj.getClass(), sqlTimeInfos[0]);
            setSqlTime(obj, field, Long.parseLong(sqlTimeInfos[1]));
        }
    }

    private static Field getField(Class clazz, String fieldName) throws Exception {
        if (clazz == null) {
            return null;
        }
        Field field = null;
        for (Field f : clazz.getDeclaredFields()) {
            if (f.getName().equals(fieldName)) {
                field = f;
            }
        }
        if (field == null) {
            return getField(clazz.getSuperclass(), fieldName);
        }
        return field;
    }

    private static void setSqlTime(Object obj, Field field, long time) throws Exception {
        Class type = field.getType();
        field.setAccessible(true);
        Object value = null;
        if (type.getName().equals("java.sql.Date")) {
            value = new Date(time);
        }
        else if (type.getName().equals("java.sql.Timestamp")) {
            value = new Timestamp(time);
        }
        else if (type.getName().equals("java.sql.Time")) {
            value = new Time(time);
        }
        field.set(obj, value);
    }

    /**
     * @description Java序列化
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/8/12 10:30
     * @param obj
     **/
    public static String serial(Object obj) {
        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream objectOut = null;
        try {
            byteOut = new ByteArrayOutputStream();
            objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(obj);
            StringBuilder sb = new StringBuilder();
            for (byte b : byteOut.toByteArray()) {
                sb.append((char) b);
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("[序列化] obj={}, 对象序列化异常, {}", obj, e.getMessage(), e);
        } finally {
            try {
                if (objectOut != null) {
                    objectOut.close();
                }
                if (byteOut != null) {
                    byteOut.close();
                }
            } catch (Exception ex) {
                logger.error("[序列化] obj={}, 输出流关闭异常, {}", obj, ex.getMessage(), ex);
            }
        }
        return null;
    }

    /**
     * @description Java反序列化
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/8/12 10:30
     * @param serial
     **/
    public static Object reserial(String serial) {
        byte[] bytes = new byte[serial.length()];
        for (int i = 0; i < serial.length(); i++) {
            bytes[i] = (byte) serial.charAt(i);
        }
        ByteArrayInputStream byteInput = null;
        ObjectInputStream objectInput = null;
        try {
            byteInput = new ByteArrayInputStream(bytes);
            objectInput = new ObjectInputStream(byteInput);
            return objectInput.readObject();
        } catch (Exception e) {
            logger.error("[序列化] serial={}, 对象反序列化异常, {}", serial, e.getMessage(), e);
        } finally {
            try {
                if (objectInput != null) {
                    objectInput.close();
                }
                if (byteInput != null) {
                    byteInput.close();
                }
            } catch (Exception ex) {
                logger.error("[序列化] serial={}, 输入流关闭异常, {}", serial, ex.getMessage(), ex);
            }
        }
        return null;
    }

}


