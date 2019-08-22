package com.robust.tools.kit.security;

import com.robust.tools.kit.base.ExceptionUtil;
import com.robust.tools.kit.number.RandomUtil;
import com.robust.tools.kit.text.Charsets;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @Description: 支持HMAC-SHA1消息签名及DES/AES对称加密的工具类
 * <p>
 * 至此和HEX和Base64两种编码方式.
 * @Author: robust
 * @CreateDate: 2019/8/21 16:55
 * @Version: 1.0
 */
public class CryptoUtil {

    private static final String AES_ALG = "AES";
    private static final String AES_CBC_ALG = "AES/CBC/PKCS5Padding";
    private static final String HMACSHA1_ALG = "HmacSHA1";

    private static final int DEFAULT_HMACSHA1_KEYSIZE = 160; //RFC2401
    private static final int DEFAULT_AES_KEYSIZE = 128;
    private static final int DEFAULT_IVSIZE = 16;

    private static SecureRandom random = RandomUtil.secureRandom();

    /*-------------------HMAC-SHA1 function------------------------*/

    /**
     * 使用HMAC-SHA1进行消息签名,返回字节数组,长度为20字节.
     *
     * @param input 字符数组
     * @param key   HMAC-SHA1签名密钥
     * @return 签名后的字节数组
     */
    public static byte[] hmacSha1(byte[] input, byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, HMACSHA1_ALG);
        Mac mac;
        try {
            mac = Mac.getInstance(HMACSHA1_ALG);
            mac.init(secretKey);
        } catch (GeneralSecurityException e) {
            throw ExceptionUtil.unchecked(e);
        }
        return mac.doFinal(input);
    }

    /**
     * 校验HMAC-SHA1签名是否正确
     *
     * @param expected 已存在的签名
     * @param input    原始输入字符数组
     * @param key      签名密钥
     * @return true/false
     */
    public static boolean isMacValid(byte[] expected, byte[] input, byte[] key) {
        byte[] actual = hmacSha1(input, key);
        return Arrays.equals(expected, actual);
    }

    /**
     * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节). HMAC-SHA1算法对密钥无特殊要求, RFC2401建议
     * 最少长度为160位(20字节).
     *
     * @return 密钥
     */
    public static byte[] generateHmacSha1Key() {
        return generateKey(HMACSHA1_ALG, DEFAULT_HMACSHA1_KEYSIZE);
    }

    /*------------------------AES function----------------------------------*/

    /**
     * 使用AES加密字符串.
     *
     * @param input 原始输入字符数组
     * @param key   符合AES要求的密钥
     */
    public static byte[] aesEncrypt(byte[] input, byte[] key) {
        return aes(input, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 使用AES解密字符串.
     *
     * @param input 原始输入字符数组
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     */
    public static byte[] aesEncrypt(byte[] input, byte[] key, byte[] iv) {
        return aes(input, key, iv, Cipher.ENCRYPT_MODE);
    }

    /**
     * 使用AES解密字符串, 返回原始字符串
     *
     * @param input HEX编码的加密字符串
     * @param key   符合AES要求的密钥
     */
    public static String aesDecrypt(byte[] input, byte[] key) {
        byte[] decryptResult = aes(input, key, Cipher.DECRYPT_MODE);
        return new String(decryptResult, Charsets.UTF_8);
    }

    /**
     * 使用AES解密字符串, 返回原始字符串
     *
     * @param input HEX编码的加密字符串
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     */
    public static String aesDecrypt(byte[] input, byte[] key, byte[] iv) {
        byte[] decryptResult = aes(input, key, iv, Cipher.DECRYPT_MODE);
        return new String(decryptResult, Charsets.UTF_8);
    }

    /**
     * 使用AES加密/解密无编码的原始字符数组, 返回无编码的字节数组.
     *
     * @param input 原始字符数组
     * @param key   符合AES要求的密钥
     * @param mode  {@link javax.crypto.Cipher#ENCRYPT_MODE} / {@link javax.crypto.Cipher#DECRYPT_MODE}
     * @return
     */
    private static byte[] aes(byte[] input, byte[] key, int mode) {
        SecretKey secretKey = new SecretKeySpec(key, AES_ALG);
        try {
            Cipher cipher = Cipher.getInstance(AES_ALG);
            cipher.init(mode, secretKey);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    /**
     * 使用AES加密/解密无编码的原始字符数组, 返回无编码的字节数组.
     *
     * @param input 原始字符数组
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     * @param mode  {@link javax.crypto.Cipher#ENCRYPT_MODE} / {@link javax.crypto.Cipher#DECRYPT_MODE}
     * @return
     */
    private static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) {
        SecretKey secretKey = new SecretKeySpec(key, AES_ALG);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_ALG);
            cipher.init(mode, secretKey, ivParameterSpec);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    /**
     * 生成AES密钥, 可选长度为128, 192, 256位.
     */
    public static byte[] generateAESKey() {
        return generateAESKey(DEFAULT_AES_KEYSIZE);
    }

    /**
     * 生成AES密钥, 可选长度为128, 192, 256位.
     *
     * @param keySize 密钥长度
     * @return 密钥数组
     */
    public static byte[] generateAESKey(int keySize) {
        return generateKey(AES_ALG, keySize);
    }

    /**
     * 生成随机向量, 默认大小为cipher.getBlockSize, 16字节.
     */
    public static byte[] generateIV() {
        byte[] bytes = new byte[DEFAULT_IVSIZE];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 根据算法和密钥长度生成密钥数组
     *
     * @param algorithm 算法
     * @param keySize   密钥长度
     * @return 密钥数组
     */
    public static byte[] generateKey(String algorithm, int keySize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(keySize);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }
}
