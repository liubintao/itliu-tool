package com.robust.tools.kit.mapper;

import com.robust.tools.kit.collection.ArrayUtil;
import com.robust.tools.kit.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/15 17:22
 * @Version: 1.0
 */
public class BeanMapperTest {

    @Test
    public void test() {
        Student s = Student.builder()
                .name("zh")
                .age(1)
                .lessons(ListUtil.newArrayList("chinese", "english"))
                .build();
        StudentVO s1 = BeanMapper.map(s, StudentVO.class);
        assertThat(s.getName()).isEqualTo(s1.getName());
        assertThat(s.getAge() == s1.getAge()).isTrue();
        assertThat(s.getLessons()).containsExactly("chinese", "english");
    }

    @Test
    public void copyList() {
        Student s1 = Student.builder()
                .name("zh")
                .age(1)
                .lessons(ListUtil.newArrayList("chinese", "english"))
                .build();
        Student s2 = Student.builder()
                .name("li")
                .age(2)
                .build();
        Student s3 = Student.builder()
                .name("wa")
                .age(3)
                .build();
        List<Student> students = ListUtil.newArrayList(s1, s2, s3);
        List<StudentVO> studentVOS = BeanMapper.mapList(students, StudentVO.class);
        assertThat(studentVOS).hasSize(3);

        StudentVO vo = studentVOS.get(0);
        assertThat(vo.getAge()).isEqualTo(1);
        assertThat(vo.getName()).isEqualTo("zh");
        assertThat(vo.getLessons()).containsExactly("chinese", "english");

        Teacher teacher = Teacher.builder()
                .age(30)
                .name("wang")
                .studentList(ListUtil.newArrayList(s1,s2,s3))
                .build();

        TeacherVO teacherVO = BeanMapper.map(teacher, TeacherVO.class);
        assertThat(teacherVO.getAge()).isEqualTo(30);
        assertThat(teacherVO.getName()).isEqualTo("wang");
        assertThat(teacherVO.getStudentList().get(0).getName()).isEqualTo("zh");
        assertThat(teacherVO.getStudentList().get(0).getLessons()).containsExactly("chinese", "english");
    }

    @Test
    public void mapArray() {
        Student s1 = Student.builder()
                .name("zh")
                .age(1)
                .lessons(ListUtil.newArrayList("chinese", "english"))
                .build();
        Student s2 = Student.builder()
                .name("li")
                .age(2)
                .lessons(ListUtil.newArrayList("physics", "english"))
                .build();
        Student s3 = Student.builder()
                .name("wa")
                .age(3)
                .build();

        Student[] s = new Student[]{s1,s2,s3};
        StudentVO[] vos = BeanMapper.mapArray(s, StudentVO.class);

        assertThat(vos).hasSize(3);

        StudentVO vo = vos[0];
        assertThat(vo.getAge()).isEqualTo(1);
        assertThat(vo.getName()).isEqualTo("zh");
        assertThat(vo.getLessons()).containsExactly("chinese", "english");
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Teacher {
        String name;
        int age;
        List<Student> studentList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TeacherVO {
        String name;
        int age;
        List<StudentVO> studentList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Student {
        String name;
        int age;
        List<String> lessons;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class StudentVO {
        String name;
        int age;
        List<String> lessons;
    }
}