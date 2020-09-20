package ru.dankoy.otus.serialization;

import ru.dankoy.otus.serialization.guineapig.GuineaPig;
import ru.dankoy.otus.serialization.serializer.Serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        GuineaPig guineaPig =
                GuineaPig.newBuilder()
                        .setAge(10)
                        .setName("name")
                        .setSomeList(List.of("str1", "str2"))
                        .setSomeList2(List.of(4, 6))
                        .setSomeMap(new HashMap<>())
                        .setBool(true)
                        .setBool2(false)
                        .build();

        GuineaPig guineaPig2 =
                GuineaPig.newBuilder()
                        .setAge(15)
                        .setName("name2")
                        .setSomeList(List.of("str123", "str243"))
                        .setBool(true)
                        .setBool2(false)
                        .build();

        Serializer serializer = new Serializer();

        serializer.serializeObject(guineaPig);
        serializer.serializeObject(guineaPig2);

    }

}
