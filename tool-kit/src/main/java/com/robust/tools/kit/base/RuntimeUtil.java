package com.robust.tools.kit.base;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 运行时工具类
 * <p>
 * 1、取得当前进程PID, JVM参数
 * 2、注册JVM关闭钩子, 获得CPU核数
 * 3、通过StackTrace 获得当前方法的类名方法名，调用者的类名方法名(获得StackTrace有消耗，不要滥用)
 * @Author: robust
 * @CreateDate: 2019/7/23 16:28
 * @Version: 1.0
 */
public class RuntimeUtil {

    private static final AtomicInteger shutdownHookThreadIndex = new AtomicInteger(0);

    /*RuntimeMXBean相关*/

    /**
     * 获得当前进程PID
     * 当失败时返回-1
     *
     * @return
     */
    public static int getPid() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getVmName();
        String[] split = jvmName.split("@");
        if (split.length != 2) {
            return -1;
        }
        try {
            return Integer.valueOf(split[0]);
        } catch (Exception e) {
            return -1;
        }
    }
}
