package com.robust.tools.kit.base;

import com.robust.tools.kit.text.Charsets;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 关于系统设定，平台信息的变量(via Common Lang SystemUtils)
 */
public class Platforms {

    // 文件路径分隔符
    String FILE_PATH_SEPARATOR = File.separator;
    char FILE_PATH_SEPARATOR_CHAR = File.separatorChar;
    char WINDOWS_FILE_PATH_SEPARATOR_CHAR = '\\';
    char LINUX_FILE_PATH_SEPARATOR_CHAR = '/';

    // ClassPath分隔符
    String CLASS_PATH_SEPARATOR = File.pathSeparator;
    char CLASS_PATH_SEPARATOR_CHAR = File.pathSeparatorChar;

    // 换行符
    String LINE_SEPARATOR = System.lineSeparator();

    // 临时目录
    String TMP_DIR = SystemUtils.JAVA_IO_TMPDIR;
    // 应用的工作目录
    String WORKING_DIR = SystemUtils.USER_DIR;
    // 用户 HOME目录
    String USER_HOME = SystemUtils.USER_HOME;
    // Java HOME目录
    String JAVA_HOME = SystemUtils.JAVA_HOME;

    // Java版本
    String JAVA_SPECIFICATION_VERSION = SystemUtils.JAVA_SPECIFICATION_VERSION; // e.g. 1.8
    String JAVA_VERSION = SystemUtils.JAVA_VERSION; // e.g. 1.8.0_102
    boolean IS_JAVA7 = SystemUtils.IS_JAVA_1_7;
    boolean IS_JAVA8 = SystemUtils.IS_JAVA_1_8;
    boolean IS_ATLEASET_JAVA7 = IS_JAVA7 || IS_JAVA8;
    boolean IS_ATLEASET_JAVA8 = IS_JAVA8;

    // 操作系统类型及版本
    String OS_NAME = SystemUtils.OS_NAME;
    String OS_VERSION = SystemUtils.OS_VERSION;
    String OS_ARCH = SystemUtils.OS_ARCH; // e.g. x86_64
    boolean IS_LINUX = SystemUtils.IS_OS_LINUX;
    boolean IS_UNIX = SystemUtils.IS_OS_UNIX;
    boolean IS_WINDOWS = SystemUtils.IS_OS_WINDOWS;
    Charset DEFAULT_CHARSET = IS_WINDOWS ? Charsets.GBK : Charset.defaultCharset();
}
