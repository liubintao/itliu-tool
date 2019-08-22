package com.robust.tools.core.util;

import org.junit.Test;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/18 17:12
 * @Version: 1.0
 */
public class EnumUtilsTest {

    @Test
    public void getEnum() {
        System.out.println(EnumUtil.getEnum(Cert.class, "SFZ"));
    }

    enum Cert{
        SFZ,HKB;
        private String code;
        private String desc;
    }
}