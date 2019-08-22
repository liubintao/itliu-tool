package com.robust.tools.kit.mapper;

import com.google.common.collect.Maps;
import com.robust.tools.kit.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/16 14:06
 * @Version: 1.0
 */
public class JsonMapperTest {

    @Test
    public void toJson() {
        JsonMapper mapper = JsonMapper.INSTANCE;
        String s = "{\"name\":\"A\",\"age\":1}";
        Person p = mapper.fromJson(s, Person.class);
        assertThat(p.getAge()).isEqualTo(1);
        assertThat(p.getName()).isEqualTo("A");
        assertThat(mapper.toJson(p)).isEqualTo(s);

        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put("a", 1);
        map.put("b", "a");
        assertThat(mapper.toJson(map)).isEqualTo("{\"a\":1,\"b\":\"a\"}");

        List<String> list = ListUtil.newArrayList("A", "B", "C");
        assertThat(mapper.toJson(list)).isEqualTo("[\"A\",\"B\",\"C\"]");

        List<Person> list1 = ListUtil.newArrayList(new Person("A", 1), new Person("B", 2));
        assertThat(mapper.toJson(list1)).isEqualTo("[{\"name\":\"A\",\"age\":1},{\"name\":\"B\",\"age\":2}]");
    }

    @Test
    public void fromJson() {
        String s = "{\"name\":\"A\",\"age\":1}";
        Person p = JsonMapper.defaultMapper().fromJson(s, Person.class);
        System.out.println(p);

        s = "{\"a\":1,\"b\":\"a\"}";
        Map map = JsonMapper.INSTANCE.fromJson(s, Map.class);
        System.out.println(map);

        s = "[\"A\",\"B\",\"C\"]";
        List<String> list = JsonMapper.INSTANCE.fromJson(s, List.class);
        System.out.println(list);

        s = "[{\"name\":\"A\",\"age\":1},{\"name\":\"B\",\"age\":2}]";
        List<Person> list1 = JsonMapper.INSTANCE.fromJson(s, List.class);
        System.out.println(list1);
    }

    @Test
    public void nonNull() {
        Person p = new Person(null,  1);
        System.out.println(JsonMapper.nonNullMapper().toJson(p));

        p = new Person("", 2);
        System.out.println(JsonMapper.nonEmptyMapper().toJson(p));
        System.out.println(JsonMapper.nonNullMapper().toJson(p));
    }

    @Test
    public void fromJsonByType() {
        String s = "[{\"name\":\"A\",\"age\":1},{\"name\":\"B\",\"age\":2}]";
        JsonMapper mapper = JsonMapper.defaultMapper();
        List<Person> list = mapper.fromJson(s, mapper.buildCollectionType(List.class, Person.class));
        System.out.println(list);

        s = "{\"a\":1,\"b\":\"a\"}";
        Map<String, Object> map = mapper.fromJson(s, mapper.buildMapType(Map.class, String.class, Object.class));
        System.out.println(map);

        s = "[\"A\",\"B\",\"C\"]";
        String[] array = mapper.fromJson(s, mapper.buildArrayType(String.class));
        Arrays.stream(array).forEach(System.out::println);
    }

    @Test
    public void update() {
        String s = "{\"name\":\"A\"}";
        Person p = new Person("zhang", 5);
        JsonMapper.defaultMapper().update(s, p);
        s = "{\"age\":6}";
        JsonMapper.defaultMapper().update(s, p);
        System.out.println(p);
    }

    @Test
    public void jsonP() {
        Person p = new Person("A", 1);
        String jsonP = JsonMapper.nonEmptyMapper().toJsonP("hello", p);
        assertThat(jsonP).isEqualTo("hello({\"name\":\"A\",\"age\":1})");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Person {
        String name;
        int age;
    }
}