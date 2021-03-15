package ru.dankoy.otus.serialization;

import com.google.gson.Gson;
import ru.dankoy.otus.serialization.guineapig.GuineaPig;
import ru.dankoy.otus.serialization.serializer.Serializer;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        GuineaPig guineaPig =
                GuineaPig.newBuilder()
                        .setAge(10)
                        .setName("name")
                        .setSomeList(List.of("str1", "str2"))
                        .setSomeList2(List.of(4, 6))
                        .setSomeList3(List.of(4, "234", false, 110.43))
                        .setBool(true)
                        .setBool2(false)
                        .setIntArray(new int[]{1, 2, 3})
                        .build();


        Serializer serializer = new Serializer();

        String serializedPig = serializer.serializeObject(guineaPig);
        System.out.println(serializedPig);

        Gson gson = new Gson();
        System.out.println(gson.toJson(guineaPig));

    }

}
