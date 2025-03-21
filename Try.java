package br.com.vtspp.github;

import java.util.Objects;
import java.util.function.Function;

public final class Try<T> implements StatelessOperation<T> {

    private Function<? super Throwable, ?> tryCatch;
    private final T value;

    private Try(T value, Function<? super Throwable, ?> tryCatch) {
        this.value = value;
        this.tryCatch = tryCatch;
    }

    public static <T> Try<T> of(T value) {
        return new Try<>(value, null);
    }

    public static <T> Try<T> of(T value, Function<? super Throwable, ?> tryCatch) {
        return new Try<>(value, tryCatch);
    }

    public <R> Try<R> onNext(Function<? super T, ? extends R> mapper) {
        try {
            return new Try<>(mapper.apply(value), tryCatch);
        } catch (Exception e) {
            if (Objects.nonNull(tryCatch)) {
                var recovery = this.tryCatch.apply(e);
                return (Try<R>) new Try<>(recovery, tryCatch);
            }
            throw e;
        }
    }

    @Override
    public <R> Try<R> onError(Function<? super Throwable, ? extends R> function) {
        this.tryCatch = function;
        return (Try<R>) this;
    }

    @Override
    public <R> R onComplete() {
        return (R) this.value;
    }
}
