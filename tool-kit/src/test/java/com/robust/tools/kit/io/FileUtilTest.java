package com.robust.tools.kit.io;

import org.junit.Test;

import java.io.*;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/14 13:00
 * @Version: 1.0
 */
public class FileUtilTest {

    @Test
    public void test() throws IOException {
        Path temp = FileUtil.createTempFile();
        File file = temp.toFile();
        FileUtil.write("123", file);
        assertThat(new String(FileUtil.toByteArray(file))).isEqualTo("123");

        FileUtil.append("456", file);
        assertThat(FileUtil.toString(file)).isEqualTo("123456");

        FileUtil.append("\r\n345", file);
        assertThat(FileUtil.toLines(file)).hasSize(2);

        OutputStream outputStream = FileUtil.asOutputStream(file);
        outputStream.write("11".getBytes());
        outputStream.flush();
        assertThat(FileUtil.toString(file)).isEqualTo("11");

        BufferedReader bufferedReader = FileUtil.asBufferedReader(file);
        assertThat(bufferedReader.readLine()).isEqualTo("11");

        BufferedWriter bufferedWriter = FileUtil.asBufferedWriter(file);
        bufferedWriter.write("22");
        bufferedWriter.flush();
        assertThat(FileUtil.toString(file)).isEqualTo("22");

        File file1 = new File(file.getName() + "1");
        FileUtil.copyFile(file, file1);
        assertThat(FileUtil.toString(file)).isEqualTo("22");

        FileUtil.deleteFile(temp);
        FileUtil.deleteFile(file1);

        file = FileUtil.createTempFile().toFile();
        FileUtil.append("23", file);
        FileUtil.moveFile(file, file1);
        assertThat(FileUtil.isFileExists(file)).isFalse();
        assertThat(FileUtil.isFileExists(file1)).isTrue();

//        System.out.println(FileUtil.getFileName("\\home\\admin\\1.txt"));

        FileUtil.deleteFile(file1);

        Path path = FileUtil.createTempDirectory();
        file = path.toFile();
        assertThat(FileUtil.isDirectoryExists(path)).isTrue();
        assertThat(FileUtil.isDirectoryExists(file)).isTrue();
        FileUtil.deleteDirectory(path);
        assertThat(FileUtil.isDirectoryExists(path)).isFalse();
        assertThat(FileUtil.isDirectoryExists(file)).isFalse();

        path = FileUtil.createTempDirectory();
        FileUtil.makeSureDirectoryExists(path.resolve("home"));
        assertThat(FileUtil.isDirectoryExists(path.resolve("home"))).isTrue();
    }
}