package com.whb.cloud.demo.common.util;

import com.whb.cloud.demo.common.consts.JwtConst;
import com.whb.cloud.demo.common.model.RequestUserInfo;

import java.util.Map;

/**
 * jwt用户相关的工具类
 */
public class JwtUserUtils {
    /**
     * 将一个jwt的token字符串转换为RequestUserInfo
     * @param jwtTokenInfoStr
     * @return
     */
    public static RequestUserInfo parsejwtUserMap2RequestUserInfo(String jwtTokenInfoStr){
        Map<String,Object> jwtTokenInfoMap = MapUtils.parseJwtStr2Map(jwtTokenInfoStr);
        RequestUserInfo userInfo = new RequestUserInfo();
        userInfo.setName((String) jwtTokenInfoMap.get(JwtConst.JWT_KEY_USER_NAME));
        userInfo.setRols((String) jwtTokenInfoMap.get(JwtConst.JWT_KEY_AUTHORITIES));
        return userInfo;
    }
}
