package com.robust.tools.kit.lang;

import com.robust.tools.kit.io.FileUtil;
import com.robust.tools.kit.io.URLResourceUtil;
import com.robust.tools.kit.reflect.ClassLoaderUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * @Description: Jar类加载器
 * @Author: robust
 * @CreateDate: 2019/7/18 11:35
 * @Version: 1.0
 */

public class JarClassLoader extends URLClassLoader {

    public JarClassLoader() {
        this(new URL[]{});
    }

    public JarClassLoader(URL[] urls) {
        super(urls, ClassLoaderUtil.getClassLoader());
    }

    /**
     * 加载jar文件或目录下的jar文件
     *
     * @param dir
     * @return
     */
    public static JarClassLoader load(File dir) {
        final JarClassLoader classLoader = new JarClassLoader();
        classLoader.addJar(dir); //查找加载所有jar
        classLoader.addURL(dir); //查找加载所有class
        return classLoader;
    }

    /**
     * 加载所有jar
     *
     * @param dir
     */
    private void addJar(File dir) {
        if (isJarFile(dir)) {
            addURL(dir);
        }
        if (dir.isDirectory()) {
            List<File> files = loopJars(dir);
            files.stream().forEach((f) -> {
                addURL(f);
            });
        }

    }

    /**
     * 递归获得jar文件
     *
     * @param file
     * @return
     */
    private static List<File> loopJars(File file) {
        return FileUtil.loopFiles(file, (f) -> isJarFile(f));
    }

    /**
     * 判断文件是否以.jar结尾
     *
     * @param dir
     * @return
     */
    private static boolean isJarFile(File dir) {
        if (null == dir || !dir.isFile()) {
            return false;
        }
        return dir.getPath().toLowerCase().endsWith(".jar");
    }

    /**
     * 增加class所在目录或文件<br>
     * 如果为目录，此目录用于搜索class文件，如果为文件，需为jar文件
     *
     * @param dir 目录
     */
    public JarClassLoader addURL(File dir) {
        super.addURL(URLResourceUtil.getURL(dir));
        return this;
    }
}
