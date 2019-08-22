package com.robust.tools.kit.io;

import com.robust.tools.kit.base.Platforms;
import com.robust.tools.kit.base.Validate;
import com.robust.tools.kit.base.annotation.NotNull;
import com.robust.tools.kit.base.annotation.Nullable;
import com.robust.tools.kit.text.Charsets;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 关于文件的工具集
 * <p>
 * 主要是调用JDK自带的Files工具类，少量代码调用Guava {@link com.google.common.io.Files}. 固定encoding为UTF8
 * 1、文件读写
 * 2、文件及目录操作
 * @Author: robust
 * @CreateDate: 2019/7/23 15:42
 * @Version: 1.0
 */
public class FileUtil {

    // fileVisitor for file deletion on Files.walkFileTree
    private static FileVisitor<Path> deleteFileVisitor = new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    /*--------------------------------文件读写------------------------------------------*/

    /**
     * 读取文件到byte[]
     *
     * @param file 文件
     * @return byte[]
     * @throws IOException
     */
    public static byte[] toByteArray(final File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    /**
     * 读取文件到String
     *
     * @param file 文件
     * @return String
     * @throws IOException
     */
    public static String toString(final File file) throws IOException {
        return com.google.common.io.Files.toString(file, Charsets.UTF_8);
    }

    /**
     * 读取文件的每行内容到List<String>
     *
     * @param file 文件
     * @return List<String>
     * @throws IOException
     */
    public static List<String> toLines(final File file) throws IOException {
        return Files.readAllLines(file.toPath(), Charsets.UTF_8);
    }

    /**
     * 写入String到File
     *
     * @param data 数据
     * @param file 文件
     * @throws IOException
     */
    public static void write(final CharSequence data, final File file) throws IOException {
        Validate.notNull(data);
        Validate.notNull(file);

        try (BufferedWriter bw = Files.newBufferedWriter(file.toPath(), Charsets.UTF_8)) {
            bw.append(data);
        }
    }

    /**
     * 追加String到File
     *
     * @param data 数据
     * @param file 文件
     * @throws IOException
     */
    public static void append(final CharSequence data, final File file) throws IOException {
        Validate.notNull(data);
        Validate.notNull(file);

        try (BufferedWriter bw = Files.newBufferedWriter(file.toPath(), Charsets.UTF_8, StandardOpenOption.APPEND)) {
            bw.append(data);
        }
    }

    /**
     * 打开文件为InputStream
     * {@link java.nio.file.Files#newInputStream(Path, OpenOption...)}
     *
     * @param fileName
     * @return
     */
    public static InputStream asInputStream(String fileName) throws IOException {
        return asInputStream(getPath(fileName));
    }

    /**
     * 打开文件为InputStream
     * {@link java.nio.file.Files#newInputStream(Path, OpenOption...)}
     *
     * @param file
     * @return
     */
    public static InputStream asInputStream(File file) throws IOException {
        Validate.notNull(file, "file must not be null");
        return asInputStream(file.toPath());
    }

    /**
     * 打开文件为InputStream
     * {@link java.nio.file.Files#newInputStream(Path, OpenOption...)}
     *
     * @param path
     * @return
     */
    public static InputStream asInputStream(Path path) throws IOException {
        Validate.notNull(path, "path must not be null");
        return Files.newInputStream(path);
    }

    /**
     * 打开文件为OutputStream
     */
    public static OutputStream asOutputStream(String fileName) throws IOException {
        return asOutputStream(getPath(fileName));
    }

    /**
     * 打开文件为OutputStream
     */
    public static OutputStream asOutputStream(File file) throws IOException {
        Validate.notNull(file, "file must not be null");
        return asOutputStream(file.toPath());
    }

    /**
     * 打开文件为OutputStream
     *
     * @param path 文件
     * @return OutputStream
     * @throws IOException
     */
    public static OutputStream asOutputStream(Path path) throws IOException {
        Validate.notNull(path, "path must not be null");
        return Files.newOutputStream(path);
    }

    /**
     * 获取文件的BufferedReader
     */
    public static BufferedReader asBufferedReader(String fileName) throws IOException {
        return asBufferedReader(getPath(fileName));
    }

    /**
     * 获取文件的BufferedReader
     */
    public static BufferedReader asBufferedReader(File file) throws IOException {
        Validate.notNull(file, "file must not be null");
        return asBufferedReader(file.toPath());
    }

    /**
     * 获取文件的BufferedReader
     *
     * @param path 文件
     * @return BufferedReader
     * @throws IOException
     */
    public static BufferedReader asBufferedReader(Path path) throws IOException {
        Validate.notNull(path, "path must not be null");
        return Files.newBufferedReader(path, Charsets.UTF_8);
    }

    /**
     * 获取文件的BufferedWriter
     */
    public static BufferedWriter asBufferedWriter(String fileName) throws IOException {
        return asBufferedWriter(getPath(fileName));
    }

    /**
     * 获取文件的BufferedWriter
     */
    public static BufferedWriter asBufferedWriter(File file) throws IOException {
        Validate.notNull(file, "file must not be null");
        return asBufferedWriter(file.toPath());
    }

    /**
     * 获取文件的BufferedWriter
     *
     * @param path 文件
     * @return BufferedWriter
     * @throws IOException
     */
    public static BufferedWriter asBufferedWriter(Path path) throws IOException {
        Validate.notNull(path, "path must not be null");
        return Files.newBufferedWriter(path, Charsets.UTF_8);
    }

    /*-------------文件操作-----------------*/

    /**
     * 复制文件或目录
     */
    public static void copy(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);
        copy(from.toPath(), to.toPath());
    }

    /**
     * 复制文件或目录, not following links.
     *
     * @param from 如果为null，或者是不存在的文件或目录，抛出异常.
     * @param to   如果为null，或者from是目录而to是已存在文件，或相反
     */
    public static void copy(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);
        if (Files.isDirectory(from)) {
            copyDirectory(from, to);
        } else {
            copyFile(from, to);
        }
    }

    /**
     * 复制文件
     */
    public static void copyFile(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);
        copyFile(from.toPath(), to.toPath());
    }

    /**
     * 复制文件
     *
     * @param from 源文件，如果为null，或文件不存在或者是目录，抛出异常
     * @param to   目标文件，如果to为null，或文件存在但是一个目录，抛出异常
     * @throws IOException
     */
    public static void copyFile(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.isTrue(isFileExists(from), "%s is not exists or not a file", from);
        Validate.notNull(to);
        Validate.isTrue(!isDirectoryExists(to), "%s is exists but not a file", to);
        Files.copy(from, to);
    }

    /**
     * 复制目录
     */
    public static void copyDirectory(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);
        copyDirectory(from.toPath(), to.toPath());
    }

    /**
     * 复制目录
     *
     * @param from 源目录
     * @param to   目标目录
     * @throws IOException
     */
    public static void copyDirectory(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.isTrue(isDirectoryExists(from), "%s is not exists or not is a directory", from);
        Validate.notNull(to);
        makeSureDirectoryExists(to);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(to)) {
            for (Path path : directoryStream) {
                copy(path, path.resolve(path.getFileName()));
            }
        }
    }


    /**
     * 递归处理文件
     *
     * @param file       文件/目录
     * @param fileFilter 文件过滤器
     * @return
     */
    public static List<File> loopFiles(File file, FileFilter fileFilter) {
        List<File> fileList = new ArrayList<>();
        if (null == file || !file.exists()) {
            return fileList;
        }

        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        fileList.addAll(loopFiles(f, fileFilter));

                    }
                }
            }
        } else {
            if (null != fileFilter && fileFilter.accept(file)) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * 文件移动/重命名
     */
    public static void moveFile(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);
        moveFile(from.toPath(), to.toPath());
    }

    /**
     * 文件移动/重命名
     *
     * @see Files#move(Path, Path, CopyOption...)
     */
    public static void moveFile(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.isTrue(isFileExists(from), "%s is not exists or not a file", from);
        Validate.notNull(to);
        Validate.isTrue(!isDirectoryExists(to), "%s exists but is a directory", to);
        Files.move(from, to);
    }

    /**
     * 目录移动,重命名
     *
     * @param from
     * @param to
     * @throws IOException
     */
    public static void moveDir(@NotNull File from, @NotNull File to) throws IOException {
        Validate.isTrue(isDirectoryExists(from), "%s is not exists or not a directory", from);
        Validate.notNull(to);
        Validate.isTrue(!isFileExists(to), "%s is exists but it is a file", to);
        final boolean rename = from.renameTo(to);
        if (!rename) {
            if (to.getCanonicalPath().startsWith(from.getCanonicalPath() + File.separator)) {
                throw new IOException("Cannot move directory: " + from + " to a subdirectory of itself: " + to);
            }
            copyDirectory(from, to);
            deleteDirectory(from);
            if (from.exists()) {
                throw new IOException("Failed to delete original directory '" + from + "' after copy to '" + to + '\'');
            }
        }
    }

    /**
     * 创建文件或更新时间戳
     */
    public static void touch(@NotNull String fileName) throws IOException {
        touch(new File(fileName));
    }

    /**
     * 创建文件或更新时间戳
     *
     * @param file 文件
     * @throws IOException
     * @see com.google.common.io.Files#touch(File)
     */
    public static void touch(@NotNull File file) throws IOException {
        com.google.common.io.Files.touch(file);
    }

    /**
     * 删除文件
     * 如果文件不存在或者是目录,则不做修改
     */
    public static void deleteFile(@Nullable File file) throws IOException {
        Validate.isTrue(isFileExists(file), "%s is not exists or not a file", file);
        deleteFile(file.toPath());
    }

    /**
     * 删除文件
     * 如果文件不存在或者是目录,则不做修改
     */
    public static void deleteFile(@Nullable Path path) throws IOException {
        Validate.isTrue(isFileExists(path), "%s is not exists or not a file", path);
        Files.delete(path);
    }

    /**
     * 删除目录及所有子目录/文件
     */
    public static void deleteDirectory(@Nullable File dir) throws IOException {
        Validate.isTrue(isDirectoryExists(dir), "%s is not exists or not a directory", dir);
        deleteDirectory(dir.toPath());
    }

    /**
     * 删除目录及所有子目录/文件
     */
    public static void deleteDirectory(@Nullable Path dir) throws IOException {
        Validate.isTrue(isDirectoryExists(dir), "%s is not exists or not a directory", dir);
        //后序遍历,先删除掉子目录中的文件/目录
        Files.walkFileTree(dir, deleteFileVisitor);
    }

    /**
     * 获取文件名,不包含路径
     */
    public static String getFileName(@NotNull String fullName) {
        Validate.notBlank(fullName);
        int last = fullName.lastIndexOf(Platforms.FILE_PATH_SEPARATOR_CHAR);
        return fullName.substring(last + 1);
    }

    /**
     * 判断目录是否存在, from Jodd.
     */
    public static boolean isDirectoryExists(String directory) {
        return directory != null && isDirectoryExists(getPath(directory));
    }

    /**
     * 判断目录是否存在, from Jodd.
     */
    public static boolean isDirectoryExists(File file) {
        return isDirectoryExists(file.toPath());
    }

    /**
     * 判断目录是否存在, from Jodd.
     *
     * @see {@link Files#exists}
     * @see {@link Files#isRegularFile}
     */
    public static boolean isDirectoryExists(Path path) {
        return path != null && Files.exists(path) && Files.isDirectory(path);
    }

    /**
     * 确保目录存在，如不存在则创建
     */
    public static void makeSureDirectoryExists(String dirPath) throws IOException {
        makeSureDirectoryExists(getPath(dirPath));
    }

    /**
     * 确保父目录至根目录都已经创建
     */
    public static void makeSureParentDirectoryExists(File file) throws IOException {
        Validate.notNull(file);
        makeSureDirectoryExists(file.getParentFile());
    }

    /**
     * 确保目录存在
     */
    public static void makeSureDirectoryExists(File file) throws IOException {
        Validate.notNull(file);
        makeSureDirectoryExists(file.toPath());
    }

    /**
     * 确保目录存在，不存在则创建
     *
     * @see Files#createDirectories(Path, FileAttribute[])
     */
    public static void makeSureDirectoryExists(Path dir) throws IOException {
        Validate.notNull(dir);
        Files.createDirectories(dir);
    }

    /**
     * 判断文件是否存在, from Jodd.
     */
    public static boolean isFileExists(String fileName) {
        return fileName != null && isFileExists(getPath(fileName));
    }

    /**
     * 判断文件是否存在, from Jodd.
     */
    public static boolean isFileExists(File file) {
        return isFileExists(file.toPath());
    }

    /**
     * 判断文件是否存在, from Jodd.
     *
     * @see {@link Files#exists}
     * @see {@link Files#isRegularFile}
     */
    public static boolean isFileExists(Path path) {
        return path != null && Files.exists(path) && Files.isRegularFile(path);
    }

    /**
     * 在临时目录创建临时目录,命名为${毫秒级时间戳}-${同一毫秒内的随机数}
     *
     * @return
     * @throws IOException
     */
    public static Path createTempDirectory() throws IOException {
        return Files.createTempDirectory(System.currentTimeMillis() + "-");
    }

    /**
     * 在临时目录创建临时文件，命名为tmp-${random.nextLong()}.tmp
     *
     * @return Path
     * @throws IOException
     * @see Files#createTempFile(String, String, FileAttribute[])
     */
    public static Path createTempFile() throws IOException {
        return createTempFile("tmp-", ".tmp");
    }

    /**
     * 在临时目录创建临时文件
     */
    public static Path createTempFile(String prefix, String suffix) throws IOException {
        return Files.createTempFile(prefix, suffix);
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     *
     * @param file 文件
     * @return 扩展名
     */
    public static String getFileExtension(File file) {
        return getFileExtension(file.getName());
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     *
     * @see {@link com.google.common.io.Files#getFileExtension}
     */
    public static String getFileExtension(String fullName) {
        return com.google.common.io.Files.getFileExtension(fullName);
    }

    /**
     * 将String类型文件路径转换为Path
     *
     * @param filePath 文件路径
     * @return Path
     */
    private static Path getPath(String filePath) {
        return Paths.get(filePath);
    }
}


