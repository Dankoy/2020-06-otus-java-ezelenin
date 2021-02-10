package ru.dankoy.otus.warmvc.web.serializator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

/**
 * Кастомный gson сериализатор, который игнорирует все поля, кроме тех, что помечены Exposed.
 * Это нужно так как gson спринга делает формирует json при сериализации User объекта.
 */
@Component
public class CustomJsonSerializer {

    private final Gson gson;

    public CustomJsonSerializer() {

        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

    }

    public Gson getGson() {
        return this.gson;
    }

}
