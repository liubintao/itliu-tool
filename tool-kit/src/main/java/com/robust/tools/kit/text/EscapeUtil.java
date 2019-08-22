package com.robust.tools.kit.text;

import org.apache.commons.text.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Description: 转义工具集
 * <p>
 * 1、URL 转义，转义后的URL可作为URL中的参数 (via JDK)
 * 2、xml/html 转义(via Commons-Text StringEscapeUtils）
 * 比如 "a" & "b" 转化为 &quot;a&quot; &amp; &quot;b&quot;
 * @Author: robust
 * @CreateDate: 2019/8/22 10:18
 * @Version: 1.0
 */
public class EscapeUtil {

    /**
     * URL 编码, Encode默认为UTF-8.
     * <p>
     * 转义后的URL可作为URL中的参数
     */
    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, Charsets.UTF_8_NAME);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * URL 解码, Encode默认为UTF-8. 转义后的URL可作为URL中的参数
     */
    public static String urlDecode(String part) {
        try {
            return URLDecoder.decode(part, Charsets.UTF_8_NAME);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Xml转码，将字符串转码为符合XML1.1格式的字符串.
     * <p>
     * 比如 "a" & "b" 转化为 &quot;a&quot; &amp; &quot;b&quot;
     */
    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml11(xml);
    }

    /**
     * Xml转码，XML格式的字符串解码为普通字符串.
     * <p>
     * 比如 &quot;a&quot; &amp; &quot;b&quot; 转化为 "a" & "b"
     */
    public static String unescapeXml(String xml) {
        return StringEscapeUtils.unescapeXml(xml);
    }

    /**
     * Html转码，将字符串转码为符合HTML4格式的字符串.
     * <p>
     * 比如 "a" & "b" 转化为 &quot;a&quot; &amp; &quot;b&quot;
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * Html解码，将HTML4格式的字符串转码解码为普通字符串.
     * <p>
     * 比如 &quot;a&quot; &amp; &quot;b&quot; 转化为 "a" & "b"
     */
    public static String unescapeHtml(String html) {
        return StringEscapeUtils.unescapeHtml4(html);
    }
}
