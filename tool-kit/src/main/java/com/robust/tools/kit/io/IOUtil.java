package com.robust.tools.kit.io;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.robust.tools.kit.text.Charsets;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * @Description: IO Stream/Reader相关工具集. 固定encoding为UTF8.
 * <p>
 * 建议使用Apache Commons IO和Guava关于IO的工具类(com.google.common.io.*)
 * <p>
 * 1、安静关闭Closeable对象
 * 2、读出InputStream/Reader全部内容到String 或 List<String>
 * 3、读出InputStream/Reader一行内容到String
 * 4、将String写到OutputStream/Writer
 * 5、将String 转换为InputStream/Reader
 * 6、InputStream/Reader与OutputStream/Writer之间复制的copy
 * @Author: robust
 * @CreateDate: 2019/8/14 15:10
 * @Version: 1.0
 */
@Slf4j
public class IOUtil {

    /**
     * 在finally中安静的关闭,不再往外抛出异常影响原有异常,最常用函数.同时兼容Closeable为空未实际创建的情况.
     *
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;

        try {
            closeable.close();
        } catch (IOException e) {
            log.warn("IOException thrown while closing closeable.", e);
        }
    }


    /**
     * 读取InputStream到String
     */
    public static String toString(final InputStream input) throws IOException {
        return CharStreams.toString(new InputStreamReader(input, Charsets.UTF_8));
    }

    /**
     * 读取Reader到String
     */
    public static String toString(final Reader input) throws IOException {
        return CharStreams.toString(input);
    }

    /**
     * 读取Reader中的内容到List<String>
     */
    public static List<String> readLines(final Reader input) throws IOException {
        return CharStreams.readLines(input);
    }

    /**
     * 读取InputStream中的内容到List<String>
     */
    public static List<String> readLines(final InputStream input) throws IOException {
        return CharStreams.readLines(new BufferedReader(new InputStreamReader(input, Charsets.UTF_8)));
    }

    /**
     * 从Reader中读取一行数据
     */
    public static String readLine(final Reader input) throws IOException {
        return toBufferedReader(input).readLine();
    }

    /**
     * 从InputStream中读取一行数据,
     * e.g 从System.in中读取内容
     */
    public static String readLine(final InputStream input) throws IOException {
        return new BufferedReader(new InputStreamReader(input, Charsets.UTF_8)).readLine();
    }

    /**
     * 写入String到OutputStream
     */
    public static void write(final String data, final OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.getBytes(Charsets.UTF_8));
        }
    }

    /**
     * 写入String到Writer
     */
    public static void write(final String data, final Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * 字符串转成Reader
     */
    public static Reader toInputStreamReader(String input) {
        return new InputStreamReader(toInputStream(input));
    }

    /**
     * 字符串转成InputStream
     */
    public static InputStream toInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes(Charsets.UTF_8));
    }

    /**
     * 在Reader和Writer之间复制内容
     *
     * @see CharStreams#copy(Readable, Appendable)
     */
    public static long copy(final Reader input, final Writer output) throws IOException {
        return CharStreams.copy(input, output);
    }

    /**
     * 在InputStream和OutputStream之间复制内容
     *
     * @see ByteStreams#copy(InputStream, OutputStream)
     */
    public static long copy(final InputStream input, final OutputStream output) throws IOException {
        return ByteStreams.copy(input, output);
    }

    /**
     * 将Reader wrap成BufferedReader
     */
    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    /**
     * 压缩
     *
     * @param data 原始字节数组
     * @return 压缩后的字节数组
     */
    public static byte[] compress(byte[] data) {

        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length / 4);
        DeflaterOutputStream zipOut = new DeflaterOutputStream(out);
        try {
            zipOut.write(data);
            zipOut.finish();
            zipOut.close();
        } catch (IOException e) {
            log.error("compress ex", e);
            return new byte[0];
        } finally {
            closeQuietly(zipOut);
        }
        return out.toByteArray();
    }

    /**
     * 解压缩
     * @param data 压缩后的字节数组
     * @return 原始字节数组
     */
    public static byte[] decompress(byte[] data) {
        InflaterInputStream zipIn = new InflaterInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length * 4);
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = zipIn.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            log.error("decompress ex", e);
            return new byte[0];
        } finally {
            closeQuietly(zipIn);
        }
        return out.toByteArray();
    }
}
