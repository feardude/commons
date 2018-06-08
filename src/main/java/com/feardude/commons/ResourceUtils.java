package com.feardude.commons;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class ResourceUtils {
    private static final ClassLoader CLASS_LOADER = ResourceUtils.class.getClassLoader();
    private static final ObjectMapper MAPPER = createObjectMapper();

    private ResourceUtils() {
        throw new AssertionError("Not for instantiation!");
    }

    public static <T> T readJson(String filePath, Class<T> clazz) {
        try {
            return MAPPER.readValue(getResourceURL(filePath), clazz);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static <T> List<T> readJsonList(String filePath, Class<T> listElementClass) {
        final JavaType type = MAPPER.getTypeFactory()
                .constructCollectionType(ArrayList.class, listElementClass);

        try {
            return MAPPER.readValue(getResourceURL(filePath), type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <K, V> Map<K, V> readJsonMap(String filePath, Class<K> keyClass, Class<V> valueClass) {
        final JavaType type = MAPPER.getTypeFactory()
                .constructMapType(HashMap.class, keyClass, valueClass);

        try {
            return MAPPER.readValue(getResourceURL(filePath), type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <K, V> Map<K, List<V>> readJsonMapValuesList(String filePath, Class<K> keyClass, Class<V> valuesListElementClass) {
        final JavaType keyType = MAPPER.constructType(keyClass);
        final JavaType valueType = MAPPER.getTypeFactory()
                .constructCollectionType(ArrayList.class, valuesListElementClass);
        final JavaType mapType = MAPPER.getTypeFactory()
                .constructMapType(HashMap.class, keyType, valueType);

        try {
            return MAPPER.readValue(getResourceURL(filePath), mapType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <K, K1, V1> Map<K, Map<K1, V1>> readJsonMapValuesMap(String filePath, Class<K> keyClass,
                                                                       Class<K1> innerMapKeyClass, Class<V1> innerMapValueClass) {
        final JavaType innerKeyType = MAPPER.getTypeFactory()
                .constructType(innerMapKeyClass);
        final JavaType innerValueType = MAPPER.getTypeFactory()
                .constructType(innerMapValueClass);
        final JavaType valueType = MAPPER.getTypeFactory()
                .constructMapType(HashMap.class, innerKeyType, innerValueType);

        final JavaType keyType = MAPPER.constructType(keyClass);
        final JavaType mapType = MAPPER.getTypeFactory()
                .constructMapType(HashMap.class, keyType, valueType);

        try {
            return MAPPER.readValue(getResourceURL(filePath), mapType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String readResourceAsString(String filePath) {
        try {
            return IOUtils.toString(CLASS_LOADER.getResourceAsStream(filePath), UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeJsonToTestResources(Object obj, String filePath) {
        try {
            final String s = MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(obj);
            FileUtils.writeStringToFile(
                    new File("src/test/resources/" + filePath),
                    s, UTF_8
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        return mapper;
    }

    private static URL getResourceURL(String filePath) {
        return CLASS_LOADER.getResource(filePath);
    }
}
