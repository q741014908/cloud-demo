package com.whb.cloud.demo.clouddemooauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
@EnableResourceServer
public class OauthConfiguration {


    @Bean
    public AccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("mytest.jks"),
                "mypass".toCharArray()).getKeyPair(
                "mytest",
                "mypass".toCharArray());
        jwtAccessTokenConverter.setKeyPair(keyPair);
        /*String txt = "-----BEGIN PUBLIC KEY-----\n" + new String(Base64.encode(keyPair.getPublic().getEncoded()))
                + "\n-----END PUBLIC KEY-----";*/
        return jwtAccessTokenConverter;
    }

}
