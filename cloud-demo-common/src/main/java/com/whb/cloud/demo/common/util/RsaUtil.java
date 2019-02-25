package com.whb.cloud.demo.common.util;

import cn.hutool.core.codec.Base64Decoder;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.consumer.InvalidJwtException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class RsaUtil {
    public static RSAPublicKey getRSAPublidKeyBybase64(String base64s) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Decoder.decode(base64s));
        RSAPublicKey publicKey = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException var4) {
            log.error("base64编码=" + base64s + "转RSA公钥失败", var4);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    public static RSAPrivateKey getRSAPrivateKeyBybase64(String base64s) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64Decoder.decode(base64s));
        RSAPrivateKey privateKey = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException var4) {
            log.error("base64编码=" + base64s + "转RSA私钥失败", var4);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return privateKey;
    }

}
