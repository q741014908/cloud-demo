package com.whb.cloud.demo.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * map相关的工具类
 */
public class MapUtils {

    private static final String MAP_STR_REGEXP = ",(?=[^,]+=[^,]+)";

    /**
     * 将一个map类型的 key=value的字符串转换为map
     * @param jwtStr
     * @return
     */
    public static Map<String, Object> parseJwtStr2Map(String jwtStr){
        Map<String, Object> map = new HashMap<String, Object>(16);
        //去掉前后括号
        jwtStr = jwtStr.substring(1, jwtStr.length()-1);
        //按“，”将其分为字符数组
        String[] arraydata = jwtStr.split(MAP_STR_REGEXP);
        for (int i = 0; i < arraydata.length; i++) {
            int j = arraydata[i].indexOf("=");
            String value = arraydata[i].substring(j + 1);
            if(value.indexOf("[")==0){
                value = value.substring(1);
            }
            if(value.indexOf("]")==value.length()-1){
                value = value.substring(0,value.length()-1);
            }
            map.put(arraydata[i].substring(0, j).trim(),value );
        }
        return map;
    }
}
