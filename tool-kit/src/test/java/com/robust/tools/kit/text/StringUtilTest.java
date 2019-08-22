package com.robust.tools.kit.text;

import org.junit.Test;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/23 10:41
 * @Version: 1.0
 */
public class StringUtilTest {

    @Test
    public void decode() {
        String s = "56,55,57,54,57,54,57,54,88,21016";
        String[] ss = s.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ss.length; i++) {
            char c = (char) Integer.parseInt(ss[i]);
            sb.append(c);

        }
        System.out.println(sb);
    }

    @Test
    public void captureName() {
        String s = "abc";
        System.out.println(StringUtil.captureName(s));
        System.out.println(StringUtil.captureName(StringUtil.captureName(s), false));
    }
}