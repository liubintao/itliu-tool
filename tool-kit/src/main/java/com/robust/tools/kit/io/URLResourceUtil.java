package com.robust.tools.kit.io;

import com.robust.tools.kit.base.Validate;
import com.robust.tools.kit.base.type.UnCheckedException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @Description: 兼容文件url为无前缀, classpath:,file:// 三种方式的Resource读取工具集
 * <p>
 * e.g: classpath:com/app/config.xml, file:///data/config.xml, /data/config.xml
 * <p>
 * 参考Spring ResourceUtils
 * @Author: robust
 * @CreateDate: 2019/7/23 9:28
 * @Version: 1.0
 */
public class URLResourceUtil {

    private static final String CLASSPATH_PREFIX = "classpath:";

    private static final String URL_PROTOCOL_FILE = "file";

    /**
     * 兼容无前缀, classpath, file:// 的情况获取文件
     * 如果以classpath: 定义的文件不存在会抛出IllegalArgumentException异常，以file://定义的则不会
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static File asFile(String path) throws FileNotFoundException {
        if (StringUtils.startsWith(path, CLASSPATH_PREFIX)) {
            String resource = StringUtils.substringAfter(path, CLASSPATH_PREFIX);
            return getFileByURL(ResourceUtil.asURL(resource));
        }
        try {
            return getFileByURL(new URL(path));
        } catch (MalformedURLException e) {
            // no URL -> treat as file path
            return new File(path);
        }
    }

    public static InputStream asStream(String path) throws IOException {
        if (StringUtils.startsWith(path, CLASSPATH_PREFIX)) {
            String resource = StringUtils.substringAfter(path, CLASSPATH_PREFIX);
            return ResourceUtil.asStream(resource);
        }
        try {
            // try URL
            return FileUtil.asInputStream(getFileByURL(new URL(path)));
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            return FileUtil.asInputStream(path);
        }
    }

    private static File getFileByURL(URL fileUrl) throws FileNotFoundException {
        Validate.notNull(fileUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILE.equals(fileUrl.getProtocol())) {
            throw new FileNotFoundException("URL cannot be resolved to absolute file path "
                    + "because it dose not reside in the file system:" + fileUrl);
        }
        try {
            return new File(toURI(fileUrl.toString()).getSchemeSpecificPart());
        } catch (URISyntaxException e) {
            // Fallback for URLs that are not valid URIs (should hardly ever happen).
            return new File(fileUrl.getFile());
        }
    }

    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }

    /**
     * 获得URL，常用于使用绝对路径时的情况
     *
     * @param file URL对应的文件对象
     * @return URL
     * @throws MalformedURLException
     */
    public static URL getURL(File file) {
        Validate.notNull(file, "File is null !");

        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new UnCheckedException(e);
        }

    }
}
