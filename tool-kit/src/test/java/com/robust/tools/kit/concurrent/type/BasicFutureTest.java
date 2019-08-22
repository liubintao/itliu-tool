package com.robust.tools.kit.concurrent.type;

import com.robust.tools.kit.base.ExceptionUtil;
import com.robust.tools.kit.base.ObjectUtil;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/6 11:59
 * @Version: 1.0
 */
public class BasicFutureTest {

    public static class Task<T> extends BasicFuture<T> {

        @Override
        protected void onCompleted(T result) {
            System.out.println("onCompleted: " + ObjectUtil.toPrettyString(result));
        }

        @Override
        protected void onFailed(Exception ex) {
            System.out.println("onFailed: " + ObjectUtil.toPrettyString(ex));
        }

        @Override
        protected void onCancelled() {
            System.out.println("onCancelled");
        }
    }

    private static class Tasks {

        public static void success(Task<String> future) {
            future.completed("haha");
        }

        public static void fail(Task<String> future) {
            future.failed(new RuntimeException("wuwu"));
        }

        public static void cancel(Task<String> future) {
            future.cancel(true);
        }
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        Task<String> future = new Task();
        Tasks.success(future);
        String result = future.get();
        System.out.println(result);

        future = new Task<>();
        try {
            future.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            assertThat(e).isInstanceOf(TimeoutException.class);
        }

        future = new Task<>();
        Tasks.fail(future);
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            assertThat(ExceptionUtil.unwrap(e)).isInstanceOf(RuntimeException.class);
        }
    }
}