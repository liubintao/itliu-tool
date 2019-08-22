package com.robust.tools.kit.base;

import com.robust.tools.kit.io.URLResourceUtil;
import com.robust.tools.kit.number.NumberUtil;
import com.robust.tools.kit.text.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

/**
 * @Description: 关于Properties的工具类
 * <p>
 * 1、统一风格读取properties到各种数据类型
 * 2、从文件或字符串装载properties
 * @Author: robust
 * @CreateDate: 2019/7/23 8:07
 * @Version: 1.0
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    /*-----------------------读取properties-------------------------------------*/

    private static Boolean getBoolean(Properties p, String name, Boolean defaultValue) {
        return BooleanUtil.toBooleanObject(p.getProperty(name), defaultValue);
    }

    public static Integer getInt(Properties p, String name, Integer defaultValue) {
        return NumberUtil.toIntObject(p.getProperty(name), defaultValue);
    }

    public static Long getLong(Properties p, String name, Long defaultValue) {
        return NumberUtil.toLongObject(p.getProperty(name), defaultValue);
    }

    public static Double getDouble(Properties p, String name, Double defaultValue) {
        return NumberUtil.toDoubleObject(p.getProperty(name), defaultValue);
    }

    public static String getString(Properties p, String name, String defaultValue) {
        return p.getProperty(name, defaultValue);
    }

    /*---------------------加载properties------------------------------------------*/

    /**
     * 从文件路径加载properties，默认使用utf-8编码解析文件
     * 路径支持从外部文件或resources文件加载，"file://"或无前缀代表外部文件,"classpath:"代表resources
     *
     * @param path
     * @return
     */
    public static Properties loadFromFile(String path) {
        Properties p = new Properties();
        try (Reader reader = new InputStreamReader(URLResourceUtil.asStream(path), Charsets.UTF_8)) {
            p.load(reader);
        } catch (IOException e) {
            logger.warn("load property from " + path + " failed", e);
        }
        return p;
    }

    /**
     * 从字符串加载进properties
     *
     * @param content
     * @return
     */
    public static Properties loadFromString(String content) {
        Properties p = new Properties();
        try (Reader reader = new StringReader(content)) {
            p.load(reader);
        } catch (IOException e) {
            //ignored
        }
        return p;
    }
}
