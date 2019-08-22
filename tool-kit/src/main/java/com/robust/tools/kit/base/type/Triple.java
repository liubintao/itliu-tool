package com.robust.tools.kit.base.type;

import com.robust.tools.kit.base.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 引入一个简简单单的Triple, 用于返回值返回三个元素.
 * <p>
 * copy from Twitter Common
 * @Author: robust
 * @CreateDate: 2019/7/22 9:44
 * @Version: 1.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Triple<L, M, R> {
    @Nullable
    private final L left;
    @Nullable
    private final M middle;
    @Nullable
    private final R right;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(left, triple.left) &&
                Objects.equals(middle, triple.middle) &&
                Objects.equals(right, triple.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, middle, right);
    }

    /**
     * 根据等号左边的泛型，自动构造合适的Triple
     *
     * @param left
     * @param middle
     * @param right
     * @param <L>
     * @param <M>
     * @param <R>
     * @return
     */
    public static <L, M, R> Triple<L, M, R> of(@Nullable L left, @Nullable M middle, @Nullable R right) {
        return new Triple<>(left, middle, right);
    }
}
