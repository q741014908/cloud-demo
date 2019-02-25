package com.whb.cloud.demo.common.security.jwt;

import cn.hutool.core.io.FileUtil;
import com.google.common.base.Strings;
import com.whb.cloud.demo.common.consts.RequestConst;
import com.whb.cloud.demo.common.model.RequestUserInfo;
import com.whb.cloud.demo.common.util.JwtUserUtils;
import com.whb.cloud.demo.common.util.JwtUtils;
import com.whb.cloud.demo.common.util.RequestUtils;
import com.whb.cloud.demo.common.util.RsaUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt的拦截器  主要处理解析jwt的json的数据  然后模拟shiro登录  将用户信息交给shiro维护
 */
public class JwtHandleInceptorAdapter extends HandlerInterceptorAdapter {

    private static final String JWT_STR_CHARSET = "utf-8";
    private static final String JWT_PUBLIC_KEY_PATH = "oauth-jwt-publicKey.txt";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        //AuthorizationException
        String currentRequestToken = RequestUtils.getCurrentRequestToken();
        if(Strings.emptyToNull(currentRequestToken)!=null){
            Subject subject = SecurityUtils.getSubject();
            if(subject instanceof WebSubject && RequestUtils.getCurrentRequestUserInfo() == null){
                String publicKey = FileUtil.readString(this.getClass().getClassLoader().getResource(JWT_PUBLIC_KEY_PATH), JWT_STR_CHARSET);
                String jwtTokenInfoStr = JwtUtils.getInfoFromToken(currentRequestToken, RsaUtil.getRSAPublidKeyBybase64(publicKey));
                RequestUserInfo requestUserInfo = JwtUserUtils.parsejwtUserMap2RequestUserInfo(jwtTokenInfoStr);
                request.setAttribute(RequestConst.LOGIN_USER,requestUserInfo);
                JwtToken jwtToken = new JwtToken(jwtTokenInfoStr);
                SecurityUtils.getSubject().login(jwtToken);
            }
        }
        return true;
    }



}
