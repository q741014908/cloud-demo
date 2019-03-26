package com.newnil.cas.oauth2.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * 添加注销端点
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {

    @Autowired
    private ConsumerTokenServices tokenServices;

    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
    @ResponseBody
    public String revokeToken(Principal principal,@RequestParam String token) {
        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }
        if (validateUserIsMyself(principal,token) && tokenServices.revokeToken(token)){
            return "注销成功";
        }else{
            return "注销失败";
        }
    }

    private boolean validateUserIsMyself(Principal principal,String token){
        DefaultTokenServices defaultTokenServices = (DefaultTokenServices) tokenServices;
        String clientId = defaultTokenServices.getClientId(token);
        return clientId!=null && clientId.equals(principal.getName());
    }
}
