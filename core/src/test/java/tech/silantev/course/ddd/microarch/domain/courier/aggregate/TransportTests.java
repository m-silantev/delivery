package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransportTests {

    public static Stream<Transport> validTransports() {
        return Transport.list().stream();
    }

    public static Stream<String> validTransportNames() {
        return validTransports().map(Transport::getName);
    }

    public static Stream<String> invalidTransportNames() {
        return Stream.generate(UUID::randomUUID).map(UUID::toString).limit(3);
    }

    @Test
    public void twoTransportsAreEqualIfTheirIdsEqual() {
        // given
        Transport transportA = Transport.fromId(1).discardError().get();
        Transport transportB = Transport.fromId(1).discardError().get();
        // when
        boolean equals = transportA.equals(transportB);
        // then
        assertTrue(equals);
    }

    @ParameterizedTest
    @MethodSource("validTransportNames")
    public void validNameShouldCreateResultWithValidTransport(String name) {
        // given valid name
        // when
        Result<Transport, String> result = Transport.fromName(name);
        // then
        assertFalse(result.isError());
    }

    @ParameterizedTest
    @MethodSource("invalidTransportNames")
    public void invalidNameShouldCreateErrorResult(String name) {
        // given invalid name
        // when
        Result<Transport, String> result = Transport.fromName(name);
        // then
        assertTrue(result.isError());
    }

    @ParameterizedTest
    @MethodSource("validTransports")
    public void movePreferablyByXMethodShouldChangeOnlyXCoordinateOnAStraightRoads(Transport transport) {
        // given
        Location from = Location.create(1, 1);
        Location to = Location.create(10, 10);
        Location expected = Location.create(from.getX() + transport.getSpeed(), from.getY());
        // when
        Location location = transport.movePreferablyByX(from, to);
        // then
        assertEquals(expected, location);
    }

    @ParameterizedTest
    @MethodSource("validTransports")
    public void movePreferablyByYMethodShouldChangeOnlyYCoordinateOnAStraightRoads(Transport transport) {
        // given
        Location from = Location.create(1, 1);
        Location to = Location.create(10, 10);
        Location expected = Location.create(from.getX(), from.getY() + transport.getSpeed());
        // when
        Location location = transport.movePreferablyByY(from, to);
        // then
        assertEquals(expected, location);
    }

    @ParameterizedTest
    @MethodSource("validTransports")
    public void movePreferablyByXMethodShouldChangeOnlyXCoordinateWhenMovingBackwardOnAStraightRoads(Transport transport) {
        // given
        Location from = Location.create(10, 10);
        Location to = Location.create(1, 1);
        Location expected = Location.create(from.getX() - transport.getSpeed(), from.getY());
        // when
        Location location = transport.movePreferablyByX(from, to);
        // then
        assertEquals(expected, location);
    }

    @ParameterizedTest
    @MethodSource("validTransports")
    public void movePreferablyByYMethodShouldChangeOnlyYCoordinateWhenMovingBackwardOnAStraightRoads(Transport transport) {
        // given
        Location from = Location.create(10, 10);
        Location to = Location.create(1, 1);
        Location expected = Location.create(from.getX(), from.getY() - transport.getSpeed());
        // when
        Location location = transport.movePreferablyByY(from, to);
        // then
        assertEquals(expected, location);
    }

    @ParameterizedTest
    @MethodSource("validTransports")
    public void movePreferablyByXMethodShouldChangeYCoordinateIfXIsOver(Transport transport) {
        // given
        Location from = Location.create(1, 1);
        Location to = Location.create(1, 10);
        Location expected = Location.create(from.getX(), from.getY() + transport.getSpeed());
        // when
        Location location = transport.movePreferablyByX(from, to);
        // then
        assertEquals(expected, location);
    }

    @ParameterizedTest
    @MethodSource("validTransports")
    public void movePreferablyByYMethodShouldChangeXCoordinateIfYIsOver(Transport transport) {
        // given
        Location from = Location.create(1, 1);
        Location to = Location.create(10, 1);
        Location expected = Location.create(from.getX() + transport.getSpeed(), from.getY());
        // when
        Location location = transport.movePreferablyByY(from, to);
        // then
        assertEquals(expected, location);
    }
}