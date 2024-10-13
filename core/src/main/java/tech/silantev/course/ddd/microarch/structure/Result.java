package tech.silantev.course.ddd.microarch.structure;

import java.util.Objects;
import java.util.Optional;

public class Result<T> {

    private final T value;
    private final Exception exception;

    private Result(T value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }

    public static <T> Result<T> createSuccess(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> createFailure(Exception exception) {
        return new Result<>(null, exception);
    }

    public boolean success() {
        return value != null;
    }

    public boolean failure() {
        return !success();
    }

    public Optional<T> value() {
        return Optional.ofNullable(value);
    }

    public Optional<Exception> exception() {
        return Optional.ofNullable(exception);
    }

    @Override
    public String toString() {
        return "Result{" +
                (success() ? "value=" + value : "exception=" + exception) +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Result<?> result = (Result<?>) object;
        return Objects.equals(value, result.value) && Objects.equals(exception, result.exception);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, exception);
    }
}
