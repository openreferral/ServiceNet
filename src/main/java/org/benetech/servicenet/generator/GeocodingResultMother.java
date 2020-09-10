package org.benetech.servicenet.generator;

import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableMap;
import com.google.maps.model.LocationType;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManager;
import net.logstash.logback.encoder.org.apache.commons.lang3.tuple.Pair;
import org.benetech.servicenet.domain.GeocodingResult;

public class GeocodingResultMother implements BaseMother<GeocodingResult> {

    public static final GeocodingResultMother INSTANCE = new GeocodingResultMother();

    private static final Faker FAKER = FakerInstance.INSTANCE;

    private static final Random RANDOM = new Random();

    private GeocodingResultMother() { }

    public static final Map<String, Pair<Double, Double>> REAL_PLACES_COORDS =
        ImmutableMap.<String, Pair<Double, Double>>builder()
        .put("New York City", Pair.of(40.71427, -74.00597))
        .put("Los Angeles", Pair.of(34.05223, -118.24368))
        .put("Chicago", Pair.of(41.85003, -87.65005))
        .put("Houston", Pair.of(29.76328, -95.36327))
        .put("Philadelphia", Pair.of(39.95233, -75.16379))
        .put("Phoenix", Pair.of(33.44838, -112.07404))
        .put("San Jose", Pair.of(37.33939, -121.89496))
        .put("Austin", Pair.of(30.26715, -97.74306))
        .put("Jacksonville", Pair.of(30.33218, -81.65565))
        .put("San Francisco", Pair.of(37.77493, -122.41942))
        .put("Columbus", Pair.of(39.96118, -82.99879))
        .put("Fort Worth", Pair.of(32.72541, -97.32085))
        .put("Indianapolis", Pair.of(39.76838, -86.15804))
        .build();

    public static final String[] REAL_PLACES_CITIES = REAL_PLACES_COORDS.keySet()
        .toArray(new String[REAL_PLACES_COORDS.size()]);

    @Override
    public GeocodingResult generate(EntityManager em) {
        String city = REAL_PLACES_CITIES[RANDOM.nextInt(REAL_PLACES_COORDS.size())];
        Pair<Double, Double> randomCity = REAL_PLACES_COORDS
            .get(city);
        return GeocodingResult.builder()
            .address(FAKER.address().fullAddress())
            .latitude(randomCity.getLeft())
            .longitude(randomCity.getRight())
            .locality(city)
            .locationType(LocationType.ROOFTOP)
            .streetNumber(FAKER.address().streetAddressNumber())
            .country("United States")
            .postalCode(FAKER.address().zipCode())
            .administrativeAreaLevel1(FAKER.address().state())
            .build();
    }
}
