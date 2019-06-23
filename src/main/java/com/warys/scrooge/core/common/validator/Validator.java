package com.warys.scrooge.core.common.validator;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Validator<T> {

    private final T validatable;

    private final List<Throwable> exceptions = new ArrayList<>();

    Validator(T validatable) {
        this.validatable = validatable;
    }

    public static <T> Validator<T> of(T validatable) {
        return new Validator<>(Objects.requireNonNull(validatable));
    }

    private Validator<T> validate(Predicate<T> validation, String message) {
        try {
            if (!validation.test(validatable)) {
                exceptions.add(new ValidationException(message));
            }
        } catch (Exception e){
            exceptions.add(new ValidationException(e.getMessage()));
        }

        return this;
    }

    <U> Validator<T> validate(Function<T, U> projection, Predicate<U> validation,
                              String message) {
        return validate(projection.andThen(validation::test)::apply, message);
    }

    T get() {
        if (exceptions.isEmpty()) {
            return validatable;
        }
        ValidationException e = new ValidationException();
        exceptions.forEach(e::addSuppressed);
        throw e;
    }

}