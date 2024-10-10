package tech.silantev.course.ddd.microarch.domain.sharedkernel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationTests {

    @ParameterizedTest
    @CsvSource({"0, 0", "-100, 2"})
    public void coordinatesLessThanMinimumShouldThrowException(int x, int y) {
        // given x and y
        // when
        Executable sut = () -> new Location(x, y);
        // then
        assertThrows(IllegalArgumentException.class, sut);
    }

    @ParameterizedTest
    @CsvSource({"11, 11", "5, 100"})
    public void coordinatesMoreThanMaximumShouldThrowException(int x, int y) {
        // given x and y
        // when
        Executable sut = () -> new Location(x, y);
        // then
        assertThrows(IllegalArgumentException.class, sut);
    }

    @ParameterizedTest
    @CsvSource({"7, 4"})
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
    public void checkRandomCoordinatesLocation() {
        // given method for random location creation
        // when
        var location = Location.random();
        // then
        assertNotNull(location);
    }
}