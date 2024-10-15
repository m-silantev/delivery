package tech.silantev.course.ddd.microarch.domain.sharedkernel;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationTests {

    @ParameterizedTest
    @MethodSource("invalidCoordinatesLessThanMinimum")
    public void coordinatesLessThanMinimumShouldThrowException(int x, int y) {
        // given x and y
        // when
        Executable sut = () -> Location.create(x, y);
        // then
        assertThrows(IllegalArgumentException.class, sut);
    }

    @ParameterizedTest
    @MethodSource("invalidCoordinatesLessThanMinimum")
    public void coordinatesLessThanMinimumShouldReturnError(int x, int y) {
        // given x and y
        // when
        Result<Location, String> result = Location.createSafe(x, y);
        // then
        assertTrue(result.isError());
    }

    @ParameterizedTest
    @MethodSource("invalidCoordinatesMoreThanMaximum")
    public void coordinatesMoreThanMaximumShouldThrowException(int x, int y) {
        // given x and y
        // when
        Executable sut = () -> Location.create(x, y);
        // then
        assertThrows(IllegalArgumentException.class, sut);
    }

    @ParameterizedTest
    @MethodSource("invalidCoordinatesMoreThanMaximum")
    public void coordinatesMoreThanMaximumShouldReturnError(int x, int y) {
        // given x and y
        // when
        Result<Location, String> result = Location.createSafe(x, y);
        // then
        assertTrue(result.isError());
    }

    @ParameterizedTest
    @MethodSource("validCoordinates")
    public void validCoordinatesShouldReturnSuccess(int x, int y) {
        // given invalid coordinates x and y
        // when
        Result<Location, String> result = Location.createSafe(x, y);
        // then
        assertFalse(result.isError());
    }

    @ParameterizedTest
    @MethodSource("validCoordinates")
    public void locationsAreEqualIfCoordinatesEqual(int x, int y) {
        // given
        var locationA = Location.create(x, y);
        var locationB = Location.create(x, y);
        // when
        boolean equals = locationA.equals(locationB);
        // then
        assertTrue(equals);
    }

    @Test
    public void checkDistanceCalculation() {
        // given
        var locationA = Location.create(9, 1);
        var locationB = Location.create(1, 4);
        // when
        int distance = locationA.distanceBetween(locationB);
        // then
        assertEquals(11, distance, "Distance between two locations calculated incorrectly!");
    }

    @Test
    public void checkRandomCoordinatesLocationCreation() {
        // given method for random location creation
        // when
        Location location = Location.createRandom();
        // then
        assertNotNull(location);
    }

    public static Stream<Arguments> invalidCoordinatesLessThanMinimum() {
        return Stream.of(Arguments.of(0, 0), Arguments.of(-100, 2));
    }

    public static Stream<Arguments> invalidCoordinatesMoreThanMaximum() {
        return Stream.of(Arguments.of(11, 11), Arguments.of(5, 100));
    }

    public static Stream<Arguments> validCoordinates() {
        return Stream.of(Arguments.of(7, 4), Arguments.of(2, 3));
    }
}