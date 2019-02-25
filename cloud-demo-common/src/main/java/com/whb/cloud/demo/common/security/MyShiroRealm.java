package com.whb.cloud.demo.common.security;

import com.whb.cloud.demo.common.model.RequestUserInfo;
import com.whb.cloud.demo.common.security.jwt.JwtToken;
import com.whb.cloud.demo.common.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    private static final String STR_ROLES_REGEXP = ",\\s{0,1}";

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        RequestUserInfo currentRequestUserInfo = RequestUtils.getCurrentRequestUserInfo();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if(currentRequestUserInfo == null || currentRequestUserInfo.getRols()==null){
            return authorizationInfo;
        }
        authorizationInfo.addRoles(Arrays.asList(currentRequestUserInfo.getRols().split(STR_ROLES_REGEXP)));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        RequestUserInfo currentRequestUserInfo = RequestUtils.getCurrentRequestUserInfo();
        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(currentRequestUserInfo.getName(), authenticationToken.getPrincipal(), null, super.getName());
        return authenticationInfo;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
}
