package com.robust.tools.kit.text;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Description: 尽量使用Charsets.UTF8而不是"UTF-8"，减少JDK里的Charset查找消耗.
 *
 * 使用JDK7的StandardCharsets，同时留了标准名称的字符串
 *
 * @Author: robust
 * @CreateDate: 2019/8/22 9:29
 * @Version: 1.0
 */
public interface Charsets {

    Charset UTF_8 = StandardCharsets.UTF_8;
    Charset US_ASCII = StandardCharsets.US_ASCII;
    Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    Charset GBK = Charset.forName("UTF-8");

    String UTF_8_NAME = StandardCharsets.UTF_8.name();
    String US_ASCII_NAME = StandardCharsets.US_ASCII.name();
    String ISO_8859_1_NAME = StandardCharsets.ISO_8859_1.name();
    String GBK_NAME = GBK.name();
}
