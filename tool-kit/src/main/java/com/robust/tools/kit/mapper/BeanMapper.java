package com.robust.tools.kit.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.robust.tools.kit.collection.ArrayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 实现深度的BeanOfClasssA<->BeanOfClassB复制
 * 不要使用Apache Common BeanUtils进行类复制，每次只是反射查询对象的属性列表, 非常缓慢.
 * <p>
 * orika性能比Dozer快近十倍，也不需要Getter函数与无参构造函数
 * <p>
 * 此版本没有修复的bug: https://github.com/orika-mapper/orika/issues/252
 * <p>
 * 如果应用启动时有并发流量进入，可能导致两个不同类型的同名属性间(如Order的User user属性，与OrderVO的UserVO user)的复制失败，只有重启才能解决。
 * <p>
 * 因此安全起见，仍然使用Dozer。
 * <p>
 * 注意: 需要参考POM文件，显式引用Dozer.
 * @Author: robust
 * @CreateDate: 2019/8/15 16:45
 * @Version: 1.0
 */
public class BeanMapper {
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    /**
     * 简单的复制新对象
     *
     * @param source           源对象
     * @param destinationClass 目标对象Class
     * @param <S>              源对象Class类型
     * @param <D>              目标对象Class类型
     * @return 目标对象的一个实例
     */
    public static <S, D> D map(final S source, final Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    /**
     * 复制出新对象ArrayList
     *
     * @param sourceList       源对象实例集合
     * @param destinationClass 目标对象Class
     * @param <S>              源对象Class
     * @param <D>              目标对象Class
     * @return 目标对象实例集合
     */
    public static <S, D> List<D> mapList(final Iterable<S> sourceList, final Class<D> destinationClass) {
        List<D> destList = new ArrayList<>();
        for (S source : sourceList) {
            destList.add(mapper.map(source, destinationClass));
        }
        return destList;
    }

    /**
     * 复制出新对象Array
     *
     * @param sourceArray      源对象实例数组
     * @param destinationClass 目标对象Class
     * @param <S>              源对象Class
     * @param <D>              目标对象Class
     * @return 目标对象实例集合
     */
    public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
        D[] destArray = ArrayUtil.newArray(destinationClass, sourceArray.length);
        int i = 0;
        for (S source : sourceArray) {
            if (source != null) {
                destArray[i] = mapper.map(sourceArray[i], destinationClass);
                i++;
            }
        }
        return destArray;
    }
}
