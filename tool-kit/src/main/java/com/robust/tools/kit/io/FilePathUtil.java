package com.robust.tools.kit.io;

import com.google.common.io.Files;
import com.robust.tools.kit.base.Platforms;
import com.robust.tools.kit.text.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 关于文件路径的工具类. 这个类只适合处理纯字符串的路径, 如果是File/Path对象的路径处理,建议直接使用Path类的方法.
 * @Author: robust
 * @CreateDate: 2019/8/6 20:30
 * @Version: 1.0
 */
public class FilePathUtil {

    /**
     * 在Windows环境里,兼容Windows上的路径分割符,将'/'转回'\'
     *
     * @param path
     * @return
     */
    public static String normalizePath(String path) {
        if (Platforms.FILE_PATH_SEPARATOR_CHAR == Platforms.WINDOWS_FILE_PATH_SEPARATOR_CHAR
                && StringUtils.indexOf(path, Platforms.LINUX_FILE_PATH_SEPARATOR_CHAR) != -1) {
            return StringUtils.replaceChars(path,
                    Platforms.LINUX_FILE_PATH_SEPARATOR_CHAR, Platforms.WINDOWS_FILE_PATH_SEPARATOR_CHAR);
        }
        return path;
    }

    /**
     * 将路径整理,如{@code a/../b}整理成{@code b}
     *
     * @param path
     * @return
     */
    public static String simplifyPath(String path) {
        return Files.simplifyPath(path);
    }

    /**
     * 拼接多个路径
     *
     * @param baseName   基础路径名称
     * @param appendName 待拼接的路径名称数组
     * @return 拼接后的路径
     */
    public static String concat(String baseName, String... appendName) {
        if (appendName.length == 0) {
            return baseName;
        }
        StringBuilder concatName = new StringBuilder(baseName);
        if (StringUtil.endWith(baseName, Platforms.FILE_PATH_SEPARATOR_CHAR)) {
            concatName.append(appendName[0]);
        } else {
            concatName.append(Platforms.FILE_PATH_SEPARATOR_CHAR).append(appendName[0]);
        }
        if (appendName.length > 1) {
            for (int i = 1; i < appendName.length; i++) {
                concatName.append(Platforms.FILE_PATH_SEPARATOR_CHAR).append(appendName[i]);
            }
        }
        return concatName.toString();
    }

    /**
     * 获得上层目录的路径
     */
    public static String getParentPath(final String path) {
        String parentPath = path;
        if (Platforms.FILE_PATH_SEPARATOR.equals(parentPath)) {
            return parentPath;
        }

        parentPath = StringUtil.removeEnd(parentPath, Platforms.FILE_PATH_SEPARATOR_CHAR);

        int idx = parentPath.lastIndexOf(Platforms.FILE_PATH_SEPARATOR_CHAR);
        if (idx > 0) {
            parentPath = parentPath.substring(0, idx + 1);
        } else {
            parentPath = Platforms.FILE_PATH_SEPARATOR;
        }
        return parentPath;
    }

    /**
     * 获得clz所在的Jar文件的绝对路径
     */
    public static String getJarPath(Class<?> clz) {
        return clz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }
}
