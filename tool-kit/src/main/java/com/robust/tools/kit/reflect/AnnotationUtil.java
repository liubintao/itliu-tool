package com.robust.tools.kit.reflect;

import com.robust.tools.kit.collection.SetUtil;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Description: Annotation的工具类
 * <p>
 * 1、获得类的全部Annotation
 * 2、获得类的标注了Annotation的所有属性和方法
 * @Author: robust
 * @CreateDate: 2019/7/16 9:07
 * @Version: 1.0
 */
public class AnnotationUtil {

    /**
     * 递归Class所有的Annotation，一个最彻底的实现.
     * <p>
     * 包括所有基类，所有接口的Annotation，同时支持Spring风格的Annotation继承的父Annotation，
     */
    public static Set<Annotation> getAllAnnotations(final Class<?> clz) {
        //获取所有父类
        List<Class<?>> allTypes = ClassUtil.getAllSuperClasses(clz);
        //获取实现的所有接口
        allTypes.addAll(ClassUtil.getAllInterface(clz));
        allTypes.add(clz);

        Set<Annotation> annotations = SetUtil.newHashSet();
        for (Class<?> type : allTypes) {
            annotations.addAll(Arrays.asList(type.getDeclaredAnnotations()));
        }

        Set<Annotation> superAnnotations = SetUtil.newHashSet();
        for (Annotation annotation : annotations) {
            getSuperAnnotations(annotation.annotationType(), superAnnotations);
        }
        annotations.addAll(superAnnotations);
        return annotations;


    }

    private static void getSuperAnnotations(Class<? extends Annotation> annotationType, Set<Annotation> superAnnotations) {
        Annotation[] annotations = annotationType.getDeclaredAnnotations();

        for (Annotation annotation : annotations) {
            if (!annotation.annotationType().getName().startsWith("java.lang") && superAnnotations.add(annotation)) {
                getSuperAnnotations(annotation.annotationType(), superAnnotations);
            }
        }
    }

    /**
     * 找出所有标注了该annotation的公共属性,循环遍历父类.
     * 暂未支持Spring风格Annotation继承Annotation.
     * copy from org.unitils.util.AnnotationUtils
     */
    public static <T extends Annotation> Set<Field> getAnnotatedPublicFields(Class<? extends Object> clazz, Class<T> annotation) {
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }
        Set<Field> annotatedFields = new HashSet<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }

    /**
     * 找出所有标注了该annotation的公共属性,循环遍历父类,包含private属性..
     * 暂未支持Spring风格Annotation继承Annotation.
     * copy from org.unitils.util.AnnotationUtils
     */
    public static <T extends Annotation> Set<Field> getFieldsAnnotatedWith(Class<? extends Object> clazz, Class<T> annotation) {
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }
        Set<Field> annotatedFields = new HashSet<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) {
                annotatedFields.add(field);
            }
        }
        annotatedFields.addAll(getFieldsAnnotatedWith(clazz.getSuperclass(), annotation));
        return annotatedFields;
    }

    /**
     * 找出所有标注了该annotation的公共方法(含父类的公共函数)，循环其接口.
     * <p>
     * 暂未支持Spring风格Annotation继承Annotation
     * <p>
     * 另，如果子类重载父类的公共函数，父类函数上的annotation不会继承，只有接口上的annotation会被继承.
     */
    public static <T extends Annotation> Set<Method> getAnnotatedPublicMethods(Class<?> clazz, Class<T> annotation) {
        // 已递归到Object.class, 停止递归
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }

        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(clazz);
        Set<Method> annotatedMethods = new HashSet<>();

        // 遍历当前类的所有公共方法
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            // 如果当前方法有标注，或定义了该方法的所有接口有标注
            if (method.getAnnotation(annotation) != null || searchOnInterfaces(method, annotation, interfaces)) {
                annotatedMethods.add(method);
            }
        }

        return annotatedMethods;
    }

    private static <T extends Annotation> boolean searchOnInterfaces(Method method, Class<T> annotationType,
                                                                     List<Class<?>> interfaces) {
        for (Class<?> iface : interfaces) {
            try {
                Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                if (equivalentMethod.getAnnotation(annotationType) != null) {
                    return true;
                }
            } catch (NoSuchMethodException ex) { // NOSONAR
                // Skip this interface - it doesn't have the method...
            }
        }
        return false;
    }
}
