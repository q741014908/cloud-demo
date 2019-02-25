package com.whb.cloud.demo.common.util;

import com.google.common.base.Strings;
import com.whb.cloud.demo.common.consts.RequestConst;
import com.whb.cloud.demo.common.model.RequestUserInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class RequestUtils {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String JWT_BEARER = "Bearer ";

    public static RequestUserInfo getCurrentRequestUserInfo(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) (RequestContextHolder.getRequestAttributes());
        return (RequestUserInfo) servletRequestAttributes.getRequest().getAttribute(RequestConst.LOGIN_USER);
    }

    public static String getCurrentRequestToken(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) (RequestContextHolder.getRequestAttributes());
        String bearer = servletRequestAttributes.getRequest().getHeader(HEADER_AUTHORIZATION);
        if(Strings.emptyToNull(bearer)!=null && bearer.startsWith(JWT_BEARER)){
            return bearer.substring(JWT_BEARER.length());
        }
        return bearer;
    }
}
