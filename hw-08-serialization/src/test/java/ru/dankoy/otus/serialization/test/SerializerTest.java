package ru.dankoy.otus.serialization.test;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import ru.dankoy.otus.serialization.guineapig.GuineaPig;
import ru.dankoy.otus.serialization.serializer.Serializer;

import java.io.IOException;
import java.util.List;

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

}
