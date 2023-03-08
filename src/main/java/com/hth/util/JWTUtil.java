package com.hth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * JWT工具类，用来生成token、加密token、解密token
 */
public class JWTUtil {
    // 过期时间 7 天  604800
    private static final long EXPIRE_TIME =604800;
    // 密钥wsrgh
    private static final String SECRET = "hrty4da8s64d8r4h896rej";

    /**
     * 生成 token
     */
    public static String createToken(Integer id) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME*1000);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            // 附带id信息
            return JWT.create()
                    .withClaim("id", id)
                    //到期时间
                    .withExpiresAt(date)
                    //创建一个新的JWT，并使用给定的算法进行标记
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 校验 token 是否正确
     */
    public static boolean verify(String token,Integer id) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //在token中附带了id信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("id",id)
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     */
    public static Integer getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }





}
