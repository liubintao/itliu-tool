package com.robust.tools.kit.mapper;

import com.robust.tools.kit.base.ExceptionUtil;
import com.robust.tools.kit.base.Validate;
import com.robust.tools.kit.reflect.ClassUtil;
import com.robust.tools.kit.text.StringUtil;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 使用Jaxb2.0实现XML<->Java Object的Mapper.
 * <p>
 * 在创建时需要设定所有需要序列化的Root对象的Class.
 * <p>
 * 特别支持Root对象是Collection的情形.
 * @Author: robust
 * @CreateDate: 2019/8/16 15:28
 * @Version: 1.0
 */
public class XmlMapper {

    private static ConcurrentHashMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap<>();

    /**
     * Java Object -> Xml without encoding
     */
    public static String toXml(Object root) {
        return toXml(root, null);
    }

    /**
     * Java Object -> Xml with encoding
     */
    public static String toXml(Object root, String encoding) {
        Class clz = ClassUtil.unWrapCglib(root);
        return toXml(root, clz, encoding);
    }

    /**
     * Java Object -> Xml with encoding
     */
    public static String toXml(Object root, Class clz, String encoding) {
        StringWriter writer = new StringWriter();
        try {
            createMarshaller(clz, encoding).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    /**
     * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形.
     */
    public static String toXml(Collection<?> root, String rootName, Class clz) {
        return toXml(root, rootName, clz, null);
    }

    public static String toXml(Collection<?> root, String rootName, Class clz, String encoding) {
        CollectionWrapper wrapper = new CollectionWrapper();
        wrapper.collection = root;

        JAXBElement<CollectionWrapper> jaxbElement = new JAXBElement<>(new QName(rootName),
                CollectionWrapper.class, wrapper);
        StringWriter writer = new StringWriter();
        try {
            createMarshaller(clz, encoding).marshal(jaxbElement, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    /**
     * Xml->Java Object.
     */
    public static <T> T fromXml(String xml, Class<T> clz) {
        StringReader reader = new StringReader(xml);
        try {
            return (T) createUnmarshaller(clz).unmarshal(reader);
        } catch (JAXBException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    /**
     * 创建Marshaller并设定encoding(可为null).
     * 线程不安全，需要每次创建或pooling。
     */
    private static Marshaller createMarshaller(Class clz, String encoding) {
        JAXBContext jaxbContext = getJaxbContext(clz);
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            if (StringUtil.isNotBlank(encoding)) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            }
            return marshaller;
        } catch (JAXBException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    /**
     * 创建UnMarshaller.
     * 线程不安全，需要每次创建或pooling。
     */
    public static Unmarshaller createUnmarshaller(Class clz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clz);
            return jaxbContext.createUnmarshaller();
        } catch (Exception e) {
            throw ExceptionUtil.unchecked(e);
        }

    }

    protected static JAXBContext getJaxbContext(Class clz) {
        Validate.notNull(clz, "class must not be null");
        JAXBContext jaxbContext = jaxbContexts.get(clz);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clz, CollectionWrapper.class);
                jaxbContexts.put(clz, jaxbContext);
            } catch (JAXBException e) {
                throw new RuntimeException(
                        "Could not instantiate JAXBContext for class [" + clz + "]: " + e.getMessage(), e);
            }
        }
        return jaxbContext;
    }


    /**
     * 封装Root Element是Collection的情况.
     */
    public static class CollectionWrapper {

        @XmlAnyElement
        protected Collection<?> collection;
    }
}
