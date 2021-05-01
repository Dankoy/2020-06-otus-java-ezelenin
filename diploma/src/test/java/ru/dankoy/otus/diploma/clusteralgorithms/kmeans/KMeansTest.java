package ru.dankoy.otus.diploma.clusteralgorithms.kmeans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import reflection.ReflectionHelper;
import ru.dankoy.otus.diploma.cluster.Cluster;
import ru.dankoy.otus.diploma.core.model.Crash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KMeansTest {

    private static final int ILLEGAL_AMOUNT_OF_CLUSTERS = 20;

    private KMeansImpl kMeans;

    @BeforeEach
    public void setUp() {

        kMeans = new KMeansImpl();

    }


    @Test
    @DisplayName("Проверка, что вернулся список из двух кластеров. Проверяются центры и количество точек в кластере.")
    void clusterTest() throws CloneNotSupportedException {

        List<Crash> crashes = makeCrashesList();

        List<Cluster> clusters = kMeans.cluster(crashes, 2);

        // Количество кластеров
        assertThat(clusters).size().isEqualTo(2);

        // Проверка центра кластера
        assertThat(clusters).extracting(Cluster::getLatitude)
                .containsExactlyElementsOf(List.of(39.05733453454546D,
                        38.989681288D));
        assertThat(clusters).extracting(Cluster::getLongitude)
                .containsExactlyElementsOf(List.of(-77.11479102636365, -77.017009632));

        // Проверка количество элементов в кластере
        assertThat(clusters).extracting(Cluster::getAmountOfPoints)
                .containsExactlyElementsOf(List.of(11L, 5L));

    }


    @Test
    @DisplayName("Проверка, что вернулся список из одного кластера. Проверяются центры и количество точек в кластере.")
    void clusterAmountOfOneTest() throws CloneNotSupportedException {

        List<Crash> crashes = makeCrashesList();

        List<Cluster> clusters = kMeans.cluster(crashes, 1);

        // Количество кластеров
        assertThat(clusters).size().isEqualTo(1);

        // Проверка центра кластера
        assertThat(clusters).extracting(Cluster::getLatitude)
                .containsExactlyElementsOf(List.of(39.036192895D));
        assertThat(clusters).extracting(Cluster::getLongitude)
                .containsExactlyElementsOf(List.of(-77.084234340625));

        // Проверка количество элементов в кластере
        assertThat(clusters).extracting(Cluster::getAmountOfPoints)
                .containsExactlyElementsOf(List.of(16L));

    }

    @Test
    @DisplayName("Проверка, что не может быть кластеров больше, чем количество кластеризуемых событий")
    void clusterAmountOfTwentyTest() throws IllegalArgumentException {

        List<Crash> crashes = makeCrashesList();

        assertThatThrownBy(() -> kMeans.cluster(crashes, ILLEGAL_AMOUNT_OF_CLUSTERS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Expected amount of clusters less or equals amount of accidents, but got "
                                + ILLEGAL_AMOUNT_OF_CLUSTERS);


    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Проверка, что нельзя запустить алгоритм кластеризации, если количество кластеров <= 0")
    void clusterIllegalAmountTest(int ints) throws IllegalArgumentException {

        List<Crash> crashes = makeCrashesList();

        assertThatThrownBy(() -> kMeans.cluster(crashes, ints))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Expected amount of clusters > 0, but got " + ints);

    }

    /**
     * Генерирует список аварий
     *
     * @return список аварий {@link Crash}
     */
    private List<Crash> makeCrashesList() {

        List<Crash> crashes = new ArrayList<>();

        Map<Double, Double> latitudeLongitudeMap = new HashMap<>();
        latitudeLongitudeMap.put(38.99213432D, -77.00987275D);
        latitudeLongitudeMap.put(38.9758395D, -76.99383967D);
        latitudeLongitudeMap.put(38.99448139D, -77.02661747D);
        latitudeLongitudeMap.put(38.99574167D, -77.02811167D);
        latitudeLongitudeMap.put(38.99020956D, -77.0266066D);
        latitudeLongitudeMap.put(39.07294667D, -77.09568333D);
        latitudeLongitudeMap.put(39.05914867D, -77.12140233D);
        latitudeLongitudeMap.put(39.05494107D, -77.11835683D);
        latitudeLongitudeMap.put(39.06675D, -77.12824D);
        latitudeLongitudeMap.put(39.07045167D, -77.11396667D);
        latitudeLongitudeMap.put(39.05103333D, -77.117765D);
        latitudeLongitudeMap.put(39.04659833D, -77.10513333D);
        latitudeLongitudeMap.put(39.05058333D, -77.11898333D);
        latitudeLongitudeMap.put(39.05325548D, -77.13208247D);
        latitudeLongitudeMap.put(39.05386333D, -77.10594167D);
        latitudeLongitudeMap.put(39.051108D, -77.10514633D);

        latitudeLongitudeMap.forEach((latitude, longitude) -> {
            Crash crash = new Crash();
            try {
                ReflectionHelper.setAnyFieldInObject(Crash.class, crash, latitude, "latitude");
                ReflectionHelper.setAnyFieldInObject(Crash.class, crash, longitude, "longitude");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            crashes.add(crash);
        });

        return crashes;
    }

}
