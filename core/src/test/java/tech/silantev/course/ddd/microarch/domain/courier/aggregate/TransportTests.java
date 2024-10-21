package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransportTests {

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

    public static Stream<String> validTransportNames() {
        return Transport.list().stream().map(Transport::getName);
    }

    public static Stream<String> invalidTransportNames() {
        return Stream.generate(UUID::randomUUID).map(UUID::toString).limit(3);
    }
}