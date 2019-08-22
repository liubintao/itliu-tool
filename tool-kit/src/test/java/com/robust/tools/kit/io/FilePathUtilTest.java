package com.robust.tools.kit.io;

import com.robust.tools.kit.base.Platforms;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/7 9:33
 * @Version: 1.0
 */
public class FilePathUtilTest {

    @Test
    public void test() {
        String path = "a/../b";
        path = FilePathUtil.simplifyPath(path);
        assertThat(path).isEqualTo("b");

        path = "/home/admin/";
        path = FilePathUtil.normalizePath(path);
        assertThat(path).isEqualTo("\\home\\admin\\");

        path = "/home/admin";
        path = FilePathUtil.concat(path, new String[]{"abc", "def"});
        assertThat(path).isEqualTo("/home/admin" + Platforms.FILE_PATH_SEPARATOR_CHAR + "abc" + Platforms.FILE_PATH_SEPARATOR_CHAR + "def");

        path = "\\home\\admin";
        path = FilePathUtil.getParentPath(path);
        assertThat(path).isEqualTo("\\home\\");

        path = FilePathUtil.getJarPath(StringUtils.class);
        System.out.println(path);
    }
}