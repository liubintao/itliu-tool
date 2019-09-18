package com.robust.tools.kit.base.type;

import com.robust.tools.kit.base.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 引入一个简简单单的Pair, 用于返回值返回两个元素.
 * <p>
 * copy from Twitter Common
 * @Author: robust
 * @CreateDate: 2019/7/19 11:53
 * @Version: 1.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Pair<L, R> {
    @Nullable
    private final L left;
    @Nullable
    private final R right;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(left, pair.left) &&
                Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        return prime * result + ((right == null) ? 0 : right.hashCode());
    }

    /**
     * 根据等号左边的泛型，自动构造合适的Pair
     *
     * @param left
     * @param right
     * @param <L>
     * @param <R>
     * @return
     */
    public static <L, R> Pair<L, R> of(@Nullable L left, @Nullable R right) {
        return new Pair<>(left, right);
    }
}
