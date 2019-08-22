package com.robust.tools.kit.io;

import com.google.common.io.Resources;
import com.robust.tools.kit.text.Charsets;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @Description: 针对Jar包内文件的工具类
 * <p>
 * 1、Classloader
 * 不指定ContextClass时，优先使用{@link Thread#getContextClassLoader()}，如果contextClassloader未设置，
 * 则使用guava {@link com.google.common.io.Resources} 类的ContextLoader
 * <p>
 * 指定ContextClass时，则使用该ContextClass的ClassLoader
 * <p>
 * 2、路径
 * 不指定ContextClass时，按URLClassLoader的实现，从jar file查找resourceName.
 * <p>
 * 所以resourceName无需以"/"打头即表示jar file的根目录，带了"/"反而导致{@link java.util.jar.JarFile#getEntry(String)} 时没有返回.
 * <p>
 * 指定ContextClass时，class.getResource()会先对name进行处理再交给classLoader，打头的"/"会被去除，
 * 不以"/"打头则表示与该contextClass package的相对路径，会转为绝对路径
 * <p>
 * 3、同名资源
 * 如果有多个同名资源，除非调用getResources()获取全部资源，否则在URLClassLoader中按ClassPath顺序打开第一个命中的Jar文件.
 * @Author: robust
 * @CreateDate: 2019/7/23 11:28
 * @Version: 1.0
 */
public class ResourceUtil {
    /**
     * 打开单个文件
     */

    public static URL asURL(String resource) {
        return Resources.getResource(resource);
    }

    public static URL asURL(Class<?> contextClass, String resource) {
        return Resources.getResource(contextClass, resource);
    }

    public static InputStream asStream(String resource) throws IOException {
        return Resources.getResource(resource).openStream();
    }

    /**
     * 读取文件的每一行
     * @param contextClass
     * @param resource
     * @return
     * @throws IOException
     */
    public static InputStream asStream(Class<?> contextClass, String resource) throws IOException {
        return Resources.getResource(contextClass, resource).openStream();
    }

    /*--------------------------------------读取单个文件内容----------------------------------------*/

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static String toString(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static String toString(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(contextClass, resourceName), Charsets.UTF_8);
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static List<String> toLines(String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(resourceName), Charsets.UTF_8);
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static List<String> toLines(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(contextClass, resourceName), Charsets.UTF_8);
    }


}
