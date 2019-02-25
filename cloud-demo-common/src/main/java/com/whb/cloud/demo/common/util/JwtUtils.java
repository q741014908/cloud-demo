package com.whb.cloud.demo.common.util;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtUtils {
    /**
     * 私钥加密token
     *
     * @param userId、userName      载荷中的数据
     * @param privateKey    私钥
     * @param expireMinutes 过期时间，单位秒
     * @return
     * @throws Exception
     */
    public static String generateToken(Integer userId, String userName, PrivateKey privateKey, int expireMinutes) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("userName", userName)
                .setExpiration(DateUtil.offsetMinute(DateTime.now(),expireMinutes))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 私钥加密token
     *
     * @param userId、userName      载荷中的数据
     * @param privateKey    私钥字节数组
     * @param expireMinutes 过期时间，单位秒
     * @return
     * @throws Exception
     */
    public static String generateToken(Integer userId, String userName, byte[] privateKey, int expireMinutes) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("userName", userName)
                .setExpiration(DateUtil.offsetMinute(DateTime.now(),expireMinutes))
                .signWith(SignatureAlgorithm.RS256, RsaUtil.getRSAPrivateKeyBybase64(Base64Encoder.encode(privateKey)))
                .compact();
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥字节数组
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, byte[] publicKey) {
        return Jwts.parser().setSigningKey(RsaUtil.getRSAPublidKeyBybase64(Base64Encoder.encode(publicKey)))
                .parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static String getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return body+"";
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static String getInfoFromToken(String token, byte[] publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return body+"";
    }
}
