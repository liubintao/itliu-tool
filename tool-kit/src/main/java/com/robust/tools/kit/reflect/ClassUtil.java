package com.robust.tools.kit.reflect;

import com.robust.tools.kit.base.Validate;
import com.robust.tools.kit.convert.BasicType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @Description: Class工具类
 * <p>
 * 1、获取类名，包名，循环向上的全部父类，全部接口
 * 2、其他便捷函数
 * @Author: robust
 * @CreateDate: 2019/7/18 11:37
 * @Version: 1.0
 */
public class ClassUtil {

    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    /*short class and package name*/

    /**
     * 返回短Class名, 不包含package
     * 内部类的话，返回"主类.内部类"
     *
     * @param clz
     * @return
     */
    public static String getShortClassName(final Class<?> clz) {
        return ClassUtils.getShortClassName(clz);
    }

    /**
     * 返回短Class名, 不包含package
     * 内部类的话，返回"主类.内部类"
     *
     * @param className
     * @return
     */
    public static String getShortClassName(final String className) {
        return ClassUtils.getShortClassName(className);
    }

    /**
     * 返回packageName
     *
     * @param clz
     * @return
     */
    public static String getPackageName(final Class<?> clz) {
        return ClassUtils.getPackageName(clz);
    }

    /**
     * 返回packageName
     *
     * @param className
     * @return
     */
    public static String getPackageName(final String className) {
        return ClassUtils.getPackageName(className);
    }

    /*----------------------------------获取全部父类，全部接口---------------------------------------*/

    /**
     * 递归返回所有的superclass，包含Object.class
     *
     * @param clz
     * @return
     */
    public static List<Class<?>> getAllSuperClasses(final Class<?> clz) {
        return ClassUtils.getAllSuperclasses(clz);
    }

    /**
     * 递归返回本类及所有基类实现的接口，及接口继承的接口，比Spring中的相同实现完整
     *
     * @param clz
     * @return
     */
    public static List<Class<?>> getAllInterface(final Class<?> clz) {
        return ClassUtils.getAllInterfaces(clz);
    }

    /*----------------------------------杂项-----------------------------------------*/

    /**
     * https://github.com/linkedin/linkedin-utils/blob/master/org.linkedin.util-core/src/main/java/org/linkedin/util/reflect/ReflectUtils.java
     * <p>
     * The purpose of this method is somewhat to provide a better naming / documentation than the javadoc of
     * <code>Class.isAssignableFrom</code> method.
     *
     * @return <code>true</code> if subclass is a subclass or sub interface of superclass
     */
    public static boolean isSubClassOrInterfaceOf(Class subclass, Class superclass) {
        return superclass.isAssignableFrom(subclass);
    }

    /**
     * 获取cglib处理过后的实体的原class
     *
     * @param instance
     * @return
     */
    public static Class<?> unWrapCglib(final Object instance) {
        Validate.notNull(instance, "Instance must not be null");
        Class<?> clz = instance.getClass();
        if (clz != null && clz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClz = clz.getSuperclass();
            if (superClz != null && !Object.class.equals(superClz)) {
                return superClz;
            }
        }
        return clz;
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型.
     * 注意：泛型必须定义在父类处，这是唯一可以通过反射从泛型获得Class实例的地方.
     * 如无法找到,返回Object.class
     * e.g. public subClz extends superClz<Integer>
     *
     * @param clz the class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    public static Class<?> getClassGenericType(final Class<?> clz) {
        return getClassGenericType(clz, 0);
    }

    public static Class getClassGenericType(final Class<?> clz, final int index) {
        Type genType = clz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clz.getSimpleName() + "'s superClass not ParameterizedType");
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of " + clz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回<code>true</code>
     *
     * @param types1 类组1
     * @param types2 类组2
     * @return
     */
    public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2) {
        if (ArrayUtils.isEmpty(types1) && ArrayUtils.isEmpty(types2)) {
            return true;
        }

        if (null == types1 || null == types2) {
            // 任何一个为null不相等（之前已判断两个都为null的情况）
            return false;
        }

        if (types1.length != types2.length) {
            return false;
        }

        Class<?> type1;
        Class<?> type2;
        for (int i = 0; i < types1.length; i++) {
            type1 = types1[i];
            type2 = types2[i];
            if (isBasicType(type1) && isBasicType(type2)) {
                /**
                 * 原始类型和其包装类存在不一致情况
                 */
                if (BasicType.unWrap(type1) != BasicType.unWrap(type2)) {
                    return false;
                } else if (type1.isAssignableFrom(type2)) {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 是否为基本类型和其包装类
     *
     * @param clz
     * @return
     */
    private static boolean isBasicType(Class<?> clz) {
        if (clz == null) {
            return false;
        }

        return clz.isPrimitive() || isPrimitiveWrapper(clz);
    }

    /**
     * 判断是否为原始类型的包装类
     *
     * @param clz
     * @return
     */
    private static boolean isPrimitiveWrapper(Class<?> clz) {
        if (clz == null) {
            return false;
        }
        return BasicType.wrapperPrimitiveMap.containsKey(clz);
    }

    /**
     * 判断是否是静态方法
     *
     * @param method
     * @return
     */
    public static boolean isStatic(Method method) {
        Validate.notNull(method, "Method to provided must not be null");
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 获得对象数组的类数组
     *
     * @param objects 对象数组，如果数组中存在{@code null}元素，则此元素被认为是Object类型
     * @return 类数组
     */
    public static Class<?>[] getClasses(Object[] objects) {
        Class<?>[] classes = new Class<?>[objects.length];
        Object obj;
        for (int i = 0; i < objects.length; i++) {
            classes[i] = (obj = objects[i]) == null ? Object.class : obj.getClass();
        }
        return classes;
    }


    /**
     * 获取指定类型分的默认值<br>
     * 默认值规则为：
     *
     * <pre>
     * 1、如果为原始类型，返回0
     * 2、非原始类型返回{@code null}
     * </pre>
     *
     * @param clazz 类
     * @return 默认值
     * @since 1.0
     */
    public static Object getDefaultValue(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (long.class == clazz) {
                return 0L;
            } else if (int.class == clazz) {
                return 0;
            } else if (short.class == clazz) {
                return (short) 0;
            } else if (char.class == clazz) {
                return (char) 0;
            } else if (byte.class == clazz) {
                return (byte) 0;
            } else if (double.class == clazz) {
                return 0D;
            } else if (float.class == clazz) {
                return 0f;
            } else if (boolean.class == clazz) {
                return false;
            }
        }

        return null;
    }

    /**
     * 获得默认值列表
     *
     * @param classes 值类型
     * @return 默认值列表
     * @since 1.0
     */
    public static Object[] getDefaultValues(Class<?>... classes) {
        final Object[] values = new Object[classes.length];
        for (int i = 0; i < classes.length; i++) {
            values[i] = getDefaultValue(classes[i]);
        }
        return values;
    }
}
