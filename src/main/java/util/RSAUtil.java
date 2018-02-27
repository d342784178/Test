package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * Created by Administrator on 2016/8/8.
 */
public class RSAUtil {
    private static final String SIGN_ALGORITHMS = "SHA1withRSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 计算二进制数据的SHA1withRSA签名。
     * @param content         待签名的数据。
     * @param privateKeyPKCS8 PKCS格式的RSA私钥。
     * @return
     */
    public static byte[] signWithSha1(byte[] content, String privateKeyPKCS8) {
        Validate.notNull(content);
        Validate.notNull(privateKeyPKCS8);

        Signature sin;
        try {
            sin = Signature.getInstance(SIGN_ALGORITHMS);
            sin.initSign(decodePrivateKey(privateKeyPKCS8));
            sin.update(content);
            return sin.sign();
        } catch (Exception e) {
            throw new RuntimeException("计算SHA1withRSA签名失败。", e);
        }
    }

    /**
     * 校验二进制数据的SHA1withRSA签名。
     * @param content      待校验的数据。
     * @param sign         数据签名。
     * @param publicKeyPEM PEM格式的RSA公钥。
     * @return
     */
    public static boolean verfiyWithSha1(byte[] content, byte[] sign, String publicKeyPEM) {
        Validate.notNull(content);
        Validate.notNull(sign);
        Validate.notNull(publicKeyPEM);

        Signature sin;
        try {
            sin = Signature.getInstance(SIGN_ALGORITHMS);
            sin.initVerify(decodePublicKey(publicKeyPEM));
            sin.update(content);
            return sin.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException("校验SHA1withRSA签名失败。", e);
        }
    }


    /**
     * <P>
     * 私钥解密
     * </p>
     * @param encryptedData 已加密数据
     * @param privateKeyStr
     * @return
     * @exception Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKeyStr)
            throws Exception {
        KeyFactory          keyFactory   = KeyFactory.getInstance("RSA");
        PrivateKey          privateKey   = decodePrivateKey(privateKeyStr);
        Cipher              cipher       = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int                   inputLen = encryptedData.length;
        ByteArrayOutputStream out      = new ByteArrayOutputStream();
        int                   offSet   = 0;
        byte[]                cache;
        int                   i        = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     * @param content 已加密数据
     * @param publicKeyStr
     * @return
     * @exception Exception
     */
    public static byte[] decryptByPublicKey(byte[] content, String publicKeyStr)
            throws Exception {
        KeyFactory         keyFactory  = KeyFactory.getInstance("RSA");
        PublicKey          publicKey   = decodePublicKey(publicKeyStr);
        Cipher             cipher      = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        int                   inputLen = content.length;
        ByteArrayOutputStream out      = new ByteArrayOutputStream();
        int                   offSet   = 0;
        byte[]                cache;
        int                   i        = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(content, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(content, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     * @param content         源数据
     * @param publicKeyStr 公钥(BASE64编码)
     * @return
     * @exception Exception
     */
    public static byte[] encryptByPublicKey(byte[] content, String publicKeyStr)
            throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey  publicKey  = decodePublicKey(publicKeyStr);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int                   inputLen = content.length;
        ByteArrayOutputStream out      = new ByteArrayOutputStream();
        int                   offSet   = 0;
        byte[]                cache;
        int                   i        = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(content, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(content, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     * @param data       源数据
     * @param privateKeyStr
     * @return
     * @exception Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKeyStr)
            throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = decodePrivateKey(privateKeyStr);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        int                   inputLen = data.length;
        ByteArrayOutputStream out      = new ByteArrayOutputStream();
        int                   offSet   = 0;
        byte[]                cache;
        int                   i        = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 解码PKCS8格式的秘钥。
     */
    static PrivateKey decodePrivateKey(String privateKeyPKCS8) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        privateKeyPKCS8 = StringUtils.remove(privateKeyPKCS8, "-----BEGIN PRIVATE KEY-----\n");
        privateKeyPKCS8 = StringUtils.remove(privateKeyPKCS8, "-----END PRIVATE KEY-----");
        byte[]              decoded = Base64Utils.decryptBASE64(privateKeyPKCS8);
        PKCS8EncodedKeySpec spec    = new PKCS8EncodedKeySpec(decoded);
        return kf.generatePrivate(spec);
    }

    /**
     * 解码PEM格式的公钥。
     */
    static PublicKey decodePublicKey(String publiKeyPEM) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        publiKeyPEM = StringUtils.remove(publiKeyPEM, "-----BEGIN PUBLIC KEY-----\n");
        publiKeyPEM = StringUtils.remove(publiKeyPEM, "-----END PUBLIC KEY-----");
        byte[]             decoded = Base64Utils.decryptBASE64(publiKeyPEM);
        X509EncodedKeySpec spec    = new X509EncodedKeySpec(decoded);
        return kf.generatePublic(spec);
    }


}
