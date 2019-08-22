package com.robust.tools.kit.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.robust.tools.kit.base.annotation.NotNull;
import com.robust.tools.kit.text.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @Description: 简单封装Jackson，实现JSON String<->Java Object转换的Mapper.
 * <p>
 * 可以直接使用公共示例JsonMapper.INSTANCE, 也可以使用不同的builder函数创建实例，封装不同的输出风格,
 * <p>
 * 不要使用GSON, 在对象稍大时非常缓慢.
 * @Author: robust
 * @CreateDate: 2019/8/16 9:19
 * @Version: 1.0
 */
@Slf4j
public class JsonMapper {
    public static final JsonMapper INSTANCE = new JsonMapper();

    private ObjectMapper mapper;

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(JsonInclude.Include include) {
        mapper = new ObjectMapper();
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }
        //设置输入时忽略在JSON字符串中存在但Java对象中实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 默认全部输出的Mapper,区别于INSTANCE,可以进一步配置
     */
    public static JsonMapper defaultMapper() {
        return new JsonMapper();
    }

    /**
     * 创建只输出非Null属性到Json字符串的Mapper
     */
    public static JsonMapper nonNullMapper() {
        return new JsonMapper(JsonInclude.Include.NON_NULL);
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper
     * 注意: 要小心使用, 特别留意empty的情况.
     */
    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(JsonInclude.Include.NON_EMPTY);
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>
     * <p>
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需反序列化复杂Collection如List<MyBean>, 请使用{@link #fromJson(String, JavaType)}
     *
     * @param jsonString json格式字符串
     * @param clz        目标对象Class
     * @param <T>        目标对象Class类型
     * @return 目标对象实例
     */
    public <T> T fromJson(@NotNull String jsonString, Class<T> clz) {
        if (StringUtil.isBlank(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clz);
        } catch (IOException e) {
            log.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, contructCollectionType()或contructMapType()构造类型, 然后调用本函数.
     */
    public <T> T fromJson(@NotNull String jsonString, JavaType type) {
        if (StringUtil.isBlank(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, type);
        } catch (IOException e) {
            log.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 构造Collection类型
     */
    public JavaType buildCollectionType(Class<? extends Collection> collectionClz, Class<?> elementClz) {
        return mapper.getTypeFactory().constructCollectionType(collectionClz, elementClz);
    }

    /**
     * 构造Map类型
     */
    public JavaType buildMapType(Class<? extends Map> mapClz, Class<?> keyClz, Class<?> valueClz) {
        return mapper.getTypeFactory().constructMapType(mapClz, keyClz, valueClz);
    }

    /**
     * 构造Array类型
     */
    public JavaType buildArrayType(Class<?> elementClz) {
        return mapper.getTypeFactory().constructArrayType(elementClz);
    }

    /**
     * 当JSON里只含有Bean的部分属性时,更新一个已存在Bean,只覆盖该部分的属性.
     */
    public void update(String jsonString, Object object) {
        try {
            mapper.readerForUpdating(object).readValue(jsonString);
        } catch (IOException e) {
            log.warn("update json string :" + jsonString + " to object:" + object, e);
        }
    }

    /**
     * 输出JsonP格式数据
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 设定是否使用Enum的toString函数来读写Enum,为false时使用name()函数来读写Enum,默认为false
     * <p>
     * 注意:本函数一定要在Mapper创建后,一切读写动作之前调用
     */
    public void enableEnumUseToString() {
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}
