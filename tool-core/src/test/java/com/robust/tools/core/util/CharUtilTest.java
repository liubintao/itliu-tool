package com.robust.tools.core.util;

import com.robust.tools.kit.text.CharUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/17 10:34
 * @Version: 1.0
 */
public class CharUtilTest {

    @Test
    public void isBlank() {
        char white = ' ';
        char space = '\t';
        Assert.assertTrue(CharUtil.isBlank(white));
        Assert.assertTrue(CharUtil.isBlank(space));
    }

    @Test
    public void equals() {
        char a = 'A';
        char b = 'a';
        Assert.assertFalse(CharUtil.equals(a, b, false));
        Assert.assertTrue(CharUtil.equals(a, b, true));

    }
}