package com.arthas.common.json;

import com.alibaba.fastjson.JSONObject;
import com.arthas.common.constant.Constant;

/**
 * 
 * @ClassName: JSONResult
 * @Description: JSON 处理相关工具类实现
 * @author DavieAC
 * @date 2017年10月5日 下午8:08:46
 *
 */
public class JSONResult {
    
    public static JSONObject getJSONObject(String code, String message, Object obj) {
        JSONObject json = new JSONObject();
        json.put(Constant.CODE, code);
        json.put(Constant.MESSAGE, message);
        json.put(Constant.OBJECT, obj);
        return json;
    }

    public static void main(String[] args) {

    }

}
