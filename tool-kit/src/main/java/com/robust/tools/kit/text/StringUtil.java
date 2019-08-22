package com.robust.tools.kit.text;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.robust.tools.kit.base.annotation.Nullable;
import com.robust.tools.kit.collection.ArrayUtil;
import com.robust.tools.kit.collection.ListUtil;
import com.robust.tools.kit.text.type.StrFormatter;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 字符串工具类
 * @Author: robust
 * @CreateDate: 2019/7/17 8:45
 * @Version: 1.0
 */
public class StringUtil {

    public static final int INDEX_NOT_FOUND = -1;

    public static final char C_SPACE = CharUtil.SPACE;
    public static final char C_TAB = CharUtil.TAB;
    public static final char C_DOT = CharUtil.DOT;
    public static final char C_CR = CharUtil.CR;
    public static final char C_LF = CharUtil.LF;
    public static final char C_UNDERLINE = CharUtil.UNDERLINE;
    public static final char C_COMMA = CharUtil.COMMA;
    public static final char C_DELIM_START = CharUtil.DELIM_START;
    public static final char C_DELIM_END = CharUtil.DELIM_END;
    public static final char C_BRACKET_START = CharUtil.BRACKET_START;
    public static final char C_BRACKET_END = CharUtil.BRACKET_END;
    public static final char C_COLON = CharUtil.COLON;

    public static final String SPACE = " ";
    public static final String TAB = "	";
    public static final String DOT = ".";
    public static final String DOUBLE_DOT = "..";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY = "";
    public static final String NULL = "null";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";
    public static final String UNDERLINE = "_";
    public static final String DASHED = "-";
    public static final String COMMA = ",";
    public static final String DELIM_START = "{";
    public static final String DELIM_END = "}";
    public static final String BRACKET_START = "[";
    public static final String BRACKET_END = "]";
    public static final String COLON = ":";

    public static final String HTML_NBSP = "&nbsp;";
    public static final String HTML_AMP = "&amp;";
    public static final String HTML_QUOTE = "&quot;";
    public static final String HTML_APOS = "&apos;";
    public static final String HTML_LT = "&lt;";
    public static final String HTML_GT = "&gt;";

    public static final String EMPTY_JSON = "{}";

    /**
     * 比较两个字符串是否相等
     *
     * @param str1
     * @param str2
     * @param ignoreCase
     * @return
     */
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (str1 == null) {
            return str2 == null;
        }
        if (str2 == null) {
            return false;
        }
        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        }
        return str1.equals(str2);
    }

    /**
     * 判断字符串是否为空字符串
     *
     * @param str 字符串
     * @return
     */
    public static boolean isBlank(CharSequence str) {
        int length;
        if (null == str || (length = str.length()) == 0) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            //只要有一个字符非空则字符串非空
            if (false == CharUtil.isBlank(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    /**
     * 字符串是否为空，空的定义如下:<br>
     * 1、为null <br>
     * 2、为""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 如果对象是字符串是否为空串空的定义如下:<br>
     * 1、为null <br>
     * 2、为""<br>
     *
     * @param obj 对象
     * @return 如果为字符串是否为空串
     * @since 3.3.0
     */
    public static boolean isEmptyIfStr(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            return 0 == ((CharSequence) obj).length();
        }
        return false;
    }

    /**
     * 字符串是否为非空白 空白的定义如下： <br>
     * 1、不为null <br>
     * 2、不为""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为非空
     */
    public static boolean isNotEmpty(CharSequence str) {
        return false == isEmpty(str);
    }

    /**
     * 当给定字符串为null时，转换为Empty
     *
     * @param str 被转换的字符串
     * @return 转换后的字符串
     */
    public static String nullToEmpty(CharSequence str) {
        return nullToDefault(str, EMPTY);
    }

    /**
     * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
     *
     * <pre>
     * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
     * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
     * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String nullToDefault(CharSequence str, String defaultStr) {
        return (str == null) ? defaultStr : str.toString();
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String utf8Str(Object obj) {
        return str(obj, StandardCharsets.UTF_8);
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj         对象
     * @param charsetName 字符集
     * @return 字符串
     */
    public static String str(Object obj, String charsetName) {
        return str(obj, Charset.forName(charsetName));
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj     对象
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return str((byte[]) obj, charset);
        } else if (obj instanceof Byte[]) {
            return str((Byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer) obj, charset);
        }

        return obj.toString();
    }

    /**
     * 将byte数组转为字符串
     *
     * @param bytes   byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(byte[] bytes, String charset) {
        return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 解码字节码
     *
     * @param data    字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * 将Byte数组转为字符串
     *
     * @param bytes   byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(Byte[] bytes, String charset) {
        return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 解码字节码
     *
     * @param data    字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(Byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        byte[] bytes = new byte[data.length];
        Byte dataByte;
        for (int i = 0; i < data.length; i++) {
            dataByte = data[i];
            bytes[i] = (null == dataByte) ? -1 : dataByte.byteValue();
        }

        return str(bytes, charset);
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(ByteBuffer data, String charset) {
        if (data == null) {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    /**
     * {@link CharSequence} 转为字符串，null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串
     */
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return
     */
    public static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    /**
     * 将字符串的首字母转大写/小写
     *
     * @param str   字符串
     * @param upper true转大写,false转小写
     * @return
     */
    public static String captureName(String str, boolean upper) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        if (upper) {
            cs[0] -= 32;
        } else {
            cs[0] += 32;
        }
        return String.valueOf(cs);
    }


    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return null;
        }
        if (ArrayUtil.isEmpty(params) || isBlank(template)) {
            return template.toString();
        }
        return StrFormatter.format(template.toString(), params);
    }

    /**
     * 高性能的Split，针对char的分隔符号，比JDK String自带的高效.
     */
    public static List<String> split(@Nullable final String str, final char separatorChar) {
        return split(str, separatorChar, 10);
    }

    /**
     * 高性能的split,针对char的分割符号,比JDK String自带的高效.
     * <p>
     * copy from Commons Lang 3.5 StringUtils, 做如下优化:
     * <p>
     * 1. 最后不做数组转换，直接返回List.
     * 2. 可设定List初始大小.
     * 3. preserveAllTokens 取默认值false
     *
     * @param expectParts 预估分割后的List大小，初始化数据更精准
     * @return 如果为null返回null, 如果为""返回空数组
     */
    public static List<String> split(@Nullable final String str, final char separatorChar, int expectParts) {
        if (str == null) {
            return null;
        }

        final int len = str.length();
        if (len == 0) {
            return ListUtil.emptyList();
        }

        final List<String> list = new ArrayList<>(expectParts);
        int i = 0;
        int start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }

        if (match) {
            list.add(str.substring(start, i));
        }
        return list;
    }

    /*-------------------char操作相关------------------------------*/

    /**
     * 使用多个可选的char作为分割符,还可以设置omitEmptyStrings,trimResults等配置
     * 设置后的Splitter进行重用，不要每次创建
     *
     * @param separatorChars 比如Unix/Windows的路径分割符 "/\\"
     * @see Splitter#on(char)
     */
    public static Splitter charsSplitter(final String separatorChars) {
        return Splitter.on(CharMatcher.anyOf(separatorChars));
    }

    /**
     * String有replace(char,char)替换全部char,但缺少单独replace first/last
     */
    public static String replaceFirst(@Nullable String s, char sub, char with) {
        if (isBlank(s)) {
            return null;
        }

        int idx = s.indexOf(sub);
        if (idx == -1) {
            return s;
        }

        char[] str = s.toCharArray();
        str[idx] = with;
        return new String(str);
    }

    /**
     * String有replace(char,char)替换全部char,但缺少单独replace first/last
     */
    public static String replaceLast(@Nullable String s, char sub, char with) {
        if (isBlank(s)) {
            return null;
        }

        int idx = s.lastIndexOf(sub);
        if (idx == -1) {
            return s;
        }

        char[] str = s.toCharArray();
        str[idx] = with;
        return new String(str);
    }

    /**
     * 判断字符串是否以某个字符开头,如果字符串为null或空,返回false
     *
     * @param s 字符串
     * @param c 字符
     * @return true or false
     */
    public static boolean startWith(@Nullable CharSequence s, char c) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        return s.charAt(0) == c;
    }

    /**
     * 判断字符串是否以某个字符结尾,如果字符串为null或空,返回false
     *
     * @param s 字符串
     * @param c 字符
     * @return true or false
     */
    public static boolean endWith(@Nullable CharSequence s, char c) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        return s.charAt(s.length() - 1) == c;
    }

    /**
     * 如果结尾是字符c, 则移除字符c
     *
     * @param s 待处理字符串
     * @param c 待移除字符
     * @return String
     */
    public static String removeEnd(final String s, char c) {
        if (endWith(s, c)) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }
}
