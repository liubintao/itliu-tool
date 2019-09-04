package com.robust.tools.kit.io;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/4 14:30
 * @Version: 1.0
 */
public class IOUtilTest {

    @Test
    public void compress() {
        String s = "abcde";
        byte[] bytes = IOUtil.compress(s.getBytes());
        byte[] srcBytes = IOUtil.decompress(bytes);

        assertThat(new String(bytes).equals(s)).isFalse();
        assertThat(new String(srcBytes)).isEqualTo(s);
//        System.out.println(new String(srcBytes));
    }
}