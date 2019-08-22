package com.robust.tools.kit.text;

import com.robust.tools.kit.base.ObjectUtil;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/22 11:40
 * @Version: 1.0
 */
public class HashUtilTest {

    @Test
    public void sha1() throws IOException {
        String s = "abc";
        System.out.println(ObjectUtil.toPrettyString(HashUtil.sha1(s)));

        assertThat(HashUtil.sha1(s)).isEqualTo(HashUtil.sha1(s.getBytes()));

        System.out.println(ObjectUtil.toPrettyString(HashUtil.sha1(s, HashUtil.generateSalt(3))));
        System.out.println(ObjectUtil.toPrettyString(HashUtil.sha1(s.getBytes(), HashUtil.generateSalt(3))));

        ByteArrayInputStream inputStream = new ByteArrayInputStream("bcd".getBytes());
        System.out.println(ObjectUtil.toPrettyString(HashUtil.sha1File(inputStream)));
    }

    @Test
    public void md5() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("bcd".getBytes());
        System.out.println(ObjectUtil.toPrettyString(HashUtil.md5File(inputStream)));
        System.out.println(EncodeUtil.encodeHex(HashUtil.md5File(inputStream)));
    }

    @Test
    public void crc() {
        String s = "abcde";
        System.out.println(HashUtil.crc32AsInt(s));
        System.out.println(HashUtil.crc32AsInt(s.getBytes()));

        System.out.println(HashUtil.crc32AsLong(s));
        System.out.println(HashUtil.crc32AsLong(s.getBytes()));
    }

    @Test
    public void murmur() {
        String s = "abcde";
        System.out.println(HashUtil.murmur32AsInt(s));
        System.out.println(HashUtil.murmur32AsInt(s.getBytes()));

        System.out.println(HashUtil.murmur128AsLong(s));
        System.out.println(HashUtil.murmur128AsLong(s.getBytes()));
    }
}