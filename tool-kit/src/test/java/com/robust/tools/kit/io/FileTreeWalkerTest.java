package com.robust.tools.kit.io;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/12 17:44
 * @Version: 1.0
 */
public class FileTreeWalkerTest {

    @Test
    public void listAll() throws IOException {
        Path tmpDir = FileUtil.createTempDirectory();

        List<File> all = FileTreeWalker.listAll(tmpDir.toFile());
        System.out.println(tmpDir);
        assertThat(all).hasSize(1);

        List<File> files = FileTreeWalker.listFile(tmpDir.getParent().toFile());
        files.stream().forEach(System.out::println);
        System.out.println("-------------------------------");
        files = FileTreeWalker.listFileWithExtension(tmpDir.getParent().toFile(),"tmp");
        files.stream().forEach(System.out::println);
        System.out.println("-------------------------------");
        files = FileTreeWalker.listFileWithWildcardFileName(tmpDir.getParent().toFile(), "wctE*");
        files.stream().forEach(System.out::println);

        System.out.println("-------------------------------");
        files = FileTreeWalker.listFileWithRegexFileName(tmpDir.getParent().toFile(), "wctE.*\\.tmp");
        files.stream().forEach(System.out::println);

        System.out.println("-------------------------------");
        files = FileTreeWalker.listFileWithAntPath(tmpDir.getParent().toFile(), "wctE*");
        files.stream().forEach(System.out::println);

        FileUtil.deleteDirectory(tmpDir);
    }
}