package tech.silantev.course.ddd.microarch.structure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ResultTests {

    @Test
    public void testSuccessResultCreation() {
        // given
        var value = "anyString";
        // when
        var result = Result.createSuccess(value);
        // then
        assertTrue(result.success());
        assertEquals(Optional.of("anyString"), result.value());
    }

    @Test
    public void testFailureResultCreation() {
        // given
        var exception = new Exception("anyString");
        // when
        var result = Result.createFailure(exception);
        // then
        assertTrue(result.failure());
        assertEquals(Optional.of(exception), result.exception());
    }

    @ParameterizedTest
    @MethodSource("equalResults")
    public void resultsAreEqualIfValuesEqual(Result<?> resultA, Result<?> resultB) {
        // given resultA and resultB
        // when
        boolean equals = resultA.equals(resultB);
        // then
        assertTrue(equals);
    }


    public static Stream<Arguments> equalResults() {
        return Stream.of(Arguments.of(Result.createSuccess("anyString"), Result.createSuccess("anyString")),
                Arguments.of(Result.createFailure(new Exception()), Result.createFailure(new Exception())));
    }
}