package com.robust.tools.kit.id;

import com.robust.tools.kit.number.RandomUtil;

import java.util.Random;
import java.util.UUID;

/**
 * @Description: id生成工具类
 * @Author: robust
 * @CreateDate: 2019/8/6 20:17
 * @Version: 1.0
 */
public class IdUtil {

    /**
     * 返回使用ThreadLocalRandom的UUID,比默认的UUID性能更优
     *
     * @return
     */
    public static UUID fastUUID() {
        Random random = RandomUtil.threadLocalRandom();
        return new UUID(random.nextLong(), random.nextLong());
    }
}
