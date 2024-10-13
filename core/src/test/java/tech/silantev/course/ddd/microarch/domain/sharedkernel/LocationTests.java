package tech.silantev.course.ddd.microarch.domain.sharedkernel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tech.silantev.course.ddd.microarch.structure.Result;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationTests {

    @ParameterizedTest
    @MethodSource("invalidCoordinatesLessThanMinimum")
    public void coordinatesLessThanMinimumShouldThrowException(int x, int y) {
        // given x and y
        // when
        Executable sut = () -> new Location(x, y);
        // then
        assertThrows(IllegalArgumentException.class, sut);
    }

    @ParameterizedTest
    @MethodSource("invalidCoordinatesMoreThanMaximum")
    public void coordinatesMoreThanMaximumShouldThrowException(int x, int y) {
        // given x and y
        // when
        Executable sut = () -> new Location(x, y);
        // then
        assertThrows(IllegalArgumentException.class, sut);
    }

    @ParameterizedTest
    @MethodSource("validCoordinates")
    public void locationsAreEqualIfCoordinatesEqual(int x, int y) {
        // given
        var locationA = new Location(x, y);
        var locationB = new Location(x, y);
        // when
        boolean equals = locationA.equals(locationB);
        // then
        assertTrue(equals);
    }

    @Test
    public void checkDistanceCalculation() {
        // given
        var locationA = new Location(9, 1);
        var locationB = new Location(1, 4);
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

    @ParameterizedTest
    @MethodSource("validCoordinates")
    public void creationMethodShouldReturnSuccessIfValidCoordinatesUsed(int x, int y) {
        // given invalid coordinates x and y
        // when
        Result<Location> result = Location.create(x, y);
        // then
        assertTrue(result.success());
    }

    @ParameterizedTest
    @MethodSource("invalidCoordinatesLessThanMinimum")
    public void creationMethodShouldReturnFailureIfInvalidCoordinatesUsed(int x, int y) {
        // given invalid coordinates x and y
        // when
        Result<Location> result = Location.create(x, y);
        // then
        assertTrue(result.failure());
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