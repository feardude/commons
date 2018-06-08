package com.feardude.commons;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.feardude.commons.ResourceUtils.readJson;
import static com.feardude.commons.ResourceUtils.readJsonList;
import static com.feardude.commons.ResourceUtils.readJsonMap;
import static com.feardude.commons.ResourceUtils.readJsonMapValuesList;
import static com.feardude.commons.ResourceUtils.readJsonMapValuesMap;
import static com.feardude.commons.ResourceUtils.readResourceAsString;
import static com.feardude.commons.ResourceUtils.writeJsonToTestResources;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class ResourceUtilsTest {
    private static final String TEMP_DIR_PATH = "src/test/resources/temp";

    @AfterClass
    public static void cleanUp() throws IOException {
        FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
    }

    @Test
    public void write_localDate() throws IOException {
        final String expected = "2018-06-07";
        writeJsonToTestResources(LocalDate.parse(expected), "temp/local_date.json");

        final String actual = readFileToString("local_date.json");
        assertEquals(expected, actual);
    }

    @Test
    public void write_localDateTime() throws IOException {
        final String expected = "2018-06-07T01:23:45";
        writeJsonToTestResources(LocalDateTime.parse(expected), "temp/local_date_time.json");

        final String actual = readFileToString("local_date_time.json");
        assertEquals(expected, actual);
    }

    @Test
    public void read_string() {
        final String expected = "some string";
        final String actual = readResourceAsString("resource_utils/read_string.json");
        assertEquals(String.format("\"%s\"", expected), actual);
    }

    @Test
    public void read_object() {
        final TestModel expected = new TestModel(
                1, "model 1", asList(LocalDate.parse("2018-05-05"), LocalDate.parse("2018-06-06"))
        );
        final TestModel actual = readJson("resource_utils/read_object.json", TestModel.class);
        assertEquals(expected, actual);
    }

    @Test
    public void read_list() {
        final List<TestModel> expected = asList(
                new TestModel(1, "model 1", singletonList(LocalDate.parse("2018-05-05"))),
                new TestModel(2, "model 2", singletonList(LocalDate.parse("2018-06-06")))
        );
        final List<TestModel> actual = readJsonList("resource_utils/read_list.json", TestModel.class);
        assertEquals(expected, actual);
    }

    @Test
    public void read_map() {
        final TestModel model1 = new TestModel(1, "model 1", singletonList(LocalDate.parse("2018-05-05")));
        final TestModel model2 = new TestModel(2, "model 2", singletonList(LocalDate.parse("2018-06-06")));

        final Map<Integer, TestModel> expected = new HashMap<>(2);
        expected.put(1, model1);
        expected.put(2, model2);

        final Map<Integer, TestModel> actual = readJsonMap("resource_utils/read_map.json", Integer.class, TestModel.class);
        assertEquals(expected, actual);
    }

    @Test
    public void read_mapValuesList() {
        final Map<Integer, List<String>> expected = new HashMap<>(2);
        expected.put(1, asList("s10", "s11"));
        expected.put(2, asList("s20", "s21"));

        final Map<Integer, List<String>> actual = readJsonMapValuesList(
                "resource_utils/read_map_values_list.json", Integer.class, String.class
        );
        assertEquals(expected, actual);
    }

    @Test
    public void read_mapValuesInnerMap() {
        final Map<String, LocalTime> innerMap = new HashMap<>(1);
        innerMap.put("s1", LocalTime.parse("01:23:45"));

        final Map<Integer, Map<String, LocalTime>> expected = new HashMap<>(1);
        expected.put(1, innerMap);

        final Map<Integer, Map<String, LocalTime>> actual = readJsonMapValuesMap(
                "resource_utils/read_map_values_inner_map.json",
                Integer.class, String.class, LocalTime.class
        );
        assertEquals(expected, actual);
    }


    private static String readFileToString(String fileName) throws IOException {
        final String pathname = String.format("%s/%s", TEMP_DIR_PATH, fileName);
        return FileUtils.readFileToString(new File(pathname), UTF_8)
                .replaceAll("\"", "");
    }
}
