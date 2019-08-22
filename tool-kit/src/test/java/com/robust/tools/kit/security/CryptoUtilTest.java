package com.robust.tools.kit.security;

import com.robust.tools.kit.base.ObjectUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/21 18:14
 * @Version: 1.0
 */
public class CryptoUtilTest {

    @Test
    public void mac() {
        byte[] key = CryptoUtil.generateHmacSha1Key();
        assertThat(key).hasSize(20);

        String s = "abc";
        byte[] result = CryptoUtil.hmacSha1(s.getBytes(), key);
        assertThat(CryptoUtil.isMacValid(result, s.getBytes(), key)).isTrue();
    }

    @Test
    public void aes() {
        byte[] aesKey = CryptoUtil.generateAESKey();
        System.out.println(ObjectUtil.toPrettyString(aesKey));

        String s = "abcde";
        byte[] encryptStr = CryptoUtil.aesEncrypt(s.getBytes(), aesKey);
        System.out.println(encryptStr);

        String decryptStr = CryptoUtil.aesDecrypt(encryptStr, aesKey);
        System.out.println(decryptStr);

        byte[] iv = CryptoUtil.generateIV();
        encryptStr = CryptoUtil.aesEncrypt(s.getBytes(), aesKey, iv);
        System.out.println(encryptStr);

        decryptStr = CryptoUtil.aesDecrypt(encryptStr, aesKey, iv);
        System.out.println(decryptStr);
    }
}