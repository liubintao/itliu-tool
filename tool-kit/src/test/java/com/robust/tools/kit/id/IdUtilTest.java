package com.robust.tools.kit.id;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/6 20:20
 * @Version: 1.0
 */
public class IdUtilTest {

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        for(int i = 0; i < 10000000; i++) {
            System.out.println(IdUtil.fastUUID());
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void test1() {
        long start = System.currentTimeMillis();
        for(int i = 0; i < 10000000; i++) {
            System.out.println(UUID.randomUUID());
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}