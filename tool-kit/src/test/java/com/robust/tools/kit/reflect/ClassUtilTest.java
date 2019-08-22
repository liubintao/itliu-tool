package com.robust.tools.kit.reflect;

import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/21 8:29
 * @Version: 1.0
 */
public class ClassUtilTest {

    @Test
    public void getName() {
        assertThat(ClassUtil.getShortClassName(ClassUtilTest.class)).isEqualTo("ClassUtilTest");
        assertThat(ClassUtil.getShortClassName(AClass.class)).isEqualTo("ClassUtilTest.AClass");

        assertThat(ClassUtil.getShortClassName(ClassUtilTest.class.getName())).isEqualTo("ClassUtilTest");
        assertThat(ClassUtil.getShortClassName(AClass.class.getName())).isEqualTo("ClassUtilTest.AClass");

        assertThat(ClassUtil.getPackageName(ClassUtilTest.class)).isEqualTo("com.robust.tools.kit.reflect");
        assertThat(ClassUtil.getPackageName(AClass.class)).isEqualTo("com.robust.tools.kit.reflect");
        assertThat(ClassUtil.getPackageName(ClassUtilTest.class.getName())).isEqualTo("com.robust.tools.kit.reflect");
        assertThat(ClassUtil.getPackageName(AClass.class.getName())).isEqualTo("com.robust.tools.kit.reflect");
    }


    @Test
    public void getAllSuperClasses() {
        assertThat(ClassUtil.getAllSuperClasses(BClass.class)).hasSize(2).contains(AClass.class, Object.class);
        assertThat(ClassUtil.getAllInterface(BClass.class)).hasSize(4)
                .contains(AInterface.class, BInterface.class, CInterface.class, DInterface.class);
        assertThat(AnnotationUtil.getAllAnnotations(BClass.class)).hasSize(4);

        assertThat(AnnotationUtil.getAnnotatedPublicFields(BClass.class, AAnnotation.class)).hasSize(2).contains(
                ReflectionUtil.getField(BClass.class, "sfield"), ReflectionUtil.getField(BClass.class, "ufield"));

        assertThat(AnnotationUtil.getFieldsAnnotatedWith(BClass.class, EAnnotation.class)).hasSize(3).contains(
                ReflectionUtil.getField(BClass.class, "bfield"), ReflectionUtil.getField(BClass.class, "efield"),
                ReflectionUtil.getField(AClass.class, "afield"));

        assertThat(AnnotationUtil.getFieldsAnnotatedWith(BClass.class, FAnnotation.class)).hasSize(1)
                .contains(ReflectionUtil.getField(AClass.class, "dfield"));

        assertThat(AnnotationUtil.getAnnotatedPublicMethods(BClass.class, FAnnotation.class)).hasSize(3).contains(
                ReflectionUtil.getAccessibleMethodByName(BClass.class, "hello"),
                ReflectionUtil.getAccessibleMethodByName(BClass.class, "hello3"),
                ReflectionUtil.getAccessibleMethodByName(AClass.class, "hello4"));
    }

    @Test
    public void getSuperClassGenericType() {
        assertThat(ClassUtil.getClassGenericType(TestBean.class)).isEqualTo(String.class);
        assertThat(ClassUtil.getClassGenericType(TestBean.class, 1)).isEqualTo(Long.class);

        assertThat(ClassUtil.getClassGenericType(TestBean2.class)).isEqualTo(Object.class);

        assertThat(ClassUtil.getClassGenericType(TestBean3.class)).isEqualTo(Object.class);
    }

    @Test
    public void classPresent() {
        assertThat(ClassLoaderUtil.isPresent("a.b.c", ClassLoaderUtil.getClassLoader())).isFalse();
        assertThat(ClassLoaderUtil.isPresent("com.robust.tools.kit.reflect.ClassUtil",
                ClassLoaderUtil.getClassLoader())).isTrue();
    }

    @Test
    public void testIsSubClassOrInterfaceOf() {
        assertTrue("TestBean should be subclass of ParentBean",
                ClassUtil.isSubClassOrInterfaceOf(BClass.class, AClass.class));
        assertTrue("BInterface should be subinterface of AInterface",
                ClassUtil.isSubClassOrInterfaceOf(BInterface.class, AInterface.class));
        assertTrue("BClass should be an implementation of BInterface",
                ClassUtil.isSubClassOrInterfaceOf(BClass.class, BInterface.class));
        assertTrue("BClass should be an implementation of AInterface",
                ClassUtil.isSubClassOrInterfaceOf(BClass.class, AInterface.class));
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface AAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @AAnnotation
    public @interface BAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface CAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface DAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface EAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface FAnnotation {
    }

    public interface AInterface {
    }

    @CAnnotation
    public interface BInterface extends AInterface {
        @FAnnotation
        void hello();
    }

    public interface CInterface {
    }

    public interface DInterface {
    }

    public static class ParentBean<T, ID> {
    }

    public static class TestBean extends ParentBean<String, Long> {

    }

    public static class TestBean2 extends ParentBean {
    }

    public static class TestBean3 {

    }

    @DAnnotation
    public static class AClass implements DInterface {

        @EAnnotation
        private int afield;

        private int cfield;

        @FAnnotation
        private int dfield;

        @AAnnotation
        public int tfield;

        @AAnnotation
        protected int vfield;

        // not counted as public annotated method
        public void hello2(int i) {

        }

        // counted as public annotated method
        @FAnnotation
        public void hello4(int i) {

        }

        // not counted as public annotated method
        @FAnnotation
        protected void hello5(int i) {

        }

        // not counted as public annotated method
        @FAnnotation
        private void hello6(int i) {

        }

        // not counted as public annotated method, because the child override it
        @FAnnotation
        public void hello7(int i) {

        }

    }

    @BAnnotation
    public static class BClass extends AClass implements CInterface, BInterface {

        @EAnnotation
        private int bfield;

        @EAnnotation
        private int efield;

        @AAnnotation
        public int sfield;

        @AAnnotation
        protected int ufield;

        // counted as public annotated method, BInterface
        @Override
        @EAnnotation
        public void hello() {
            // TODO Auto-generated method stub

        }

        public void hello2(int i) {

        }

        // counted as public annotated method
        @FAnnotation
        public void hello3(int i) {

        }

        // not counted as public annotated method
        @Override
        public void hello7(int i) {

        }

    }
}