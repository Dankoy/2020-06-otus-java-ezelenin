package ru.dankoy.otus.serialization.test;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.dankoy.otus.serialization.guineapig.GuineaPig;
import ru.dankoy.otus.serialization.serializer.Serializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SerializerTest {

    GuineaPig guineaPig;
    String guienaPigSerializedWithGson;

    @BeforeEach
    public void setUp() {

        guineaPig =
                GuineaPig.newBuilder()
                        .setAge(10)
                        .setName("name")
                        .setSomeList(List.of("str1", "str2"))
                        .setSomeList2(List.of(4, 6))
                        .setSomeList3(List.of(4, "234", false, 110.43))
                        .setBool(true)
                        .setBool2(false)
                        .build();

        Gson gson = new Gson();
        guienaPigSerializedWithGson = gson.toJson(guineaPig);

    }

    @Test
    @DisplayName("Проверка, что строки, составленные кастомным сериализатором и GSON совпадают")
    public void serializerTest() throws IOException {

        Serializer serializer = new Serializer();

        String serializedPig = serializer.serializeObject(guineaPig);

        assertThat(serializedPig).isEqualTo(guienaPigSerializedWithGson);

    }

    @ParameterizedTest
    @MethodSource("generateDataForCustomTest")
    void customTest(Object o) throws IOException {
        Gson gson = new Gson();
        Serializer serializer = new Serializer();
        System.out.println(serializer.serializeObject(o));
        assertThat(serializer.serializeObject(o)).isEqualTo(gson.toJson(o));
    }

    private static Stream<Arguments> generateDataForCustomTest() {
        return Stream.of(
                null,
                Arguments.of(true), Arguments.of(false),
                Arguments.of((byte) 1), Arguments.of((short) 2f),
                Arguments.of(3), Arguments.of(4L), Arguments.of(5f), Arguments.of(6d),
                Arguments.of("aaa"), Arguments.of('b'),
                Arguments.of(new byte[]{1, 2, 3}),
                Arguments.of(new short[]{4, 5, 6}),
                Arguments.of(new int[]{7, 8, 9}),
                Arguments.of(new float[]{10f, 11f, 12f}),
                Arguments.of(new double[]{13d, 14d, 15d}),
                Arguments.of(List.of(16, 17, 18)),
                Arguments.of(Collections.singletonList(19))
        );
    }

}
