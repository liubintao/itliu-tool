package com.robust.tools.kit.concurrent.threadpool;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/5 17:33
 * @Version: 1.0
 */
public class AbortPolicyWithReportTest {

    @Test
    public void jstackDump() throws InterruptedException {
        AbortPolicyWithReport abortPolicyWithReport = new AbortPolicyWithReport("test");
        try {
            abortPolicyWithReport.rejectedExecution(() -> {
                System.out.println("hello");
            }, (ThreadPoolExecutor) Executors.newFixedThreadPool(1));
        } catch (RejectedExecutionException e) {

        }

        Thread.sleep(1000);
    }
}