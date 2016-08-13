package utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import json.PreLoginResponseMessage;

/**
 * @author 解析JSON格式的数据工具类
 */
public class JsonUtils {

    //将预登录时的json字符串转换为对应的bean
    public static PreLoginResponseMessage jsontoPreLoginResponseMessage(String jsondata) {
        JSONObject jsonobj = JSONObject.fromObject(jsondata);
        PreLoginResponseMessage message = (PreLoginResponseMessage) JSONObject.toBean(jsonobj, PreLoginResponseMessage.class);
        return message;
    }

    //把一个数据转换为json字符串
    public static String beantojsonArray(Object obj) {
        JSONArray jsonarray = JSONArray.fromObject(obj);
        return jsonarray.toString();
    }

    //把一个bean对象转换为json字符串
    public static String beantojson(Object obj) {
        JSONObject jsonobj = JSONObject.fromObject(obj);
        return jsonobj.toString();
    }
}
