package util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * 项目：fs-fubei-shop
 * 包名：com.fshows.fubei.shop.api.jwt
 * 功能：jwt帮助类
 * 时间：2016-08-29
 * 作者：呱牛
 */
public class JWTHelper {

    /**
     * 解析token
     *
     * @param jWTString      token
     * @param base64Security key
     * @return
     */
    public static Claims parseJWT(String jWTString, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jWTString).getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public static JwtClaims parseToken(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(JWTConstant.BASE64))
                    .parseClaimsJws(token).getBody();
            if (claims == null) {
                return null;
            }
            JwtClaims jwtClaims = new JwtClaims();
            jwtClaims.setUsername((String) claims.get("username"));
            jwtClaims.setUserId(Integer.valueOf(claims.get("userId").toString()));
            jwtClaims.setPassword((String) claims.get("password"));
            jwtClaims.setUserIdStr(claims.get("userId").toString());
            return jwtClaims;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 生成token
     *
     * @return
     */
    public static String createJWT(String username, String password, long uid, String audience, Date nowDate, Date expiry, String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("userId", uid)
                .claim("userName", username)
                .claim("password", password)
                .setIssuer(JWTConstant.ISSUER)//该JWT的签发者
                .setIssuedAt(nowDate)//签发时间
                .setSubject(username)//该JWT所面向的用户  username
                .setAudience(audience)//接收该JWT的一方
                .setId(JWTConstant.ID)//
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (expiry != null) {
            builder.setExpiration(expiry)//过期时间
                    .setNotBefore(nowDate);//开始时间
        }

        //生成JWT
//        return builder;
        return builder.compact();
    }

    /**
     * 检查token
     *
     * @param token token
     * @return
     */
    public static Claims checkLoginToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //现在时间
        long nowTime = System.currentTimeMillis();

        //token实体
        Claims claims = JWTHelper.parseJWT(token, JWTConstant.BASE64);
        if (claims != null
                        && JWTConstant.ADMIN_AUDIENCE.equals(claims.getAudience())
                        && StringUtils.isNotBlank(claims.get("agency_id").toString())
                && nowTime >= claims.getNotBefore().getTime()
                && nowTime < claims.getExpiration().getTime()
                && JWTConstant.ISSUER.equals(claims.getIssuer())
                && JWTConstant.ID.equals(claims.getId())) {
            return claims;
        }

        return null;
    }
}
