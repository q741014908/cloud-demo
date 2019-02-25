package com.whb.cloud.demo.common.security.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * shiro登录用到的jwt token
 */
public class JwtToken implements AuthenticationToken {

    private String jwtToken;

    public JwtToken(String jwtToken){
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return jwtToken;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }
}
