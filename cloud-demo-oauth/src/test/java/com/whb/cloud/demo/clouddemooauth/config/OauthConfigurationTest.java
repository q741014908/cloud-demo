package com.whb.cloud.demo.clouddemooauth.config;

import cn.hutool.core.io.FileUtil;
import com.whb.cloud.demo.clouddemooauth.CloudDemoOauthApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;

@SpringBootTest(classes = CloudDemoOauthApplication.class)
@RunWith(SpringRunner.class)
public class OauthConfigurationTest {

    @Autowired
    private KeyProperties keyProperties;

    @Test
    public void createPublickKey(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(keyProperties.getKeyStore().getLocation(),
                keyProperties.getKeyStore().getPassword().toCharArray()).getKeyPair(
                keyProperties.getKeyStore().getAlias(),
                keyProperties.getKeyStore().getSecret().toCharArray());
        jwtAccessTokenConverter.setKeyPair(keyPair);
        String txt = "-----BEGIN PUBLIC KEY-----\n" + new String(Base64.encode(keyPair.getPublic().getEncoded()))
                + "\n-----END PUBLIC KEY-----";
        FileUtil.writeString(txt, "/publicKey.txt", "UTF-8");
    }
}