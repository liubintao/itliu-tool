package com.robust.tools.kit.text;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/22 9:46
 * @Version: 1.0
 */
public class EncodeUtilTest {

    @Test
    public void encodeHex() {
        String s = "abc";
        System.out.println(EncodeUtil.encodeHex(s.getBytes()));

        s = "13091319881121405X";
        System.out.println(EncodeUtil.encodeHex(s.getBytes()));
    }

    @Test
    public void decodeHex() {
        String s = "abc";
        System.out.println(new String(EncodeUtil.decodeHex(EncodeUtil.encodeHex(s.getBytes()))));

        s = "13091319881121405X";
        System.out.println(new String(EncodeUtil.decodeHex(EncodeUtil.encodeHex(s.getBytes()))));
    }

    @Test
    public void testBase64() {
        String s = "abc";
        String encodeStr = EncodeUtil.encodeBase64(s.getBytes());
        System.out.println(encodeStr);
        System.out.println(new String(EncodeUtil.decodeBase64(encodeStr)));

        s = "13091319881121405X";
        encodeStr = EncodeUtil.encodeBase64(s.getBytes());
        System.out.println(encodeStr);
        System.out.println(new String(EncodeUtil.decodeBase64(encodeStr)));
    }

    @Test
    public void testBase64Url() {
        String url = "https://mbd.baidu.com/newspage/data/landingsuper?context=%7B%22nid%22%3A%22news_9217466555451144538%22%7D&n_type=0&p_from=1";
        String result = EncodeUtil.encodeBase64UrlSafe(url.getBytes());
        System.out.println(result);

        byte[] decodeResult = EncodeUtil.decodeBase64UrlSafe(result);
        assertThat(new String(decodeResult)).isEqualTo(url);
    }
}