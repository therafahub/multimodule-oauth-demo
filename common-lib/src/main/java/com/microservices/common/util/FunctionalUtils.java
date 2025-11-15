package com.microservices.common.util;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

/**
 * Utilidades funcionales usando Vavr
 */
public final class FunctionalUtils {

    private FunctionalUtils() {
        throw new AssertionError("No instances");
    }

    /**
     * Ejecuta una operación y retorna Either con éxito o error
     */
    public static <T, E extends Throwable> Either<E, T> attempt(
            FunctionalUtils.FunctionalSupplier<T, E> supplier) {
        try {
            return Either.right(supplier.get());
        } catch (Throwable e) {
            return Either.left((E) e);
        }
    }

    /**
     * Transforma una operación que puede fallar
     */
    public static <T> Option<T> attemptOption(FunctionalUtils.VoidSupplier<T> supplier) {
        try {
            return Option.of(supplier.get());
        } catch (Exception e) {
            return Option.none();
        }
    }

    @FunctionalInterface
    public interface FunctionalSupplier<T, E extends Throwable> {
        T get() throws E;
    }

    @FunctionalInterface
    public interface VoidSupplier<T> {
        T get() throws Exception;
    }
}
